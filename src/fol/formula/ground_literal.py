from dataclasses import dataclass

from .formula import Formula
from ..predicate import GroundPredicate


@dataclass(frozen=True)
class GroundLiteral(Formula):
    predicate: GroundPredicate

    def prettify(self, force_brackets: bool = False) -> str:
        return str(self.predicate)
