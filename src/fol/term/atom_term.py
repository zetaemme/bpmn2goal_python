from dataclasses import dataclass
from typing import Any, override

from .constant_term import ConstantTerm


@dataclass(frozen=True)
class AtomTerm(ConstantTerm):
    atom: str

    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, AtomTerm) and self.atom == other.atom

    @override
    def __hash__(self) -> int:
        return hash(self.atom)

    @override
    def __str__(self) -> str:
        return self.atom
