// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state EXPORT

// BLOCK 2 - END
%%

<EXPORT> {
    {WHITESPACE}   { }
    {FUNCTION} { enter(FUNCTION); return LexerTokens.FUNCTION_KEY; }
    {SYMBOL} { return LexerTokens.SYMBOL; }
    {EQUAL} { enter(EXPRESSION); return LexerTokens.EQUAL; }
    {PIPE} { enter(EXPRESSION); return LexerTokens.PIPE;}
    [^}=|] {  pushbackall(); enter(EXPRESSION); }

}



