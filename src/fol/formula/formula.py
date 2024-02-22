from abc import ABC, abstractmethod


class Formula:
    @abstractmethod
    def prettify(self, force_brackets: bool = False) -> str: ...
