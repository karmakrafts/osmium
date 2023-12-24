/*
 * Copyright 2023 Karma Krafts & associates
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.karma.ferrous.osmium.parser;

import io.karma.ferrous.antlr.ANTLRv4Lexer;
import io.karma.ferrous.antlr.ANTLRv4Parser;
import io.karma.ferrous.antlr.ANTLRv4ParserListener;
import io.karma.ferrous.osmium.grammar.LexerGrammar;
import io.karma.ferrous.osmium.util.DefaultErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public abstract class ParseAdapter implements ANTLRv4ParserListener {
    protected final Path parentDir;

    protected ParseAdapter(final Path parentDir) {
        this.parentDir = parentDir;
    }

    protected @Nullable LexerGrammar loadLexerGrammar(final String name) {
        final var path = parentDir.resolve(STR."\{name}.g4");
        if (!Files.exists(path)) {
            System.err.println(STR."Lexer grammar file \{path} does not exist");
            return null;
        }
        try (final var stream = Files.newInputStream(path); final var channel = Channels.newChannel(stream)) {
            final var charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8);
            final var lexer = new ANTLRv4Lexer(charStream);
            final var tokenStream = new CommonTokenStream(lexer);
            tokenStream.fill();
            final var parser = new ANTLRv4Parser(tokenStream);
            parser.removeErrorListeners();
            parser.addErrorListener(DefaultErrorListener.INSTANCE);
            final var context = parser.grammarSpec();
            final var lexerGrammar = LexerGrammarParser.parse(parentDir, context);
            if (lexerGrammar == null) {
                System.err.println(STR."Failed to parse lexer grammar file \{path}");
                return null;
            }
            return lexerGrammar;
        }
        catch (Throwable error) {
            System.err.println(STR."Could not load lexer grammar file \{path}: \{error.getMessage()}");
            return null;
        }
    }

    @Override
    public void enterGrammarSpec(ANTLRv4Parser.GrammarSpecContext grammarSpecContext) {

    }

    @Override
    public void exitGrammarSpec(ANTLRv4Parser.GrammarSpecContext grammarSpecContext) {

    }

    @Override
    public void enterGrammarDecl(ANTLRv4Parser.GrammarDeclContext grammarDeclContext) {

    }

    @Override
    public void exitGrammarDecl(ANTLRv4Parser.GrammarDeclContext grammarDeclContext) {

    }

    @Override
    public void enterGrammarType(ANTLRv4Parser.GrammarTypeContext grammarTypeContext) {

    }

    @Override
    public void exitGrammarType(ANTLRv4Parser.GrammarTypeContext grammarTypeContext) {

    }

    @Override
    public void enterPrequelConstruct(ANTLRv4Parser.PrequelConstructContext prequelConstructContext) {

    }

    @Override
    public void exitPrequelConstruct(ANTLRv4Parser.PrequelConstructContext prequelConstructContext) {

    }

    @Override
    public void enterOptionsSpec(ANTLRv4Parser.OptionsSpecContext optionsSpecContext) {

    }

    @Override
    public void exitOptionsSpec(ANTLRv4Parser.OptionsSpecContext optionsSpecContext) {

    }

    @Override
    public void enterOption(ANTLRv4Parser.OptionContext optionContext) {

    }

    @Override
    public void exitOption(ANTLRv4Parser.OptionContext optionContext) {

    }

    @Override
    public void enterOptionValue(ANTLRv4Parser.OptionValueContext optionValueContext) {

    }

    @Override
    public void exitOptionValue(ANTLRv4Parser.OptionValueContext optionValueContext) {

    }

    @Override
    public void enterDelegateGrammars(ANTLRv4Parser.DelegateGrammarsContext delegateGrammarsContext) {

    }

    @Override
    public void exitDelegateGrammars(ANTLRv4Parser.DelegateGrammarsContext delegateGrammarsContext) {

    }

    @Override
    public void enterDelegateGrammar(ANTLRv4Parser.DelegateGrammarContext delegateGrammarContext) {

    }

    @Override
    public void exitDelegateGrammar(ANTLRv4Parser.DelegateGrammarContext delegateGrammarContext) {

    }

    @Override
    public void enterTokensSpec(ANTLRv4Parser.TokensSpecContext tokensSpecContext) {

    }

    @Override
    public void exitTokensSpec(ANTLRv4Parser.TokensSpecContext tokensSpecContext) {

    }

    @Override
    public void enterChannelsSpec(ANTLRv4Parser.ChannelsSpecContext channelsSpecContext) {

    }

    @Override
    public void exitChannelsSpec(ANTLRv4Parser.ChannelsSpecContext channelsSpecContext) {

    }

    @Override
    public void enterIdList(ANTLRv4Parser.IdListContext idListContext) {

    }

    @Override
    public void exitIdList(ANTLRv4Parser.IdListContext idListContext) {

    }

    @Override
    public void enterAction_(ANTLRv4Parser.Action_Context actionContext) {

    }

    @Override
    public void exitAction_(ANTLRv4Parser.Action_Context actionContext) {

    }

    @Override
    public void enterActionScopeName(ANTLRv4Parser.ActionScopeNameContext actionScopeNameContext) {

    }

    @Override
    public void exitActionScopeName(ANTLRv4Parser.ActionScopeNameContext actionScopeNameContext) {

    }

    @Override
    public void enterActionBlock(ANTLRv4Parser.ActionBlockContext actionBlockContext) {

    }

    @Override
    public void exitActionBlock(ANTLRv4Parser.ActionBlockContext actionBlockContext) {

    }

    @Override
    public void enterArgActionBlock(ANTLRv4Parser.ArgActionBlockContext argActionBlockContext) {

    }

    @Override
    public void exitArgActionBlock(ANTLRv4Parser.ArgActionBlockContext argActionBlockContext) {

    }

    @Override
    public void enterModeSpec(ANTLRv4Parser.ModeSpecContext modeSpecContext) {

    }

    @Override
    public void exitModeSpec(ANTLRv4Parser.ModeSpecContext modeSpecContext) {

    }

    @Override
    public void enterRules(ANTLRv4Parser.RulesContext rulesContext) {

    }

    @Override
    public void exitRules(ANTLRv4Parser.RulesContext rulesContext) {

    }

    @Override
    public void enterRuleSpec(ANTLRv4Parser.RuleSpecContext ruleSpecContext) {

    }

    @Override
    public void exitRuleSpec(ANTLRv4Parser.RuleSpecContext ruleSpecContext) {

    }

    @Override
    public void enterParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext parserRuleSpecContext) {

    }

    @Override
    public void exitParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext parserRuleSpecContext) {

    }

    @Override
    public void enterExceptionGroup(ANTLRv4Parser.ExceptionGroupContext exceptionGroupContext) {

    }

    @Override
    public void exitExceptionGroup(ANTLRv4Parser.ExceptionGroupContext exceptionGroupContext) {

    }

    @Override
    public void enterExceptionHandler(ANTLRv4Parser.ExceptionHandlerContext exceptionHandlerContext) {

    }

    @Override
    public void exitExceptionHandler(ANTLRv4Parser.ExceptionHandlerContext exceptionHandlerContext) {

    }

    @Override
    public void enterFinallyClause(ANTLRv4Parser.FinallyClauseContext finallyClauseContext) {

    }

    @Override
    public void exitFinallyClause(ANTLRv4Parser.FinallyClauseContext finallyClauseContext) {

    }

    @Override
    public void enterRulePrequel(ANTLRv4Parser.RulePrequelContext rulePrequelContext) {

    }

    @Override
    public void exitRulePrequel(ANTLRv4Parser.RulePrequelContext rulePrequelContext) {

    }

    @Override
    public void enterRuleReturns(ANTLRv4Parser.RuleReturnsContext ruleReturnsContext) {

    }

    @Override
    public void exitRuleReturns(ANTLRv4Parser.RuleReturnsContext ruleReturnsContext) {

    }

    @Override
    public void enterThrowsSpec(ANTLRv4Parser.ThrowsSpecContext throwsSpecContext) {

    }

    @Override
    public void exitThrowsSpec(ANTLRv4Parser.ThrowsSpecContext throwsSpecContext) {

    }

    @Override
    public void enterLocalsSpec(ANTLRv4Parser.LocalsSpecContext localsSpecContext) {

    }

    @Override
    public void exitLocalsSpec(ANTLRv4Parser.LocalsSpecContext localsSpecContext) {

    }

    @Override
    public void enterRuleAction(ANTLRv4Parser.RuleActionContext ruleActionContext) {

    }

    @Override
    public void exitRuleAction(ANTLRv4Parser.RuleActionContext ruleActionContext) {

    }

    @Override
    public void enterRuleModifiers(ANTLRv4Parser.RuleModifiersContext ruleModifiersContext) {

    }

    @Override
    public void exitRuleModifiers(ANTLRv4Parser.RuleModifiersContext ruleModifiersContext) {

    }

    @Override
    public void enterRuleModifier(ANTLRv4Parser.RuleModifierContext ruleModifierContext) {

    }

    @Override
    public void exitRuleModifier(ANTLRv4Parser.RuleModifierContext ruleModifierContext) {

    }

    @Override
    public void enterRuleBlock(ANTLRv4Parser.RuleBlockContext ruleBlockContext) {

    }

    @Override
    public void exitRuleBlock(ANTLRv4Parser.RuleBlockContext ruleBlockContext) {

    }

    @Override
    public void enterRuleAltList(ANTLRv4Parser.RuleAltListContext ruleAltListContext) {

    }

    @Override
    public void exitRuleAltList(ANTLRv4Parser.RuleAltListContext ruleAltListContext) {

    }

    @Override
    public void enterLabeledAlt(ANTLRv4Parser.LabeledAltContext labeledAltContext) {

    }

    @Override
    public void exitLabeledAlt(ANTLRv4Parser.LabeledAltContext labeledAltContext) {

    }

    @Override
    public void enterLexerRuleSpec(ANTLRv4Parser.LexerRuleSpecContext lexerRuleSpecContext) {

    }

    @Override
    public void exitLexerRuleSpec(ANTLRv4Parser.LexerRuleSpecContext lexerRuleSpecContext) {

    }

    @Override
    public void enterLexerRuleBlock(ANTLRv4Parser.LexerRuleBlockContext lexerRuleBlockContext) {

    }

    @Override
    public void exitLexerRuleBlock(ANTLRv4Parser.LexerRuleBlockContext lexerRuleBlockContext) {

    }

    @Override
    public void enterLexerAltList(ANTLRv4Parser.LexerAltListContext lexerAltListContext) {

    }

    @Override
    public void exitLexerAltList(ANTLRv4Parser.LexerAltListContext lexerAltListContext) {

    }

    @Override
    public void enterLexerAlt(ANTLRv4Parser.LexerAltContext lexerAltContext) {

    }

    @Override
    public void exitLexerAlt(ANTLRv4Parser.LexerAltContext lexerAltContext) {

    }

    @Override
    public void enterLexerElements(ANTLRv4Parser.LexerElementsContext lexerElementsContext) {

    }

    @Override
    public void exitLexerElements(ANTLRv4Parser.LexerElementsContext lexerElementsContext) {

    }

    @Override
    public void enterLexerElement(ANTLRv4Parser.LexerElementContext lexerElementContext) {

    }

    @Override
    public void exitLexerElement(ANTLRv4Parser.LexerElementContext lexerElementContext) {

    }

    @Override
    public void enterLexerBlock(ANTLRv4Parser.LexerBlockContext lexerBlockContext) {

    }

    @Override
    public void exitLexerBlock(ANTLRv4Parser.LexerBlockContext lexerBlockContext) {

    }

    @Override
    public void enterLexerCommands(ANTLRv4Parser.LexerCommandsContext lexerCommandsContext) {

    }

    @Override
    public void exitLexerCommands(ANTLRv4Parser.LexerCommandsContext lexerCommandsContext) {

    }

    @Override
    public void enterLexerCommand(ANTLRv4Parser.LexerCommandContext lexerCommandContext) {

    }

    @Override
    public void exitLexerCommand(ANTLRv4Parser.LexerCommandContext lexerCommandContext) {

    }

    @Override
    public void enterLexerCommandName(ANTLRv4Parser.LexerCommandNameContext lexerCommandNameContext) {

    }

    @Override
    public void exitLexerCommandName(ANTLRv4Parser.LexerCommandNameContext lexerCommandNameContext) {

    }

    @Override
    public void enterLexerCommandExpr(ANTLRv4Parser.LexerCommandExprContext lexerCommandExprContext) {

    }

    @Override
    public void exitLexerCommandExpr(ANTLRv4Parser.LexerCommandExprContext lexerCommandExprContext) {

    }

    @Override
    public void enterAltList(ANTLRv4Parser.AltListContext altListContext) {

    }

    @Override
    public void exitAltList(ANTLRv4Parser.AltListContext altListContext) {

    }

    @Override
    public void enterAlternative(ANTLRv4Parser.AlternativeContext alternativeContext) {

    }

    @Override
    public void exitAlternative(ANTLRv4Parser.AlternativeContext alternativeContext) {

    }

    @Override
    public void enterElement(ANTLRv4Parser.ElementContext elementContext) {

    }

    @Override
    public void exitElement(ANTLRv4Parser.ElementContext elementContext) {

    }

    @Override
    public void enterPredicateOptions(ANTLRv4Parser.PredicateOptionsContext predicateOptionsContext) {

    }

    @Override
    public void exitPredicateOptions(ANTLRv4Parser.PredicateOptionsContext predicateOptionsContext) {

    }

    @Override
    public void enterPredicateOption(ANTLRv4Parser.PredicateOptionContext predicateOptionContext) {

    }

    @Override
    public void exitPredicateOption(ANTLRv4Parser.PredicateOptionContext predicateOptionContext) {

    }

    @Override
    public void enterLabeledElement(ANTLRv4Parser.LabeledElementContext labeledElementContext) {

    }

    @Override
    public void exitLabeledElement(ANTLRv4Parser.LabeledElementContext labeledElementContext) {

    }

    @Override
    public void enterEbnf(ANTLRv4Parser.EbnfContext ebnfContext) {

    }

    @Override
    public void exitEbnf(ANTLRv4Parser.EbnfContext ebnfContext) {

    }

    @Override
    public void enterBlockSuffix(ANTLRv4Parser.BlockSuffixContext blockSuffixContext) {

    }

    @Override
    public void exitBlockSuffix(ANTLRv4Parser.BlockSuffixContext blockSuffixContext) {

    }

    @Override
    public void enterEbnfSuffix(ANTLRv4Parser.EbnfSuffixContext ebnfSuffixContext) {

    }

    @Override
    public void exitEbnfSuffix(ANTLRv4Parser.EbnfSuffixContext ebnfSuffixContext) {

    }

    @Override
    public void enterLexerAtom(ANTLRv4Parser.LexerAtomContext lexerAtomContext) {

    }

    @Override
    public void exitLexerAtom(ANTLRv4Parser.LexerAtomContext lexerAtomContext) {

    }

    @Override
    public void enterAtom(ANTLRv4Parser.AtomContext atomContext) {

    }

    @Override
    public void exitAtom(ANTLRv4Parser.AtomContext atomContext) {

    }

    @Override
    public void enterNotSet(ANTLRv4Parser.NotSetContext notSetContext) {

    }

    @Override
    public void exitNotSet(ANTLRv4Parser.NotSetContext notSetContext) {

    }

    @Override
    public void enterBlockSet(ANTLRv4Parser.BlockSetContext blockSetContext) {

    }

    @Override
    public void exitBlockSet(ANTLRv4Parser.BlockSetContext blockSetContext) {

    }

    @Override
    public void enterSetElement(ANTLRv4Parser.SetElementContext setElementContext) {

    }

    @Override
    public void exitSetElement(ANTLRv4Parser.SetElementContext setElementContext) {

    }

    @Override
    public void enterBlock(ANTLRv4Parser.BlockContext blockContext) {

    }

    @Override
    public void exitBlock(ANTLRv4Parser.BlockContext blockContext) {

    }

    @Override
    public void enterRuleref(ANTLRv4Parser.RulerefContext rulerefContext) {

    }

    @Override
    public void exitRuleref(ANTLRv4Parser.RulerefContext rulerefContext) {

    }

    @Override
    public void enterCharacterRange(ANTLRv4Parser.CharacterRangeContext characterRangeContext) {

    }

    @Override
    public void exitCharacterRange(ANTLRv4Parser.CharacterRangeContext characterRangeContext) {

    }

    @Override
    public void enterTerminalDef(ANTLRv4Parser.TerminalDefContext terminalDefContext) {

    }

    @Override
    public void exitTerminalDef(ANTLRv4Parser.TerminalDefContext terminalDefContext) {

    }

    @Override
    public void enterElementOptions(ANTLRv4Parser.ElementOptionsContext elementOptionsContext) {

    }

    @Override
    public void exitElementOptions(ANTLRv4Parser.ElementOptionsContext elementOptionsContext) {

    }

    @Override
    public void enterElementOption(ANTLRv4Parser.ElementOptionContext elementOptionContext) {

    }

    @Override
    public void exitElementOption(ANTLRv4Parser.ElementOptionContext elementOptionContext) {

    }

    @Override
    public void enterIdentifier(ANTLRv4Parser.IdentifierContext identifierContext) {

    }

    @Override
    public void exitIdentifier(ANTLRv4Parser.IdentifierContext identifierContext) {

    }

    @Override
    public void visitTerminal(TerminalNode node) {

    }

    @Override
    public void visitErrorNode(ErrorNode node) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {

    }
}
