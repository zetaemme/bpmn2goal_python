from dataclasses import dataclass

from . import ConstantStringTerm


@dataclass(frozen=True)
class FalsityTerm(ConstantStringTerm):
    def __str__(self) -> str:
        return "false"
