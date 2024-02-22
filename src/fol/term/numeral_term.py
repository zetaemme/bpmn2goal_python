from dataclasses import dataclass

from .term import Term


@dataclass(frozen=True)
class NumeralTerm(Term):
    number: float

    def __str__(self) -> str:
        return str(self.number)
