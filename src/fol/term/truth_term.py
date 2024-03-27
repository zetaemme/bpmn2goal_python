from dataclasses import dataclass
from typing import Any, override

from . import ConstantStringTerm


@dataclass(frozen=True)
class TruthTerm(ConstantStringTerm):
    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, TruthTerm)

    @override
    def __hash__(self) -> int:
        return hash("true")

    @override
    def __str__(self) -> str:
        return "true"
