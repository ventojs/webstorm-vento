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
    {WHITESPACE}   {  }

    {EXPORT} / .+"=" {
            yybegin(EXPORT_VALUE);
            return LexerTokens.EXPORT_KEY;
        }

    {EXPORT} / {WHITESPACE}[^=]+{OWS}{CBLOCK} {
            yybegin(EXPORT_BLOCK_MODE);
            yypushback(yylength()-6);
            return LexerTokens.EXPORT_KEY;
        }


    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yypushback(yylength());
        yybegin(BLOCK);
    }
}

<EXPORT_VALUE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTokens.EXPORT_VAR; }

    "=" {
            enter(EXPRESSION);
            return LexerTokens.EQUAL;
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
            return LexerTokens.UNKNOWN;
        }

    [^] {
            return LexerTokens.UNKNOWN;
        }

}

<EXPORT_BLOCK_MODE> {
    {WHITESPACE}   {  }

    {IDENT} { return LexerTokens.EXPORT_VAR; }

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
            return LexerTokens.UNKNOWN;
        }

    [^] { return LexerTokens.UNKNOWN; }

}

<EXPORT_CLOSE> {
    {WHITESPACE} {  }

    [/]{EXPORT} / {OWS}{CBLOCK} {
            yybegin(BLOCK);
            return LexerTokens.EXPORT_CLOSE_KEY;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yypushback(yylength());
        yybegin(BLOCK);
    }
}

<EXPORT_FUNCTION_BLOCK> {

    {WHITESPACE} { }

    {EXPORT} { return LexerTokens.EXPORT_KEY; }

    {FUNCTION} { return LexerTokens.FUNCTION_KEY; }

    {IDENT} { return LexerTokens.EXPORT_VAR; }

    "("{IDENT}?([,]{IDENT})*")" { return LexerTokens.FUNCTION_ARGS; }

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
            return LexerTokens.UNKNOWN;
        }

    [^] { return LexerTokens.UNKNOWN; }

}


