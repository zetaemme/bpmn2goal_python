from abc import ABC
from dataclasses import dataclass
from typing import Optional

from src.fol.formula import Formula


@dataclass(frozen=True)
class Workflow:
    datatypes: list["DataType"]
    items: list["Item"]
    flows: list["Flow"]
    data: list["Data"]


@dataclass(frozen=True)
class DataType:
    id: str
    name: str


# DATA
class Data(ABC):
    id: str


@dataclass(frozen=True)
class DataObjectRef(Data):
    id: str
    object_type: DataType
    state: Optional[str]


@dataclass(frozen=True)
class Message(Data):
    id: str
    label: str


@dataclass(frozen=True)
class Signal(Data):
    id: str
    label: str


# EVENT DEFINITION
class EventDefinition(ABC):
    pass


@dataclass(frozen=True)
class EmptyEventDefinition(EventDefinition):
    pass


@dataclass(frozen=True)
class MessageEventDefinition(EventDefinition):
    message: Message


@dataclass(frozen=True)
class SignalEventDefinition(EventDefinition):
    signal: Signal


@dataclass(frozen=True)
class TimerEventDefinition(EventDefinition):
    timer_type: str
    time_condition: str


# FLOW
class Flow(ABC):
    id: str


class MessageFlow(Flow, ABC):
    id: str


@dataclass
class BoundaryFlow(Flow):
    id: str
    source: "Task"
    boundary: "Event"


@dataclass(frozen=True)
class DataInputFlow(Flow):
    id: str
    target: "Task"
    data: DataObjectRef


@dataclass(frozen=True)
class DataOutputFlow(Flow):
    id: str
    source: "Task"
    data: DataObjectRef


@dataclass(frozen=True)
class InMessageFlow(MessageFlow):
    id: str
    receiver: "Task"
    message: Message


@dataclass(frozen=True)
class OutMessageFlow(MessageFlow):
    id: str
    sender: "Task"
    message: Message


@dataclass(frozen=True)
class SequenceFlow(Flow):
    id: str
    start: "Item"
    end: "Item"
    condition: Optional[Formula]


# GATEWAY DIRECTION
class GatewayDirection(ABC):
    pass


@dataclass(frozen=True)
class Converging(GatewayDirection):
    pass


@dataclass(frozen=True)
class Diverging(GatewayDirection):
    pass


@dataclass(frozen=True)
class Unspecified(GatewayDirection):
    pass


# ITEM
class Item(ABC):
    id: str


@dataclass(frozen=True)
class Event(Item):
    id: str
    label: str
    event_type: str
    definition: EventDefinition


@dataclass(frozen=True)
class Gateway(Item):
    id: str
    label: str
    type: str
    direction: GatewayDirection


@dataclass(frozen=True)
class Task(Item):
    id: str
    label: str
    task_type: str
    message: Optional[Message] = None
