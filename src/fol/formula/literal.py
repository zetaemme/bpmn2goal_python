from dataclasses import dataclass

from .formula import Formula
from ..predicate import Predicate


@dataclass(frozen=True)
class Literal(Formula):
    predicate: Predicate

    def prettify(self, force_brackets: bool = False) -> str:
        return str(self.predicate)
