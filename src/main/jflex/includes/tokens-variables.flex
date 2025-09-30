// BLOCK 1 - START
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.VentoParserTypes;
import org.js.vento.plugin.lexer.VentoLexerTypes;
import static com.intellij.psi.TokenType.WHITE_SPACE;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state JSON_STRING
%state JS_OBJECT
%state JS_REGEX
%state JS_STRING_BACK_TICK
%state JS_STRING_DOUBLE_QOUTE
%state JS_STRING_SINGLE_QUOTE
%state VARIABLE_CONTENT
%state BRACKET


CLOSE_VENTO_BLOCK = -?}}
WHITESPACE = [ \t\r\n]+

// BLOCK 2 - END
%%


<VARIABLE_CONTENT> {

   //strings
   \" {
            yybegin(JS_STRING_DOUBLE_QOUTE);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   ' {
            yybegin(JS_STRING_SINGLE_QUOTE);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   ` {
            yybegin(JS_STRING_BACK_TICK);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   // regex segment
   \/  {
            yybegin(JS_REGEX);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   //objects
   \{ {
            objectDepth=1;
            yybegin(JS_OBJECT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   "|>" {
           return VentoLexerTypes.PIPE_ELEMENT;
   }

   \- / [^}] {return VentoLexerTypes.VARIABLE_ELEMENT;}


   [^\/\"'`{}\- \t]+ { return VentoLexerTypes.VARIABLE_ELEMENT; }

   {WHITESPACE} {}

   {CLOSE_VENTO_BLOCK} {
            yybegin(YYINITIAL);
            return VentoLexerTypes.VARIABLE_END;
   }

   [^] { return VentoLexerTypes.ERROR; }
}

<JS_OBJECT> {

    \{ {
            objectDepth++;
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    [^}{\"]+ {return VentoLexerTypes.VARIABLE_ELEMENT;}

    \} {
            objectDepth--;
            if (objectDepth == 0) {
             yybegin(VARIABLE_CONTENT);
            }
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    //single line strings
    \" {
            yybegin(JSON_STRING);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JSON_STRING> {

    [^\"]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    \" {
            yybegin(JS_OBJECT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_STRING_DOUBLE_QOUTE> {

    "\\\"" { return VentoLexerTypes.VARIABLE_ELEMENT;}

    [^\\\"]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    [\"]+ {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_STRING_SINGLE_QUOTE> {

    "\\'" { return VentoLexerTypes.VARIABLE_ELEMENT;}
    [^']+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    ' {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_STRING_BACK_TICK> {

    [^`]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    ` {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_REGEX> {

    \[ {
            yybegin(BRACKET);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    [^\/\[]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    \/ {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<BRACKET> {

  "]"        { yybegin(JS_REGEX); return VentoLexerTypes.VARIABLE_ELEMENT; }
  [^\]]+     { return VentoLexerTypes.VARIABLE_ELEMENT; }   // any char except ']'

}
