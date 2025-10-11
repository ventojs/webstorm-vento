// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
// BLOCK 1 - END
%%
// BLOCK 2 - START

%state EXPRESSION
%state EXP_BRACKET
%state EXP_JSON_STRING
%state EXP_OBJECT
%state EXP_REGEX
%state EXP_STR_BK_TICK
%state EXP_STR_DBL_QOUTE
%state EXP_STR_SNGL_QUOTE

// BLOCK 2 - END
%%


<EXPRESSION> {

   //strings
   [\"][^\"\n\r]*[\"] { return VentoLexerTypes.STRING; }

   [\'][^\'\n\r]*[\'] { return VentoLexerTypes.STRING; }

   [\`][^\`\n\r]*[\`] { return VentoLexerTypes.STRING; }

   \/([^\\/\[]|\\.|(\[([^\]\\]|\\.)*\]))*\/ { return VentoLexerTypes.REGEX; }

   [.]  { return VentoLexerTypes.DOT; }

   [()\[\]]  { return VentoLexerTypes.BRACKET; }

   \{ {
        objectDepth=1;
        yybegin(EXP_OBJECT);
        return VentoLexerTypes.EXPRESSION;
   }


   "}}" {
        yypushback(yylength());
        leave();
   }

   "|>" {
        enter(EXPRESSION);
        return VentoLexerTypes.PIPE_ELEMENT;
   }

   [^\/\"'`(){} \t\n\r]+ { return VentoLexerTypes.EXPRESSION; }
   [ \t]+ { }

   <<EOF>> { leave(); }

   [^] { leave(); }
}

<EXP_OBJECT> {

    \{ {
        objectDepth++;
        return VentoLexerTypes.EXPRESSION;
    }

    [\"][^\"\n\r]*[\"] { return VentoLexerTypes.STRING; }

    [^}{\"]+ {return VentoLexerTypes.EXPRESSION;}

    \} {
        objectDepth--;
        if (objectDepth == 0) {
         yybegin(EXPRESSION);
        }
        return VentoLexerTypes.EXPRESSION;
    }

    <<EOF>> {
        leave();
        return VentoLexerTypes.UNKNOWN;
    }

}



