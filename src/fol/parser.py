from pyparsing import *

from src.fol.formula import Conjunction, Disjunction, Negation
from src.fol.predicate import Predicate
from src.fol.term import AtomTerm, NumeralTerm
from src.fol.term.falsity_term import FalsityTerm
from src.fol.term.string_term import StringTerm
from src.fol.term.truth_term import TruthTerm


class Parser:
    def __init__(self):
        self.formula = Forward()

        self.constant = (Regex(r"\d+(\.\d*)?").setParseAction(self.handle_numeral) |
                         QuotedString('"').setParseAction(self.handle_string))
        self.atom = (Word(alphas).setParseAction(self.handle_atom) |
                     Keyword("true").setParseAction(self.handle_truth) |
                     Keyword("false").setParseAction(self.handle_falsity))

        self.term = self.constant | self.atom
        self.term_list = delimitedList(self.term, ",")

        self.predicate = Group(Word(alphas) + "(" + Optional(self.term_list) + ")").setParseAction(self.handle_predicate)
        self.conjunction = Group("(" + delimitedList(self.formula, "and") + ")").setParseAction(self.handle_conjunction)
        self.disjunction = Group("(" + delimitedList(self.formula, "or") + ")").setParseAction(self.handle_disjunction)
        self.negation = Group("not" + self.formula).setParseAction(self.handle_negation)

        self.formula << (self.predicate | self.conjunction | self.disjunction | self.negation)

    def handle_predicate(self, tokens) -> Literal:
        func, terms = tokens[0][0], tokens[0][2]
        predicate = Predicate(func, terms if terms else [])
        return Literal(str(predicate))

    def handle_conjunction(self, tokens) -> Conjunction:
        return Conjunction(tokens[0][1::2])

    def handle_disjunction(self, tokens) -> Disjunction:
        return Disjunction(tokens[0][1::2])

    def handle_negation(self, tokens) -> Negation:
        return Negation(tokens[0][1])

    def handle_numeral(self, tokens) -> NumeralTerm:
        return NumeralTerm(float(tokens[0]))

    def handle_string(self, tokens) -> StringTerm:
        return StringTerm(tokens[0])

    def handle_atom(self, tokens) -> AtomTerm:
        return AtomTerm(tokens[0])

    def handle_truth(self, tokens) -> TruthTerm:
        return TruthTerm()

    def handle_falsity(self, tokens) -> FalsityTerm:
        return FalsityTerm()

    def parse(self, string) -> ParseResults:
        return self.formula.parse_string(string, parse_all=True)
