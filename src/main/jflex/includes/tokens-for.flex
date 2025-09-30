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

    {FOR_KEY} / .*"}}"   {
          yybegin(FOR_VALUE);
          return VentoLexerTypes.FOR_KEY;
    }

    [/]{FOR_KEY}    {
          return VentoLexerTypes.CLOSE_FOR_KEY;
    }

    "}}"  {
          yybegin(YYINITIAL);
          return VentoLexerTypes.FOR_END;
    }

    [^] {
          yybegin(YYINITIAL);
          return VentoLexerTypes.ERROR;
    }

}

<FOR_VALUE> {

    {WHITESPACE}   {  }

    [^ \t]+ / [ \t]+"of"[ \t]+   {
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

    [^}{]+ {
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
             yybegin(FOR_CONTENT);
            }
            return VentoLexerTypes.FOR_COLLECTION;
    }

    [^] {
            yybegin(FOR_COLLECTION);
            yypushback(yylength());
    }

}


