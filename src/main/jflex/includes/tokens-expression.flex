// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
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
    [ \t]+ { System.out.println("SPACE"); }
   //strings
   \"([^\"\\]|\\.)*\" {
          System.out.println("STRING");
          return LexerTypes.STRING; }

   \'([^\'\\]|\\.)*\' { return LexerTypes.STRING; }

   \`([^\`\\]|\\.)*\` { return LexerTypes.STRING; }

   \/([^\\/\[]|\\.|(\[([^\]\\]|\\.)*\]))*\/ { return LexerTypes.REGEX; }

   [.]  { return LexerTypes.DOT; }

   [()\[\]]  { return LexerTypes.BRACKET; }

   \{ {
        objectDepth=1;
        yybegin(EXP_OBJECT);
        return LexerTypes.EXPRESSION;
   }

   "}}" {
          System.out.println("CLOSE");
        yypushback(yylength());
        leave();
   }

   "|>" {
          System.out.println("PIPE");
        enter(EXPRESSION);
        return LexerTypes.PIPE_ELEMENT;
   }

   [^\/\"'`(){} \t\n\r]+ {
          System.out.println("EXPR");
          return LexerTypes.EXPRESSION; }


   <<EOF>> {
          System.out.println("UNKNOWN STATE");
        leave();
        return LexerTypes.UNKNOWN;
   }

   [^] {
          System.out.println("UNKNOWN STATE");
        leave();
        return LexerTypes.UNKNOWN;
   }
}

<EXP_OBJECT> {

    \{ {
        objectDepth++;
        return LexerTypes.EXPRESSION;
    }

    [\"][^\"\n\r]*[\"] { return LexerTypes.STRING; }

    [^}{\"]+ {return LexerTypes.EXPRESSION;}

    \} {
        objectDepth--;
        if (objectDepth == 0) {
            yybegin(EXPRESSION);
        }
        return LexerTypes.EXPRESSION;
    }

    <<EOF>> {
         leave();
         return LexerTypes.UNKNOWN;
    }

}



