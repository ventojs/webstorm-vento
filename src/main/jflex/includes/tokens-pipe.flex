// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state PIPE


// BLOCK 2 - END
%%


<PIPE> {

    {WHITESPACE} {  }

    {PIPE} / .*{CBLOCK} {
                enter(EXPRESSION);
                return LexerTokens.PIPE;
            }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }
}


