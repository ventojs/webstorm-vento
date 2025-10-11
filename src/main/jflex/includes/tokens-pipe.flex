// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state PIPE

PIPE = "|>"

OWS =[ \t\n\r]*
IDENT = [a-zA-Z_$]+[a-zA-Z_$0-9]*
FUNC_PARAM = "(".*")"
STRING = "\"".*"\""
REGEX = [\!]?\/.+\/({OWS}\.{IDENT}{OWS}{FUNC_PARAM})*

// BLOCK 2 - END
%%


<PIPE> {

    {WHITESPACE}   {  }

    {PIPE} / .*{CBLOCK} { return VentoLexerTypes.PIPE_ELEMENT; }

    {IDENT} { return VentoLexerTypes.VARIABLE_ELEMENT; }
    {FUNC_PARAM} { return VentoLexerTypes.VARIABLE_ELEMENT; }
    \. { return VentoLexerTypes.VARIABLE_ELEMENT; }
    {REGEX} { return VentoLexerTypes.VARIABLE_ELEMENT; }
    {STRING} { return VentoLexerTypes.STRING; }

    {CBLOCK} {
          yypushback(yylength());
          leave();
    }

    <<EOF>> {
              // Unterminated pipe at EOF: reset and consume safely
              yybegin(YYINITIAL);
              return VentoLexerTypes.ERROR;
        }

    [^] { return VentoLexerTypes.UNKNOWN; }
}


