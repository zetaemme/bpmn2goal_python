from dataclasses import dataclass
from typing import Any, override

from .formula import Formula


@dataclass(frozen=True)
class Negation(Formula):
    formula: Formula

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, Negation) and self.formula == other.formula

    @override
    def __hash__(self) -> int:
        return hash(self.formula)

    @override
    def prettify(self, force_brackets: bool = False) -> str:
        return f"not {self.formula.prettify(True)}"
