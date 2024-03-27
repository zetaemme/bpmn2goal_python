from dataclasses import dataclass
from typing import Any, override

from .formula import Formula


@dataclass(frozen=True)
class Falsity(Formula):
    @override
    def __eq__(self, other: Any) -> bool:
        return isinstance(other, Falsity)

    @override
    def __hash__(self) -> int:
        return hash("false")

    @override
    def __str__(self) -> str:
        return "false"

    @override
    def prettify(self, force_brackets: bool = False) -> str:
        return "<unspecified>"
