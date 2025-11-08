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
    > { enter(SCRIPT_CONTENT); return LexerTokens.JAVASCRIPT_START; }
    - { enter(EXPRESSION); return LexerTokens.TRIM_OPEN;}
    \{ { pushbackall(); enter(EXPRESSION);}
    [^}] { pushbackall(); enter(EXPRESSION);}
    "}}" { pushbackall(); leave();}
    - / "}}" { leave();return LexerTokens.TRIM_CLOSE;}
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
