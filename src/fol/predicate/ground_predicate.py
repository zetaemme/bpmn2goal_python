from dataclasses import dataclass
from typing import Any, Self, override

from src.fol.term import ConstantTerm


@dataclass(frozen=True)
class GroundPredicate:
    functional: str
    terms: list[ConstantTerm]

    @classmethod
    def create(cls, functional: str, terms: list[ConstantTerm]) -> Self:
        return cls(functional, terms)

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, GroundPredicate) and self.functional == other.functional and self.terms == other.terms

    @override
    def __hash__(self) -> int:
        return hash((self.functional, tuple(self.terms)))

    @override
    def __str__(self) -> str:
        return f"{self.functional}({', '.join(map(str, self.terms))})"

    @property
    def is_true(self) -> bool:
        return self.functional == "true"

    @property
    def is_false(self) -> bool:
        return self.functional == "false"
