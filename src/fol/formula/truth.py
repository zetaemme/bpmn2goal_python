from dataclasses import dataclass
from typing import Any, override

from .formula import Formula


@dataclass(frozen=True)
class Truth(Formula):
    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, Truth)

    @override
    def __hash__(self) -> int:
        return hash("true")

    @override
    def __str__(self) -> str:
        return "true"

    @override
    def prettify(self, force_brackets: bool = False) -> str:
        return "<unspecified>"
