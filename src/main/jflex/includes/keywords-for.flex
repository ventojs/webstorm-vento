// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FOR
%state BEFORE_OF

FOR_KEY = "for"
OF = "of"

// BLOCK 2 - END
%%


<FOR> {

    {WHITESPACE} {  }

    {OF} {
         enter(EXPRESSION);
         return LexerTokens.FOR_OF;
    }

    {SYMBOL} {
          pushbackall();
          enter(BEFORE_OF);
      }

    \[|\{ {
        pushbackall();
        enter(BEFORE_OF);
      }


    {PIPE} {
          enter(EXPRESSION);
          return LexerTokens.PIPE;
      }

}

<BEFORE_OF> {
    {WHITESPACE} {  }

    {OF} { pushbackall();leave();}

    , {return LexerTokens.COMMA;}

    \{ { pushbackall(); enter(EXPRESSION); }

    \[ { pushbackall(); enter(EXPRESSION); }

    {SYMBOL} { return LexerTokens.SYMBOL; }
}





