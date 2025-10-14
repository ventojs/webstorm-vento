// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
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

    {PIPE} / .*{CBLOCK} { return LexerTokens.PIPE; }

    {IDENT} { return LexerTokens.VARIABLE_ELEMENT; }

    {FUNC_PARAM} { return LexerTokens.VARIABLE_ELEMENT; }

    \. { return LexerTokens.VARIABLE_ELEMENT; }

    {REGEX} { return LexerTokens.VARIABLE_ELEMENT; }

    {STRING} { return LexerTokens.STRING; }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] { return LexerTokens.UNKNOWN; }
}


