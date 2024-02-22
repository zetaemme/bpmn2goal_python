from dataclasses import dataclass, field
from pathlib import Path
from typing import Optional
from xml.etree.ElementTree import Element, ElementTree, parse

from src.fol import Parser
from src.workflow import *


@dataclass
class BpmnLoader:
    data: list[Data] = field(init=False)
    data_objects: list[DataObjectRef] = field(init=False)
    datatypes: list[DataType] = field(init=False)
    items: list[Item] = field(init=False)
    flows: list[Flow] = field(init=False)
    messages: list[Message] = field(init=False)
    signals: list[Signal] = field(init=False)

    xml_tree: ElementTree = field(init=False)

    def load(self, path: str) -> None:
        filepath = Path(path)

        if not filepath.exists():
            raise FileNotFoundError(f"File {path} not found")

        self.xml_tree = parse(filepath.as_posix())

    def extract_workflow(self) -> Optional[Workflow]:
        if self.xml_tree is None or self.xml_tree.getroot() is None:
            return None

        root = self.xml_tree.getroot()
        if "definitions" not in root.tag:
            return None

        self.datatypes = self.parse_items(root.findall(".//dataObject"))
        self.messages = self.parse_messages(root.findall(".//message"))
        self.data_objects = self.parse_data_objects(
            root.findall(".//dataObjectReference")
        ) + self.parse_data_store(
            root.findall(".//dataStore")
        )
        self.signals = self.parse_signal(root.findall(".//signal"))

        self.data = self.datatypes + self.messages + self.data_objects + self.signals

        items_tasks = self.parse_tasks(root.findall(".//task"), "task")
        items_receive_tasks = self.parse_tasks(root.findall(".//receiveTask"), "receive")
        items_send_tasks = self.parse_tasks(root.findall(".//sendTask"), "send")
        items_user_tasks = self.parse_tasks(root.findall(".//userTask"), "user")
        items_manual_tasks = self.parse_tasks(root.findall(".//manualTask"), "user")
        items_service_tasks = self.parse_tasks(root.findall(".//serviceTask"), "services")
        items_script_tasks = self.parse_tasks(root.findall(".//scriptTask"), "script")

        event_start = self.parse_event(root.findall(".//startEvent"), "start")
        event_end = self.parse_event(root.findall(".//endEvent"), "start")
        event_boundary = self.parse_event(root.findall(".//boundaryEvent"), "boundary")
        intermediate = self.parse_event(root.findall(".//intermediateCatchEvent"), "interm_catch")

        exclusive_gateways = self.parse_gateway(root.findall(".//exclusiveGateway"), "exclusive")
        inclusive_gateways = self.parse_gateway(root.findall(".//inclusiveGateway"), "inclusive")
        parallel_gateways = self.parse_gateway(root.findall(".//parallelGateway"), "parallel")

        self.items = items_tasks + items_receive_tasks + items_send_tasks + items_user_tasks + items_manual_tasks + \
                     items_service_tasks + items_script_tasks + event_start + event_end + event_boundary + \
                     intermediate + exclusive_gateways + inclusive_gateways + parallel_gateways

        sequence_flows = self.parse_sequence_flows(root.findall(".//sequenceFlow"))
        message_flows = self.parse_message_flows(root.findall(".//messageFlow"))
        data_input_flows = self.parse_data_input_flows(
            root.findall(".//task") + root.findall(".//receiveTask") + root.findall(".//sendTask") +
            root.findall(".//userTask") + root.findall(".//manualTask") + root.findall(".//serviceTask")
        )
        data_output_flows = self.parse_data_output_flows(
            root.findall(".//task") + root.findall(".//receiveTask") + root.findall(".//sendTask") +
            root.findall(".//userTask") + root.findall(".//manualTask") + root.findall(".//serviceTask")
        )
        boundary_flows = self.parse_boundary_flows(root.findall(".//boundaryEvent"))

        self.flows = sequence_flows + message_flows + data_input_flows + data_output_flows + boundary_flows

        return Workflow(self.datatypes, self.items, self.flows, self.data)

    def parse_items(self, items: list[Element]) -> list[DataType]:
        return [DataType(item.get("id"), item.get("name")) for item in reversed(items)]

    def parse_messages(self, items: list[Element]) -> list[Message]:
        return [Message(item.get("id"), item.get("name")) for item in reversed(items)]

    def parse_data_objects(self, items: list[Element]) -> list[DataObjectRef]:
        refs = []

        for item in reversed(items):
            id = item.get("id")
            ref = item.get("dataObjectRef")
            stage_tag = item.find(".//dataState")

            data_type = self.search_datatype_by_id(ref)
            if data_type is not None:
                if len(stage_tag) > 0:
                    refs.append(DataObjectRef(id, data_type, stage_tag[0].get("name")))
                else:
                    refs.append(DataObjectRef(id, data_type, None))

        return refs

    def parse_data_store(self, items: list[Element]) -> list[DataObjectRef]:
        return [
            DataObjectRef(item.get("id"), DataType(item.get("id"), item.get("name")), None)
            for item in reversed(items)
        ]

    def parse_signal(self, items: list[Element]) -> list[Signal]:
        return [Signal(item.get("id"), item.get("name")) for item in reversed(items)]

    def parse_tasks(self, items: list[Element], tag: str) -> list[Task]:
        tasks = []

        for item in reversed(items):
            id = item.get("id")
            label = item.get("name")

            message_id = item.get("messageRef")
            if message_id is not None:
                message = self.search_message_by_id(message_id)
                tasks.append(Task(id, label, tag, message))
            else:
                tasks.append(Task(id, label, tag))

        return tasks

    def parse_event(self, items: list[Element], tag: str) -> list[Event]:
        events = []

        for item in reversed(items):
            id = item.get("id")
            label = item.get("name")

            message_id = item.find(".//messageEventDefinition").get("messageRef")
            signal_id = item.find(".//signalEventDefinition").get("signalRef")

            duration = item.find(".//timeDuration").text
            time_date = item.find(".//timeDate").text
            repetition = item.find(".//timeCycle").text

            if message_id is not None:
                message = self.search_message_by_id(message_id)
                events.append(Event(id, label, tag, MessageEventDefinition(message)))
            elif signal_id is not None:
                signal = self.search_signal_by_id(signal_id)
                events.append(Event(id, label, tag, SignalEventDefinition(signal)))
            elif duration is not None:
                events.append(Event(id, label, tag, TimerEventDefinition("duration", duration)))
            elif time_date is not None:
                events.append(Event(id, label, tag, TimerEventDefinition("time_date", time_date)))
            elif repetition is not None:
                events.append(Event(id, label, tag, TimerEventDefinition("repetition", repetition)))

        return events

    def parse_gateway(self, items: list[Element], tag: str) -> list[Gateway]:
        gateways = []

        for item in reversed(items):
            id = item.get("id")
            label = item.get("name")
            direction = self.parse_gateway_direction(item)

            gateways.append(Gateway(id, label, tag, direction))

        return gateways

    def parse_gateway_direction(self, item: Element) -> GatewayDirection:
        ins = item.findall(".//incoming")
        outs = item.findall(".//outgoing")

        if len(ins) == 1:
            return Diverging()
        elif len(outs) == 1:
            return Converging()
        else:
            return Unspecified()

    def parse_sequence_flows(self, items: list[Element]) -> list[SequenceFlow]:
        flows = []

        for item in reversed(items):
            id = item.get("id")
            source = item.get("sourceRef")
            target = item.get("targetRef")

            start = self.search_item_by_id(source)
            end = self.search_item_by_id(target)

            condition_tags = item.find(".//conditionExpression")
            opt_condition = None
            if len(condition_tags) > 0:
                parser = Parser()
                parse_result = parser.parse(condition_tags.text)

                if parse_result:
                    opt_condition = parse_result[0]

            flows.append(SequenceFlow(id, start, end, opt_condition))

        return flows

    def parse_message_flows(self, items: list[Element]) -> list[MessageFlow]:
        flows = []

        for item in reversed(items):
            id = item.get("id")
            source = item.get("sourceRef")
            target = item.get("targetRef")
            message_id = item.get("messageRef")

            message = self.search_message_by_id(message_id)
            start = self.search_item_by_id(source)
            end = self.search_item_by_id(target)

            if message is not None and end is not None and isinstance(end, Task):
                flows.append(InMessageFlow(id, end, message))
            elif message is not None and start is not None and isinstance(start, Task):
                flows.append(OutMessageFlow(id, start, message))

        return flows

    def parse_data_input_flows(self, items: list[Element]) -> list[DataInputFlow]:
        flows = []

        for item in reversed(items):
            task = self.search_item_by_id(item.get("id"))

            data_inputs = item.findall(".//dataInputAssociation")
            for data_input in data_inputs:
                if data_input is not None:
                    id = data_input.get("id")
                    data_id = data_input.get("sourceRef")
                    data = self.search_dataobjectref_by_id(data_id)

                    if task is not None and data is not None:
                        flows.append(DataInputFlow(id, task, data))

        return flows

    def parse_data_output_flows(self, items: list[Element]) -> list[DataOutputFlow]:
        flows = []

        for item in reversed(items):
            task = self.search_item_by_id(item.get("id"))

            data_inputs = item.findall(".//dataInputAssociation")
            for data_input in data_inputs:
                if data_input is not None:
                    id = data_input.get("id")
                    data_id = data_input.get("sourceRef")
                    data = self.search_dataobjectref_by_id(data_id)

                    if task is not None and data is not None:
                        flows.append(DataOutputFlow(id, task, data))

        return flows

    def parse_boundary_flows(self, items: list[Element]) -> list[BoundaryFlow]:
        flows = []

        for item in reversed(items):
            id = item.get("id")
            task = self.search_item_by_id(id)
            event = self.search_item_by_id(item.get("attachedToRef"))

            flows.append(BoundaryFlow(id, task, event))

        return flows

    def search_datatype_by_id(self, id: str) -> Optional[DataType]:
        item = None
        remaining = self.datatypes

        while item is None and len(remaining) > 0:
            if remaining[0].id == id:
                item = remaining[0]

            remaining = remaining[1:]

        return item

    def search_message_by_id(self, id: str) -> Optional[Message]:
        item = None
        remaining = self.messages

        while item is None and len(remaining) > 0:
            if remaining[0].id == id:
                item = remaining[0]

            remaining = remaining[1:]

        return item

    def search_signal_by_id(self, id: str) -> Optional[Signal]:
        item = None
        remaining = self.signals

        while item is None and len(remaining) > 0:
            if remaining[0].id == id:
                item = remaining[0]

            remaining = remaining[1:]

        return item

    def search_item_by_id(self, id: str) -> Optional[Item]:
        item = None
        remaining = self.items

        while item is None and len(remaining) > 0:
            if remaining[0].id == id:
                item = remaining[0]

            remaining = remaining[1:]

        return item

    def search_dataobjectref_by_id(self, id: str) -> Optional[DataObjectRef]:
        item = None
        remaining = self.data_objects

        while item is None and len(remaining) > 0:
            if remaining[0].id == id:
                item = remaining[0]

            remaining = remaining[1:]

        return item
