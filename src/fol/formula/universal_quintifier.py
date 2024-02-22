from dataclasses import dataclass

from ..predicate import Predicate
from ..term import VariableTerm


@dataclass(frozen=True)
class UniversalQuantifier:
    predicate: Predicate
    vars: list[VariableTerm]

    def prettify(self) -> str:
        return f"for each {','.join(map(str, self.vars))} such that {str(self.predicate)}"
