// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state EXPORT
%state EXPORT_CLOSE
%state EXPORT_VALUE
%state EXPORT_BLOCK_MODE
%state EXPORT_FUNCTION_BLOCK


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



