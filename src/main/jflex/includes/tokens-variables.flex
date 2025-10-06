// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
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


// BLOCK 2 - END
%%


<VARIABLE_CONTENT> {

   //strings
   \" {
            yybegin(JS_STRING_DOUBLE_QOUTE);
            return VentoLexerTypes.STRING;
   }

   ' {
            yybegin(JS_STRING_SINGLE_QUOTE);
            return VentoLexerTypes.STRING;
   }

   ` {
            yybegin(JS_STRING_BACK_TICK);
            return VentoLexerTypes.STRING;
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

   {CVAR} {
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
            return VentoLexerTypes.STRING;
    }

}

<JSON_STRING> {

    [^\"]+ { return VentoLexerTypes.STRING;}

    \" {
            yybegin(JS_OBJECT);
            return VentoLexerTypes.STRING;
    }

}

<JS_STRING_DOUBLE_QOUTE> {

    "\\\"" { return VentoLexerTypes.STRING;}

    [^\\\"]+ { return VentoLexerTypes.STRING;}

    [\"]+ {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.STRING;
    }

}

<JS_STRING_SINGLE_QUOTE> {

    "\\'" { return VentoLexerTypes.STRING;}
    [^']+ { return VentoLexerTypes.STRING;}

    ' {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.STRING;
    }

}

<JS_STRING_BACK_TICK> {

    [^`]+ { return VentoLexerTypes.STRING;}

    ` {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.STRING;
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

<JS_STRING_DOUBLE_QOUTE,JS_STRING_SINGLE_QUOTE,JS_STRING_BACK_TICK,JS_REGEX,BRACKET,JSON_STRING,JS_OBJECT> [^] { return VentoLexerTypes.ERROR; }
