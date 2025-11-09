// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FUNCTION
%state FUNCTION_ARGS
%state FUNCTION_BODY
%state FUNCTION_LAMBDA

ARG = ({SYMBOL}([=]{OWS}([0-9]+[0-9_]?|[\"'].*[\"']))?)
// BLOCK 2 - END
%%

<FUNCTION> {

    {WHITESPACE} { }

    \( {
          pushbackall();
          enter(FUNCTION_ARGS);
      }

    "async" {return LexerTokens.ASYNC_KEY;}

    "function" {return LexerTokens.FUNCTION_KEY;}

    {SYMBOL} { return LexerTokens.FUNCTION_NAME; }

    \{ {
          pushbackall();
          enter(FUNCTION_BODY);
      }

    \} {
        leave();
        return LexerTokens.BRACE;
    }

    {CBLOCK} | {PIPE}  {
          pushbackall();
          leave();
      }
}

<FUNCTION_LAMBDA> {
    {WHITESPACE} { }

    \( {
          pushbackall();
          enter(FUNCTION_ARGS);
      }

    "=>" {return LexerTokens.LAMBDA_ARROW;}


    \{ {
          pushbackall();
          enter(FUNCTION_BODY);
      }

    \} {
          leave();
          return LexerTokens.BRACE;
      }

    "|>" {
          pushbackall();
          leave();
      }

    [^{}] {
          pushbackall();
          enter(EXPRESSION);
      }
}

<FUNCTION_ARGS> {

    {WHITESPACE} { }

    \( { return LexerTokens.PARENTHESIS; }

    \) { leave(); return LexerTokens.PARENTHESIS; }

    {SYMBOL} { return LexerTokens.FUNCTION_ARG; }

    [=] {
          enter(EXPRESSION);
          return LexerTokens.EQUAL;
      }

    [,] { return LexerTokens.COMMA;}

}

<FUNCTION_BODY>{



    \{ {
          incObjDepth();
          return LexerTokens.BRACE;
       }

    [^}{;] {return LexerTokens.STATEMENT;}

    ; { return LexerTokens.SEMICOLON; }

    \} {
        decObjDepth();
        if(this.strategy.currentDepth().getFirst() == 0){
            pushbackall();
            leave();
        } else {
            return LexerTokens.BRACE;
        }
      }

}

