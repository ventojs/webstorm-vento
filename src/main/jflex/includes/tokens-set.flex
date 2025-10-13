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

    "set" /{OWS}{IDENT}{OWS}"=" {
        return  LexerTypes.SET_KEY;
    }

    {IDENT} {
          return LexerTypes.IDENTIFIER;
      }

    "=" {
        enter(EXPRESSION);
        return LexerTypes.EQUAL;
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


