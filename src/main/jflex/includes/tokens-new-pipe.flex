// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state NEW_PIPE


// BLOCK 2 - END
%%


<NEW_PIPE> {

    {WHITESPACE} {  }

    {PIPE} / .*{CBLOCK} { return LexerTypes.PIPE_ELEMENT; }

    [^|][^>]+{OWS}"}}" {
        yypushback(yylength());
        System.out.println("EXPR: " +yytext());
        enter(EXPRESSION);
    }

    {CBLOCK} {
        yypushback(yylength());
        leave();
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] { return LexerTypes.UNKNOWN; }
}


