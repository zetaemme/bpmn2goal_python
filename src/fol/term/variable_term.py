from dataclasses import dataclass

from .term import Term


@dataclass(frozen=True)
class VariableTerm(Term):
    name: str

    def __str__(self) -> str:
        return self.name
