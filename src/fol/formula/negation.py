from dataclasses import dataclass

from .formula import Formula


@dataclass(frozen=True)
class Negation(Formula):
    formula: Formula

    def prettify(self, force_brackets: bool = False) -> str:
        return f"not {self.formula.prettify(True)}"
