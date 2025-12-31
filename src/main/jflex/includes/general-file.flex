// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FILE

// BLOCK 2 - END
%%

<FILE> {

    {WHITESPACE} {  }

    [\"][.]?[/]?.*[\"] / {WHITESPACE} | {OWS}"}}" {
          leave();
          return LexerTokens.FILE;
      }

    [\"][.]?[/]?[^\"]*"}}" {
          yypushback(2);
          leave();
          return LexerTokens.UNKNOWN;
      }

}


