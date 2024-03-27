from dataclasses import dataclass
from typing import Any, override

from .formula import Formula
from ..predicate import Predicate


@dataclass(frozen=True)
class Literal(Formula):
    predicate: Predicate

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, Literal) and self.predicate == other.predicate

    @override
    def __hash__(self) -> int:
        return hash(self.predicate)

    @override
    def prettify(self, force_brackets: bool = False) -> str:
        return str(self.predicate)
