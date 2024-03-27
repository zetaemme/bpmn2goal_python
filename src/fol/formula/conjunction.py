from dataclasses import dataclass
from typing import Any, Collection, override

from .formula import Formula


@dataclass
class Conjunction(Formula):
    formulas: Collection[Formula]

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, Conjunction) and self.formulas == other.formulas

    @override
    def __hash__(self) -> int:
        return hash(tuple(self.formulas))

    @override
    def prettify(self, force_brackets: bool = False) -> str:
        if force_brackets:
            return f"({' and '.join([f.prettify(True) for f in self.formulas])})"

        return f"{' and '.join([f.prettify(True) for f in self.formulas])}"
