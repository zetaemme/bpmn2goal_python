from dataclasses import dataclass
from typing import Any, override

from .term import Term


@dataclass(frozen=True)
class VariableTerm(Term):
    name: str

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, VariableTerm) and self.name == other.name

    @override
    def __hash__(self) -> int:
        return hash(self.name)

    @override
    def __str__(self) -> str:
        return self.name
