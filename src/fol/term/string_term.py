from dataclasses import dataclass

from . import ConstantStringTerm


@dataclass(frozen=True)
class StringTerm(ConstantStringTerm):
    value: str

    def __str__(self) -> str:
        return f"\"{self.value}\""
