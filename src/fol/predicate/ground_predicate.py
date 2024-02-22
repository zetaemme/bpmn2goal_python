from dataclasses import dataclass
from typing import Self

from src.fol.term import ConstantTerm


@dataclass(frozen=True)
class GroundPredicate:
    functional: str
    terms: list[ConstantTerm]

    @classmethod
    def create(cls, functional: str, terms: list[ConstantTerm]) -> Self:
        return cls(functional, terms)

    def __str__(self) -> str:
        return f"{self.functional}({', '.join(map(str, self.terms))})"

    @property
    def is_true(self) -> bool:
        return self.functional == "true"

    @property
    def is_false(self) -> bool:
        return self.functional == "false"
