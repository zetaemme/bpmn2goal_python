from dataclasses import dataclass

from .formula import Formula


@dataclass
class Disjunction(Formula):
    formulas: list[Formula]

    def prettify(self, force_brackets: bool = False) -> str:
        if force_brackets:
            return f"({' or '.join([f.prettify(True) for f in self.formulas])})"

        return f"{' or '.join([f.prettify(True) for f in self.formulas])}"
