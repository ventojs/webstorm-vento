// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state SET
%state SET_VALUE
%state SET_BLOCK_MODE

// BLOCK 2 - END
%%

<SET> {
    {WHITESPACE}   {  }

    [/]{SET} { leave(); return LexerTokens.SET_CLOSE_KEY; }

    .+"=" {
            yypushback(yylength());
            enter(SET_VALUE);
        }

    {WHITESPACE}[^=]+{OWS}{CBLOCK} {
            yypushback(yylength());
            enter(SET_BLOCK_MODE);
        }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }

    {OBLOCK} {
        yypushback(yylength());
        leave();
    }

}

<SET_VALUE> {
    {WHITESPACE}   {  }

    {SYMBOL} { return LexerTokens.SYMBOL; }

    "="/{OWS}[(]?{OWS}"function" {
           enter(FUNCTION);
           return LexerTokens.EQUAL;
       }

    "=" {
            enter(EXPRESSION);
            return LexerTokens.EQUAL;
        }

    {PIPE} {
        enter(EXPRESSION);
        return LexerTokens.PIPE;
    }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }


}


<SET_BLOCK_MODE> {
    {WHITESPACE}   {  }

    {SYMBOL} { return LexerTokens.SYMBOL; }

    {PIPE} {
        enter(EXPRESSION);
        return LexerTokens.PIPE;
    }

    {CBLOCK} {
        leave();
        yypushback(yylength());
    }


}


