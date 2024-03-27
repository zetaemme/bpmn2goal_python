from dataclasses import dataclass
from typing import Any, override

from ..predicate import Predicate
from ..term import VariableTerm


@dataclass(frozen=True)
class UniversalQuantifier:
    predicate: Predicate
    vars: list[VariableTerm]

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, UniversalQuantifier) and self.predicate == other.predicate and self.vars == other.vars

    @override
    def __hash__(self) -> int:
        return hash((self.predicate, *self.vars))

    def prettify(self) -> str:
        return f"for each {','.join(map(str, self.vars))} such that {str(self.predicate)}"
