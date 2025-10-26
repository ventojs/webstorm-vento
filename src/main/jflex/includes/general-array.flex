// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state ARRAY
%state OBJECT_STRING

// BLOCK 2 - END
%%

<ARRAY> {

    \[ {
            objectDepth++;
            return LexerTokens.BRACKET;
        }

    , { return LexerTokens.COMMA;}

    \] {
            objectDepth--;
            if (objectDepth == 0) { leave(); }
            return LexerTokens.BRACKET;
        }

    "|>" {
        yypushback(yylength());
        leave();
    }

    [^\[\],}] {
          yypushback(yylength());
          enter(EXPRESSION);
      }

}


