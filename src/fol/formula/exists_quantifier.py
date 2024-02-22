from dataclasses import dataclass

from ..predicate import Predicate
from ..term import VariableTerm


@dataclass(frozen=True)
class ExistsQuantifier:
    predicate: Predicate
    vars: list[VariableTerm]

    def prettify(self, force_brackets: bool = False) -> str:
        return f"exists {', '.join(map(str, self.vars))} such that {str(self.predicate)}"
