from dataclasses import dataclass

from .formula import Formula


@dataclass(frozen=True)
class Falsity(Formula):
    def __str__(self) -> str:
        return "false"

    def prettify(self, force_brackets: bool = False) -> str:
        return "<unspecified>"
