// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state DEFAULT
%state DEFAULT_VALUE
%state DEFAULT_BLOCK_MODE
%state DEFAULT_IIFE

// BLOCK 2 - END
%%

<DEFAULT> {

    {WHITESPACE}   {  }

    [/]{DEFAULT} { leave(); return LexerTokens.DEFAULT_CLOSE_KEY; }

    .+"=" {
          pushbackall();
          enter(DEFAULT_VALUE);
      }

    {WHITESPACE}[^=]+{OWS}{CBLOCK} {
          pushbackall();
          enter(DEFAULT_BLOCK_MODE);
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

<DEFAULT_VALUE> {

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
           enter(DEFAULT_IIFE);
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


<DEFAULT_BLOCK_MODE> {

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

<DEFAULT_IIFE> {
    {WHITESPACE}   {  }

    \) {return LexerTokens.PARENTHESIS;}

    \( {pushbackall(); enter(FUNCTION_ARGS);}

    [^\)\(] {pushbackall(); leave();}
}


