// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state OBJECT
%state OBJECT_STRING

// BLOCK 2 - END
%%

<OBJECT> {

    {WHITESPACE} {  }

    \{ {
        objectDepth++;
        return LexerTokens.BRACE;
    }

    "|>" {
            yypushback(yylength());
            leave();
    }

    \} {
        objectDepth--;
        if (objectDepth == 0) { leave(); }
        return LexerTokens.BRACE;
    }

    \" {
        yypushback(yylength());
        enter(STRING);
    }

    "," { return LexerTokens.COMMA; }

    {BOOLEAN} { return LexerTokens.BOOLEAN; }

    {SYMBOL} { return LexerTokens.SYMBOL; }

    ":" { return LexerTokens.COLON; }

    {NUMBER} { return LexerTokens.NUMBER; }




}



