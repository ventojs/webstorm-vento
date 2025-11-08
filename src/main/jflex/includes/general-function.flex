// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FUNCTION
%state FUNCTION_ARGS
%state FUNCTION_BODY
// BLOCK 2 - END
%%

<FUNCTION> {

    {WHITESPACE} { }

    \(|\) {return LexerTokens.PARENTHESIS;}

    "function" {return LexerTokens.FUNCTION_KEY;}

    {SYMBOL} { return LexerTokens.SYMBOL; }

    "("{SYMBOL}?([,]{SYMBOL})*")" { return LexerTokens.FUNCTION_ARGS; }

    \{ { pushbackall();enter(FUNCTION_BODY);}

    {CBLOCK} | {PIPE}  {
          yypushback(yylength());
          leave();
      }
}

<FUNCTION_BODY>{

    {WHITESPACE} { }

    \{ {
          incObjDepth();
          return LexerTokens.BRACE;
       }

    [^}{;\n\r] {return LexerTokens.STATEMENT;}

    ; { return LexerTokens.SEMICOLON; }

    \} {
        decObjDepth();
        if(this.strategy.currentDepth().getFirst() ==0){ leave();}
        return LexerTokens.BRACE;
      }

}

