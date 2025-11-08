// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state LAYOUT
%state SLOT

SLOT = "slot"

// BLOCK 2 - END
%%


<LAYOUT> {

    {WHITESPACE} {  }

    \{ {
          pushbackall();
          enter(EXPRESSION);
      }

    \" { pushbackall(); enter(FILE); }

    {PIPE} { enter(EXPRESSION); return LexerTokens.PIPE; }

}


<SLOT> {

    {WHITESPACE} {  }

    {SYMBOL} { return LexerTokens.SYMBOL; }

    {PIPE} { enter(EXPRESSION); return LexerTokens.PIPE; }


}
