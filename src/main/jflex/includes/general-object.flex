// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state OBJECT
%state JS_OBJECT
%state OBJECT_STRING

// BLOCK 2 - END
%%

<OBJECT> {

    {WHITESPACE} {  }

    "|>" { pushbackall(); leave(); }
    [,] { return LexerTokens.COMMA; }
    [:]  { enter(EXPRESSION); return LexerTokens.COLON; }
    [\"'`] { pushbackall(); enter(STRING); }
    [+] {return LexerTokens.PLUS;}

    \{ {
            incObjDepth();
            return LexerTokens.BRACE;
        }

    \} {
            decObjDepth();
            if (currentDepth().getFirst() <= 0) { leave(); }
            return LexerTokens.BRACE;
       }

    [^{}'\",: \t]+ / {OWS}[,}]  { return LexerTokens.SYMBOL; }

    [^{}'\",: \t]+ / {OWS}":"  { return LexerTokens.SYMBOL; }

    "{{" {
        yypushback(yylength());
        leave();
    }


    [^] {
        leave();
        return LexerTokens.UNKNOWN;
    }

    <<EOF>> { leave(); }


}
