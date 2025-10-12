// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
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
    {WHITESPACE}   {  }

    {EXPORT} / {WHITESPACE} {
        yybegin(EXPORT_VALUE);
        return LexerTypes.EXPORT_KEY;
    }

    {EXPORT}{WHITESPACE}.*{OBLOCK}{OWS}[/]{EXPORT}{OWS}{CBLOCK} {
        yybegin(EXPORT_BLOCK_MODE);
        yypushback(yylength()-6);
        return LexerTypes.EXPORT_KEY;
    }


    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yypushback(yylength());
        yybegin(BLOCK);
    }
}

<EXPORT_VALUE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTypes.EXPORT_VAR; }
    "=" {
        enter(EXPRESSION);
        return LexerTypes.EXPORT_EQ;
    }

    {PIPE} {
        yypushback(yylength());
        enter(PIPE);
    }

    {CBLOCK} {
        yybegin(BLOCK);
        yypushback(yylength());
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        return LexerTypes.UNKNOWN;
    }

}

<EXPORT_BLOCK_MODE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTypes.EXPORT_VAR; }

    {CBLOCK} {
        yybegin(BLOCK);
        yypushback(yylength());
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] { return LexerTypes.UNKNOWN; }

}

<EXPORT_CLOSE> {
    {WHITESPACE} {  }

    [/]{EXPORT} / {OWS}{CBLOCK} {
        yybegin(BLOCK);
        return LexerTypes.EXPORT_CLOSE_KEY;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yypushback(yylength());
        yybegin(BLOCK);
    }
}

<EXPORT_FUNCTION_BLOCK> {

    {WHITESPACE} { }

    {EXPORT} { return LexerTypes.EXPORT_KEY; }

    {FUNCTION} { return LexerTypes.EXPORT_FUNCTION_KEY; }

    {IDENT} { return LexerTypes.EXPORT_VAR; }

    "("{IDENT}?([,]{IDENT})*")" { return LexerTypes.EXPORT_FUNCTION_ARGS; }

    {PIPE} {
        yypushback(yylength());
        enter(PIPE);
    }

    {CBLOCK} {
        yypushback(yylength());
        yybegin(BLOCK);
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] { return LexerTypes.UNKNOWN; }

}


