from dataclasses import dataclass, field
from typing import Any, Self, override

from src.fol.term import Term


@dataclass(frozen=True)
class Predicate:
    functional: str
    terms: list[Term] = field(default_factory=list)

    @classmethod
    def create(cls, functional: str, terms: list[Term]) -> Self:
        return cls(functional, terms)

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, Predicate) and self.functional == other.functional and self.terms == other.terms

    @override
    def __hash__(self) -> int:
        return hash((self.functional, tuple(self.terms)))

    @override
    def __str__(self) -> str:
        return f"{self.functional}({', '.join(map(str, self.terms))})"
