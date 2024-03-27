from dataclasses import dataclass
from typing import Any, override

from .formula import Formula
from ..predicate import GroundPredicate


@dataclass(frozen=True)
class GroundLiteral(Formula):
    predicate: GroundPredicate

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, GroundLiteral) and self.predicate == other.predicate

    @override
    def __hash__(self) -> int:
        return hash(self.predicate)

    @override
    def prettify(self, force_brackets: bool = False) -> str:
        return str(self.predicate)
