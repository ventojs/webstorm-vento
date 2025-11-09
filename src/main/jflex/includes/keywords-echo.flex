// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state ECHO

// BLOCK 2 - END
%%

<ECHO> {

    {WHITESPACE} {  }

    \"|\'|\` {
          pushbackall();
          enter(STRING);
      }

    "|>" {
          enter(EXPRESSION);
          return LexerTokens.PIPE;
      }

}


