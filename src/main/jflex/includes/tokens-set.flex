// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
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

    {SET} / .+"=" {
        enter(SET_VALUE);
        return LexerTypes.SET_KEY;
    }

    {SET} / {WHITESPACE}[^=]+{OWS}{CBLOCK} {
        enter(SET_BLOCK_MODE);
        yypushback(yylength()-3);
        return LexerTypes.SET_KEY;
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
        return LexerTypes.ERROR;
    }

    [^] {
        yypushback(yylength());
        leave();
    }
}

<SET_VALUE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTypes.IDENTIFIER; }

    "=" {
        enter(EXPRESSION);
        return LexerTypes.EQUAL;
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
        return LexerTypes.ERROR;
    }

    [^] {
        return LexerTypes.UNKNOWN;
    }

}

<SET_BLOCK_MODE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTypes.IDENTIFIER; }

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
        System.out.println("3");
        yypushback(yylength());
        leave();
    }

}


