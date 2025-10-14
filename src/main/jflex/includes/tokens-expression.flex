// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
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

    [ \t]+ { }

   //strings
   \"([^\"\\]|\\.)*\" { return LexerTokens.STRING; }

   \'([^\'\\]|\\.)*\' { return LexerTokens.STRING; }

   \`([^\`\\]|\\.)*\` { return LexerTokens.STRING; }

   // regex
   \/([^\\/\[]|\\.|(\[([^\]\\]|\\.)*\]))*\/ { return LexerTokens.REGEX; }

   [.]  { return LexerTokens.DOT; }

   [()\[\]]  { return LexerTokens.BRACKET; }

   \{ {
           objectDepth=1;
           yybegin(EXP_OBJECT);
           return LexerTokens.EXPRESSION;
      }

   "}}" {
        yypushback(yylength());
        leave();
   }

   "|>" {
        yypushback(yylength());
        leave();
   }

   [^\/\"'`(){} \t\n\r]+ { return LexerTokens.EXPRESSION; }

   <<EOF>> {
           leave();
           return LexerTokens.UNKNOWN;
      }

   [^ \t] {
           leave();
           return LexerTokens.UNKNOWN;
      }
}

<EXP_OBJECT> {

    \{ {
            objectDepth++;
            return LexerTokens.EXPRESSION;
        }

    [\"][^\"\n\r]*[\"] { return LexerTokens.STRING; }

    [^}{\"]+ {return LexerTokens.EXPRESSION;}

    \} {
            objectDepth--;
            if (objectDepth == 0) {
                yybegin(EXPRESSION);
            }
            return LexerTokens.EXPRESSION;
        }

    <<EOF>> {
             leave();
             return LexerTokens.UNKNOWN;
        }

}



