// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state SET
%state SET_VALUE
%state SET_BLOCK_MODE
%state SET_IIFE

// BLOCK 2 - END
%%

<SET> {

    {WHITESPACE}   {  }

    [/]{SET} { leave(); return LexerTokens.SET_CLOSE_KEY; }

    .+"=" {
          pushbackall();
          enter(SET_VALUE);
      }

    {WHITESPACE}[^=]+{OWS}{CBLOCK} {
          pushbackall();
          enter(SET_BLOCK_MODE);
      }

    {CBLOCK} {
        pushbackall();
        leave();
      }

    {OBLOCK} {
        pushbackall();
        leave();
      }

}

<SET_VALUE> {

    {WHITESPACE}   {  }

    "function" {
          pushbackall();
          enter(FUNCTION);
      }

    "("{OWS}")"{OWS}"=>" {
          pushbackall();
          enter(FUNCTION_LAMBDA);
      }

    "="/{OWS}[(]?{OWS}"function" {
           return LexerTokens.EQUAL;
       }

    "="/{OWS}"("{OWS}")"{OWS}"=>" {
          return LexerTokens.EQUAL;
       }

    \(/{OWS}"function" {
          return LexerTokens.PARENTHESIS;
      }

    \){OWS}\({OWS}\) {
           pushbackall();
           enter(SET_IIFE);
      }


    {SYMBOL} { return LexerTokens.SYMBOL; }


    "=" {
          enter(EXPRESSION);
          return LexerTokens.EQUAL;
      }

    {PIPE} {
          enter(EXPRESSION);
          return LexerTokens.PIPE;
     }

    {CBLOCK} {
        pushbackall();
        leave();
    }

}


<SET_BLOCK_MODE> {

    {WHITESPACE}   {  }

    {SYMBOL} { return LexerTokens.SYMBOL; }

    {PIPE} {
          enter(EXPRESSION);
          return LexerTokens.PIPE;
      }

    {CBLOCK} {
          pushbackall();
          leave();
      }


}

<SET_IIFE> {
    {WHITESPACE}   {  }

    \) {return LexerTokens.PARENTHESIS;}

    \( {pushbackall(); enter(FUNCTION_ARGS);}

    [^\)\(] {pushbackall(); leave();}
}


