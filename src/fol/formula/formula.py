from abc import ABC, abstractmethod


class Formula(ABC):
    @abstractmethod
    def prettify(self, force_brackets: bool = False) -> str: ...
