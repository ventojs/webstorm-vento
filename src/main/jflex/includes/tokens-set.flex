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

    [/]{SET} / {OWS}{CBLOCK} { return LexerTokens.SET_CLOSE_KEY; }

    {SET} / .+"=" {
            enter(SET_VALUE);
            return LexerTokens.SET_KEY;
        }

    {SET} / {WHITESPACE}[^=]+{OWS}{CBLOCK} {
            enter(SET_BLOCK_MODE);
            yypushback(yylength()-3);
            return LexerTokens.SET_KEY;
        }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }

    {OBLOCK} {
        yypushback(yylength());
        leave();
    }

    <<EOF>> {
            leave();
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yypushback(yylength());
        leave();
    }
}

<SET_VALUE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTokens.IDENTIFIER; }

    "=" {
            enter(EXPRESSION);
            return LexerTokens.EQUAL;
        }

    {PIPE} {
        yypushback(yylength());
        enter(NEW_PIPE);
    }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }

    <<EOF>> {
            leave();
            return LexerTokens.UNKNOWN;
        }

    [^] {
            return LexerTokens.UNKNOWN;
        }

}

<SET_BLOCK_MODE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTokens.IDENTIFIER; }

    {PIPE} {
        yypushback(yylength());
        enter(NEW_PIPE);
    }

    {CBLOCK} {
        leave();
        yypushback(yylength());
    }

    <<EOF>> {
        yypushback(yylength());
        leave();
    }

    [^|}] {
        yypushback(yylength());
        leave();
    }

}


