// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state OBJECT
%state OBJECT_STRING

// BLOCK 2 - END
%%

<OBJECT> {

    \{ {
            objectDepth = 0;
            objectDepth++;
            return LexerTokens.OBJECT;
        }

    [^}{\"]+ { return LexerTokens.OBJECT; }

    \} {
            objectDepth--;
            if (objectDepth == 0) { leave(); }
            return LexerTokens.OBJECT;
        }

    \" {
          yypushback(yylength());
          enter(STRING);
      }

}

<OBJECT_STRING> {

    [^]+ / \"{WHITESPACE} { return LexerTokens.OBJECT; }

    \" / {WHITESPACE} {
            leave();
            return LexerTokens.OBJECT;
        }

}


<OBJECT,OBJECT_STRING> {

    <<EOF>> {
            leave();
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yypushback(yylength());
        leave();
    }
}


