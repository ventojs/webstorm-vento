// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state SET
%state SET_VALUE
%state SET_BLOCK_MODE
%state SET_IIFE
%state SET_DESTRUCTURE_VARS

DESTRUCTURE_ID = [a-zA-Z_$]+[a-zA-Z_$0-9]*([ \t]+as[ \t]+[a-zA-Z_$]+[a-zA-Z_$0-9]*)?

// BLOCK 2 - END
%%

<SET> {

    {WHITESPACE}   {  }

    [/]{SET} { leave(); return LexerTokens.SET_CLOSE_KEY; }

    \{ / [^=]*= { yypushback(yylength()-1); enter(SET_DESTRUCTURE_VARS); return LexerTokens.DESTRUCTURE_BRACE; }

    \[ / [^=]*= { yypushback(yylength()-1); enter(SET_DESTRUCTURE_VARS); return LexerTokens.DESTRUCTURE_BRACKET; }

    [^{\[=}]+ / "="|{OWS}{CBLOCK} {
          pushbackall();
          enter(SET_VALUE);
      }

    {SYMBOL}{OWS}{CBLOCK} | {SYMBOL}{WHITESPACE}{PIPE}{WHITESPACE}.+{OWS}{CBLOCK} {
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


    {SYMBOL} {return LexerTokens.SYMBOL; }


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

<SET_DESTRUCTURE_VARS> {
    {WHITESPACE}   {  }

    {DESTRUCTURE_ID} { return LexerTokens.SYMBOL; }

    \[{SYMBOL}\] {return LexerTokens.DESTRUCTURE_KEY;}

    , { return LexerTokens.COMMA; }

    : { return LexerTokens.COLON; }

    = { enter(EXPRESSION); return LexerTokens.EQUAL; }

    "..." { return  LexerTokens.EXPAND; }

    \{ { enter(SET_DESTRUCTURE_VARS); return  LexerTokens.DESTRUCTURE_BRACE; }

    \[ { enter(SET_DESTRUCTURE_VARS); return  LexerTokens.DESTRUCTURE_BRACKET; }

    \} { leave(); return LexerTokens.DESTRUCTURE_BRACE; }

    \] { leave(); return LexerTokens.DESTRUCTURE_BRACKET; }
}


