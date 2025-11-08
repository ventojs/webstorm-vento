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

    {WHITESPACE} { }

    \[ {
          incArrDepth();
          return LexerTokens.BRACKET;
      }

    , { return LexerTokens.COMMA;}

    \] {
          decArrDepth();
          if (currentDepth().getSecond() <= 0) { leave(); }
          return LexerTokens.BRACKET;
      }

    [^\[\],] {
          pushbackall();
          enter(EXPRESSION);
      }

}


