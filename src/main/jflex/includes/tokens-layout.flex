// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state LAYOUT

// BLOCK 2 - END
%%


<LAYOUT> {

    {WHITESPACE} {  }

    {LAYOUT} {
            enter(NEW_FILE);
            return LexerTokens.LAYOUT_KEY;
        }

    [/]{LAYOUT}  {
            leave();
            return LexerTokens.LAYOUT_CLOSE_KEY;
        }

}
