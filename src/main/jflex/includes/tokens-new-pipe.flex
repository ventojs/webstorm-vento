// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state NEW_PIPE


// BLOCK 2 - END
%%


<NEW_PIPE> {

    {WHITESPACE} {  }

    {PIPE} / .*{CBLOCK} {
                enter(EXPRESSION);
                return LexerTokens.PIPE;
            }

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


