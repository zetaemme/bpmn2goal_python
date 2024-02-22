from dataclasses import dataclass

from .constant_term import ConstantTerm


@dataclass(frozen=True)
class AtomTerm(ConstantTerm):
    atom: str

    def __str__(self) -> str:
        return self.atom
