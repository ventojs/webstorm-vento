// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state PIPE


FUNC_PARAM = "(".*")"

REGEX = [\!]?\/.+\/({OWS}\.{IDENT}{OWS}{FUNC_PARAM})*

// BLOCK 2 - END
%%


<PIPE> {

    {WHITESPACE} {  }

    {PIPE} / .*{CBLOCK} { return LexerTypes.PIPE_ELEMENT; }

    {IDENT} { return LexerTypes.VARIABLE_ELEMENT; }

    {FUNC_PARAM} { return LexerTypes.VARIABLE_ELEMENT; }

    \. { return LexerTypes.VARIABLE_ELEMENT; }

    {REGEX} { return LexerTypes.VARIABLE_ELEMENT; }

    {STRING} { return LexerTypes.STRING; }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] { return LexerTypes.UNKNOWN; }
}


