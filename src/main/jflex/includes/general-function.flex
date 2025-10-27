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
    \{ { yypushback(yylength());enter(FUNCTION_BODY);}
    {CBLOCK} | {PIPE}  {
        yypushback(yylength());
        leave();
    }
}


/*<FUNCTION> {
    "function" { return LexerTokens.FUNCTION_KEY;}

    \( {
        enter(FUNCTION_ARGS);
        return LexerTokens.PARENTHESIS;
    }

    \{ {
        enter(FUNCTION_BODY);
        objectDepth=0;
        return LexerTokens.BRACE;
    }


}

<FUNCTION_ARGS> {

    [^,] {yypushback(yylength());enter(EXPRESSION);}
    , { return LexerTokens.COMMA;}
    \) {
        leave();
        return LexerTokens.PARENTHESIS;
        }
}
*/



<FUNCTION_BODY>{

       {WHITESPACE} { }

       \{ {
             objectDepth++;
             return LexerTokens.BRACE;
       }


       [^}{;\n\r] {return LexerTokens.STATEMENT;}

       ; { return LexerTokens.SEMICOLON; }

       \} {
            objectDepth--;
            if(objectDepth==0){ leave();}
            return LexerTokens.BRACE;
          }

}

