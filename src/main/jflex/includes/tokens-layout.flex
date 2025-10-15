// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state LAYOUT
%state SLOT

SLOT = "layout"

// BLOCK 2 - END
%%


<LAYOUT> {

    {WHITESPACE} {  }

    {LAYOUT} {
            enter(FILE);
            return LexerTokens.LAYOUT_KEY;
        }

    [/]{LAYOUT}  {
            leave();
            return LexerTokens.LAYOUT_CLOSE_KEY;
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


<SLOT> {

    {WHITESPACE} {  }

    {SLOT} { return LexerTokens.LAYOUT_SLOT_KEY; }

    {IDENT} { return LexerTokens.IDENTIFIER; }

    [/]{SLOT}  {
            leave();
            return LexerTokens.LAYOUT_SLOT_CLOSE_KEY;
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
