from dataclasses import dataclass
from typing import Any, override

from .term import Term


@dataclass(frozen=True)
class NumeralTerm(Term):
    number: float

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, NumeralTerm) and self.number == other.number

    @override
    def __hash__(self) -> int:
        return hash(self.number)

    @override
    def __str__(self) -> str:
        return str(self.number)
