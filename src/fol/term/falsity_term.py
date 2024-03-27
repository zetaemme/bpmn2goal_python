from dataclasses import dataclass
from typing import Any, override

from . import ConstantStringTerm


@dataclass(frozen=True)
class FalsityTerm(ConstantStringTerm):
    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, FalsityTerm)

    @override
    def __hash__(self) -> int:
        return hash("false")

    @override
    def __str__(self) -> str:
        return "false"
