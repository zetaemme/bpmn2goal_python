from dataclasses import dataclass

from . import ConstantStringTerm


@dataclass(frozen=True)
class TruthTerm(ConstantStringTerm):
    def __str__(self) -> str:
        return "true"
