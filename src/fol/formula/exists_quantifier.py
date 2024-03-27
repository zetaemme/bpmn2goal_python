from dataclasses import dataclass
from typing import Any, override

from ..predicate import Predicate
from ..term import VariableTerm


@dataclass(frozen=True)
class ExistsQuantifier:
    predicate: Predicate
    vars: list[VariableTerm]

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, ExistsQuantifier) and self.predicate == other.predicate and self.vars == other.vars

    @override
    def __hash__(self) -> int:
        return hash((self.predicate, tuple(self.vars)))

    def prettify(self, force_brackets: bool = False) -> str:
        return f"exists {', '.join(map(str, self.vars))} such that {str(self.predicate)}"
