from dataclasses import dataclass

from .formula import Formula


@dataclass(frozen=True)
class Truth(Formula):
    def __str__(self) -> str:
        return "true"

    def prettify(self, force_brackets: bool = False) -> str:
        return "<unspecified>"
