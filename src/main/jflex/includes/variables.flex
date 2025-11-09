// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state VARIABLE
%state SCRIPT_CONTENT

// BLOCK 2 - END
%%

<VARIABLE> {
    \{ { pushbackall(); enter(EXPRESSION);}
    [^}] { pushbackall(); enter(EXPRESSION);}
    "}}" { pushbackall(); leave();}
    "}"{OWS}"}"{OWS}"}" { yypushback(2); leave();return LexerTokens.UNKNOWN;}
    "{{" { pushbackall();leave(); }

}

<SCRIPT_CONTENT> {
    {WHITESPACE} { }

    \{ {
          incObjDepth();
          return LexerTokens.JAVASCRIPT;
      }

    \} {
          decObjDepth();
          return LexerTokens.JAVASCRIPT;
      }

    {CBLOCK} {
          if (currentDepth().getFirst() == 0) {
              leave();
              return LexerTokens.JSBLOCK_CLOSE;
          } else {
              yypushback(1);
              decObjDepth();
              return LexerTokens.JAVASCRIPT;
          }
      }

    [^}{]+ { return LexerTokens.JAVASCRIPT; }

}
