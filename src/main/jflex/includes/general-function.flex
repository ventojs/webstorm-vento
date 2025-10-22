// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state FUNCTION

// BLOCK 2 - END
%%

<FUNCTION> {

    {WHITESPACE} { }
    {SYMBOL} { return LexerTokens.SYMBOL; }
    "("{SYMBOL}?([,]{SYMBOL})*")" { return LexerTokens.FUNCTION_ARGS; }
    {CBLOCK}  {
        yypushback(yylength());
        leave();
    }
}


