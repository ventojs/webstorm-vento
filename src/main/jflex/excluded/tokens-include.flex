// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state INCLUDE

INCLUDE = "include"

// BLOCK 2 - END
%%


<INCLUDE> {

    {WHITESPACE} {  }

    {INCLUDE} {
            enter(EXPRESSION);
            return LexerTokens.INCLUDE_KEY;
        }

    \{ {
            yypushback(yylength());
            enter(OBJECT);
        }

    {PIPE} {
            yypushback(yylength());
            enter(PIPE);
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

