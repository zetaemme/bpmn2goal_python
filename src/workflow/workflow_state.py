from dataclasses import dataclass
from typing import Generator

from .workflow import (
    BoundaryFlow,
    DataInputFlow,
    DataObjectRef,
    DataOutputFlow,
    Event,
    Gateway,
    Item,
    Message,
    MessageEventDefinition,
    SequenceFlow,
    SignalEventDefinition,
    Task,
    TimerEventDefinition,
    Workflow
)
from ..fol.formula import Conjunction, Disjunction, ExclusiveDisjunction, Formula, GroundLiteral, Truth
from ..fol.predicate import GroundPredicate
from ..fol.term import AtomTerm


@dataclass
class WorkflowState:
    workflow: Workflow

    def waited_state(self, item: Item) -> Formula:
        if isinstance(item, Task):
            return self.task_waited_state(item)

        if isinstance(item, Event):
            return self.event_waited_state(item)

        if isinstance(item, Gateway):
            return self.gateway_waited_state(item)

    def generated_state(self, item: Item) -> Formula:
        if isinstance(item, Task):
            return self.task_generated_state(item)

        if isinstance(item, Event):
            return self.event_generated_state(item)

        if isinstance(item, Gateway):
            return self.gateway_generated_state(item)

    def successors_influence(self, item: Item) -> Formula:
        successors_disjunctions = []

        for flow in self.outgoing_sequence(item):
            if flow.condition is not None:
                successors_disjunctions.append(
                    self.conjunction_or_truth([self.inverse_propagation(flow), flow.condition])
                )
            else:
                successors_disjunctions.append(self.inverse_propagation(flow))

        successors_exclusive_disjunction_or_truth = self.exclusive_disjunction_or_truth(successors_disjunctions)
        return successors_exclusive_disjunction_or_truth

    def predecessors_influence(self, item: Item) -> Formula:
        predecessors_disjunctions = []

        for flow in self.incoming_sequence(item):
            if flow.condition is not None:
                predecessors_disjunctions.append(
                    self.conjunction_or_truth([self.direct_propagation(flow), flow.condition])
                )
            else:
                predecessors_disjunctions.append(self.direct_propagation(flow))

        pred_exclusive_disjunction_or_truth = self.exclusive_disjunction_or_truth(predecessors_disjunctions)
        return pred_exclusive_disjunction_or_truth

    def goal_trigger_condition(self, item: Item) -> Formula:
        trigger_waited_state = self.waited_state(item)
        trigger_predecessors_influence = self.predecessors_influence(item)
        return self.conjunction_or_truth([trigger_waited_state, trigger_predecessors_influence])

    def goal_final_state(self, item: Item) -> Formula:
        final_generated_state = self.generated_state(item)
        final_successors_influence = self.successors_influence(item)
        return self.conjunction_or_truth([final_generated_state, final_successors_influence])

    def direct_propagation(self, flow: SequenceFlow) -> Formula:
        predecessor = flow.start

        if isinstance(predecessor, Task) or isinstance(predecessor, Event):
            return self.generated_state(predecessor)

        if isinstance(predecessor, Gateway):
            if predecessor.type == "exclusive":
                direct_exclusive_disjunctions = []

                for exclusive_flow in self.incoming_sequence(predecessor):
                    if exclusive_flow.condition is not None:
                        direct_exclusive_disjunctions.append(
                            self.conjunction_or_truth(
                                [self.direct_propagation(exclusive_flow), exclusive_flow.condition]
                            )
                        )
                    else:
                        direct_exclusive_disjunctions.append(self.direct_propagation(exclusive_flow))

                return self.exclusive_disjunction_or_truth(direct_exclusive_disjunctions)
            elif predecessor.type == "inclusive":
                direct_inclusive_disjunctions = []

                for inclusive_flow in self.incoming_sequence(predecessor):
                    if inclusive_flow.condition is not None:
                        direct_inclusive_disjunctions.append(
                            self.conjunction_or_truth(
                                [self.direct_propagation(inclusive_flow), inclusive_flow.condition]
                            )
                        )
                    else:
                        direct_inclusive_disjunctions.append(self.direct_propagation(inclusive_flow))

                return self.disjunction_or_truth(direct_inclusive_disjunctions)
            elif predecessor.type == "parallel":
                direct_parallel_conjunctions = []

                for parallel_flow in self.incoming_sequence(predecessor):
                    if parallel_flow.condition is not None:
                        direct_parallel_conjunctions.append(
                            self.conjunction_or_truth([self.direct_propagation(parallel_flow), parallel_flow.condition])
                        )
                    else:
                        direct_parallel_conjunctions.append(self.direct_propagation(parallel_flow))

                return self.conjunction_or_truth(direct_parallel_conjunctions)
            else:
                return Truth()

        return Truth()

    def inverse_propagation(self, flow: SequenceFlow) -> Formula:
        successor = flow.end

        if isinstance(successor, Task) or isinstance(successor, Event):
            return self.waited_state(successor)

        if isinstance(successor, Gateway):
            if successor.type == "exclusive":
                outgoing_exclusive_disjunctions = []

                for exclusive_flow in self.outgoing_sequence(successor):
                    if exclusive_flow.condition is not None:
                        outgoing_exclusive_disjunctions.append(
                            self.conjunction_or_truth(
                                [self.inverse_propagation(exclusive_flow), exclusive_flow.condition]
                            )
                        )
                    else:
                        outgoing_exclusive_disjunctions.append(self.inverse_propagation(exclusive_flow))

                return self.exclusive_disjunction_or_truth(outgoing_exclusive_disjunctions)
            elif successor.type == "inclusive":
                outgoing_inclusive_disjunctions = []

                for inclusive_flow in self.outgoing_sequence(successor):
                    if inclusive_flow.condition is not None:
                        outgoing_inclusive_disjunctions.append(
                            self.conjunction_or_truth(
                                [self.inverse_propagation(inclusive_flow), inclusive_flow.condition]
                            )
                        )
                    else:
                        outgoing_inclusive_disjunctions.append(self.inverse_propagation(inclusive_flow))

                return self.disjunction_or_truth(outgoing_inclusive_disjunctions)
            elif successor.type == "parallel":
                outgoing_parallel_conjunctions = []

                for parallel_flow in self.outgoing_sequence(successor):
                    if parallel_flow.condition is not None:
                        outgoing_parallel_conjunctions.append(
                            self.conjunction_or_truth(
                                [self.inverse_propagation(parallel_flow), parallel_flow.condition]
                            )
                        )
                    else:
                        outgoing_parallel_conjunctions.append(self.inverse_propagation(parallel_flow))

                return self.conjunction_or_truth(outgoing_parallel_conjunctions)
            else:
                return Truth()

        return Truth()

    def task_waited_state(self, task: Task) -> Formula:
        formulas = []

        for data in self.expected_data(task):
            state = self.to_label(data.state) if data.state is not None else "available"
            predicate = GroundPredicate(state, [AtomTerm(self.to_label(data.object_type.name))])
            formulas.append(GroundLiteral(predicate))

        for message in self.expected_messages(task):
            state = "received"
            predicate = GroundPredicate(state, [AtomTerm(self.to_label(message.label))])
            formulas.append(GroundLiteral(predicate))

        return self.conjunction_or_truth(formulas)

    def event_waited_state(self, event: Event) -> Formula:
        return Truth()

    def gateway_waited_state(self, gateway: Gateway) -> Formula:
        return Truth()

    def task_generated_state(self, task: Task) -> Formula:
        normal_termination_terms = []

        for data in self.produced_data(task):
            state = self.to_label(data.state) if data.state is not None else "available"
            predicate = GroundPredicate(state, [AtomTerm(self.to_label(data.object_type.name))])
            normal_termination_terms.append(GroundLiteral(predicate))

        for message in self.produced_messages(task):
            state = "sent"
            predicate = GroundPredicate(state, [AtomTerm(self.to_label(message.label))])
            normal_termination_terms.append(GroundLiteral(predicate))

        if len(normal_termination_terms) == 0:
            normal_termination_terms.append(
                GroundLiteral(GroundPredicate("done", [AtomTerm(self.to_label(task.label))]))
            )

        normal_termination = self.conjunction_or_truth(normal_termination_terms)

        boundary_termination_terms = [normal_termination]

        for boundary_flow in self.attached_boundary_conditions(task):
            boundary_termination_terms.append(self.event_generated_state(boundary_flow.boundary))

        return self.exclusive_disjunction_or_truth(boundary_termination_terms)

    def attached_boundary_conditions(self, task: Task) -> Generator[BoundaryFlow, None, None]:
        yield from (
            flow
            for flow in reversed(self.workflow.flows)
            if isinstance(flow, BoundaryFlow) and flow.source == task
        )

    def event_generated_state(self, event: Event) -> Formula:
        conjunctions = []

        if isinstance(event.definition, MessageEventDefinition):
            state = "received"
            if len(event.definition.message.label) > 0:
                predicate = GroundPredicate(state, [AtomTerm(self.to_label(event.definition.message.label))])
                conjunctions.append(GroundLiteral(predicate))
            else:
                conjunctions.append(GroundLiteral(GroundPredicate(state, [AtomTerm(self.to_label(event.label))])))
        elif isinstance(event.definition, SignalEventDefinition):
            state = "caught"
            conjunctions.append(
                GroundLiteral(GroundPredicate(state, [AtomTerm(self.to_label(event.definition.signal.label))]))
            )
        elif isinstance(event.definition, TimerEventDefinition):
            if event.definition.timer_type == "duration":
                if event.event_type == "boundary":
                    task = self.search_task_via_boundary(event)
                    predecessor_influence = self.predecessors_influence(task)
                    predicate = GroundPredicate("after", [
                        AtomTerm(event.definition.time_condition),
                        AtomTerm(predecessor_influence.prettify()),
                    ])
                    conjunctions.append(GroundLiteral(predicate))
                else:
                    predecessor_influence = self.predecessors_influence(event)
                    predicate = GroundPredicate("after", [
                        AtomTerm(event.definition.time_condition),
                        AtomTerm(predecessor_influence.prettify()),
                    ])
                    conjunctions.append(GroundLiteral(predicate))
            elif event.definition.timer_type == "timedate":
                predicate = GroundPredicate("at", [AtomTerm(event.definition.time_condition)])
                conjunctions.append(GroundLiteral(predicate))
            elif event.definition.timer_type == "repetition":
                predicate = GroundPredicate("every", [AtomTerm(event.definition.time_condition)])
                conjunctions.append(GroundLiteral(predicate))

        return self.conjunction_or_truth(conjunctions)

    def search_task_via_boundary(self, event: Event) -> Task:
        task = None

        for flow in self.workflow.flows:
            if isinstance(flow, BoundaryFlow) and flow.boundary == event:
                task = flow.source

        return task

    def gateway_generated_state(self, gateway: Gateway) -> Formula:
        return Truth()

    def incoming_sequence(self, item: Item) -> Generator[SequenceFlow, None, None]:
        yield from (
            flow
            for flow in reversed(self.workflow.flows)
            if isinstance(flow, SequenceFlow) and flow.end == item
        )

    def outgoing_sequence(self, item: Item) -> Generator[SequenceFlow, None, None]:
        yield from (
            flow
            for flow in reversed(self.workflow.flows)
            if isinstance(flow, SequenceFlow) and flow.start == item
        )

    def expected_data(self, task: Task) -> Generator[DataObjectRef, None, None]:
        yield from (
            flow.data
            for flow in reversed(self.workflow.flows)
            if isinstance(flow, DataInputFlow) and flow.target == task
        )

    def expected_messages(self, task: Task) -> list[Message]:
        messages = []

        if task.task_type == "receive" and task.message is not None:
            messages.append(task.message)

        return messages

    def produced_data(self, task: Task) -> Generator[DataObjectRef, None, None]:
        yield from (
            flow.data
            for flow in reversed(self.workflow.flows)
            if isinstance(flow, DataOutputFlow) and flow.source == task
        )

    def produced_messages(self, task: Task) -> list[Message]:
        messages = []

        if task.task_type == "send" and task.message is not None:
            messages.append(task.message)

        return messages

    def disjunction_or_truth(self, formulas: list[Formula]) -> Formula:
        not_true = set()

        for formula in formulas:
            if formula not in not_true and formula != Truth():
                not_true.add(formula)

        if len(not_true) == 0:
            return Truth()
        elif len(not_true) == 1:
            return list(not_true)[0]
        else:
            disjunction = Disjunction(not_true)
            return disjunction

    def exclusive_disjunction_or_truth(self, formulas: list[Formula]) -> Formula:
        not_true = set()

        for formula in formulas:
            if formula not in not_true and formula != Truth():
                not_true.add(formula)

        if len(not_true) == 0:
            return Truth()
        elif len(not_true) == 1:
            return list(not_true)[0]
        else:
            exclusive_disjunction = ExclusiveDisjunction(not_true)
            return exclusive_disjunction

    def conjunction_or_truth(self, formulas: list[Formula]) -> Formula:
        not_true = set()

        for formula in formulas:
            if formula not in not_true and formula != Truth():
                not_true.add(formula)

        if len(not_true) == 0:
            return Truth()
        elif len(not_true) == 1:
            return list(not_true)[0]
        else:
            conjunction = Conjunction(not_true)
            return conjunction

    def to_label(self, s: str) -> str:
        return s.translate(str.maketrans("\n ", "__")).lower()
