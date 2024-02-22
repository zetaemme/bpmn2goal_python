from dataclasses import dataclass, field
from typing import Self

from src.fol.term import Term


@dataclass(frozen=True)
class Predicate:
    functional: str
    terms: list[Term] = field(default_factory=list)

    @classmethod
    def create(cls, functional: str, terms: list[Term]) -> Self:
        return cls(functional, terms)

    def __str__(self) -> str:
        return f"{self.functional}({', '.join(map(str, self.terms))})"
