// BLOCK 1 - START
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.VentoParserTypes;
import org.js.vento.plugin.lexer.VentoLexerTypes;
import static com.intellij.psi.TokenType.WHITE_SPACE;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FOR_COLLECTION
%state FOR_CONTENT
%state FOR_VALUE
%state FOR_OBJECT
%state FOR_ARRAY
%state FOR_PIPE



CLOSE_COMMENT_PHRASE = -?#}}
CLOSE_JAVASCRIPT = }}
DEFAULT_HTML = [^{]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
FOR_KEY = "for"
OPEN_JAVASCRIPT = \{\{>
OPEN_VENTO_BLOCK = \{\{-?
WHITESPACE = [ \t\r\n]+



// BLOCK 2 - END
%%


<FOR_CONTENT> {

    {WHITESPACE}   {  }

    {FOR_KEY} / [ \t].*"}}"   {
          yybegin(FOR_VALUE);
          return VentoLexerTypes.FOR_KEY;
    }

    [/]{FOR_KEY}    {
          return VentoLexerTypes.CLOSE_FOR_KEY;
    }

    [/][f]?[o]?[r]? {
          return VentoLexerTypes.ERROR;
    }

    "}}"  {
          yybegin(YYINITIAL);
          return VentoLexerTypes.FOR_END;
    }

    [^/] {
          //System.out.println("for error : "+yytext());
          yybegin(YYINITIAL);
          return VentoLexerTypes.ERROR;
    }

}

<FOR_VALUE> {

    {WHITESPACE}   {  }

    [^ \t]+"await"?"index,"?.+ / [ \t]+"of"[ \t]+   {
          value = true;
          return VentoLexerTypes.FOR_VALUE;
    }

    "of"   {
          if(value == true) {
              value= false;
              yybegin(FOR_COLLECTION);
              return VentoLexerTypes.FOR_OF;
          } else {
              value= false;
              yybegin(FOR_COLLECTION);
              return VentoLexerTypes.ERROR;
          }
    }

    [^] {
          //System.out.println("value error");
          yybegin(FOR_COLLECTION);
          return VentoLexerTypes.ERROR;
    }

}

<FOR_COLLECTION> {

    {WHITESPACE}   {  }

    \{ {
          objectDepth = 0;
          objectDepth++;
          yybegin(FOR_OBJECT);
          return VentoLexerTypes.FOR_COLLECTION;
    }

    \[ {
          objectDepth = 0;
          objectDepth++;
          yybegin(FOR_ARRAY);
          return VentoLexerTypes.FOR_COLLECTION;
    }

    [^}{\]\[]+ {
          collection = true;
          return VentoLexerTypes.FOR_COLLECTION;
      }

    "}}"  {
          if(collection == true){
              collection = false;
              yybegin(YYINITIAL);
              return VentoLexerTypes.FOR_END;
          } else {
              collection = false;
              yybegin(YYINITIAL);
              return VentoLexerTypes.ERROR;
          }
    }

    [^] {
          //System.out.println("collection error:" + yytext());
          yybegin(FOR_VALUE);
          yypushback(yylength());
    }
}

<FOR_OBJECT> {

    {WHITESPACE}   {  }

    \{ {
            objectDepth++;
            return VentoLexerTypes.FOR_COLLECTION;
    }

    [^}{]+ {return VentoLexerTypes.FOR_COLLECTION;}

    \} {
            objectDepth--;
            if (objectDepth == 0) {
             yybegin(FOR_PIPE);
            }
            return VentoLexerTypes.FOR_COLLECTION;
    }

    [^] {
            yybegin(FOR_COLLECTION);
            yypushback(yylength());
    }

}

<FOR_ARRAY> {

    {WHITESPACE}   {  }

    \[ {
          objectDepth++;
          return VentoLexerTypes.FOR_COLLECTION;
    }

    [^\]\[]+ {return VentoLexerTypes.FOR_COLLECTION;}

    \] {
          objectDepth--;
          if (objectDepth == 0) {
            yybegin(FOR_PIPE);
          }
          return VentoLexerTypes.FOR_COLLECTION;
    }

    \][ \t]*[.].*\(.*\) {
          objectDepth--;
          if (objectDepth == 0) {
             yybegin(FOR_PIPE);
          }
          return VentoLexerTypes.FOR_COLLECTION;
    }

    [^] {
            yybegin(FOR_COLLECTION);
            yypushback(yylength());
    }

}

<FOR_PIPE> {

     {WHITESPACE}   {  }

    "|> ".+ / [ \t]"}}" {
          yybegin(FOR_CONTENT);
          return VentoLexerTypes.FOR_COLLECTION;
    }

    [^] {
          yybegin(FOR_CONTENT);
          yypushback(yylength());
      }
}


