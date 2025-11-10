// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state IF

// BLOCK 2 - END
%%

<IF> {

    {WHITESPACE} {  }

    "if" {
          enter(EXPRESSION);
          return LexerTokens.IF_KEY;
      }


    "else"{WHITESPACE}"if" {
          enter(EXPRESSION);
          return LexerTokens.ELSEIF_KEY;
      }

    "else" { return LexerTokens.ELSE_KEY; }

    "|>" {
          enter(EXPRESSION);
          return LexerTokens.PIPE;
      }

}


