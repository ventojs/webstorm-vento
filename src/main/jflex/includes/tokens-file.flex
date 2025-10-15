// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state NEW_FILE



// BLOCK 2 - END
%%



<NEW_FILE> {

    {WHITESPACE} {  }

    [\"][.]?[/]?.*[\"] {
            leave();
            return LexerTokens.FILE;
        }


}


