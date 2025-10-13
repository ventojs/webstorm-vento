// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state SET

// BLOCK 2 - END
%%

<SET> {

    {WHITESPACE}   {  }

    \/"set" { return LexerTypes.SET_CLOSE_KEY; }

    "set" /{OWS}{IDENT}{OWS}"=" { return LexerTypes.SET_KEY; }

    "set" /{OWS}{IDENT}{OWS}"}}" { return  LexerTypes.SET_KEY; }

    "set" /{OWS}{IDENT}{OWS}"|>" { return  LexerTypes.SET_KEY; }

    {IDENT} { return LexerTypes.IDENTIFIER; }

    "=" {
        enter(EXPRESSION);
        return LexerTypes.EQUAL;
    }

    {PIPE} {
        yypushback(yylength());
        enter(NEW_PIPE);
    }

    "}}" {
        yypushback(yylength());
        leave();
    }

    <<EOF>> {
        yypushback(yylength());
        leave();
        return LexerTypes.UNKNOWN;
    }

    [^] {
        yypushback(yylength());
        leave();
        return LexerTypes.UNKNOWN;
    }
}


