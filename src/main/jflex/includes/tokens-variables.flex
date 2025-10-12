// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
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
        return LexerTypes.STRING;
    }

    ' {
        yybegin(JS_STRING_SINGLE_QUOTE);
        return LexerTypes.STRING;
      }

    ` {
        yybegin(JS_STRING_BACK_TICK);
        return LexerTypes.STRING;
    }

    //regex
    \/  {
        yybegin(JS_REGEX);
        return LexerTypes.VARIABLE_ELEMENT;
    }

    //object
    \{ {
        objectDepth=1;
        yybegin(JS_OBJECT);
        return LexerTypes.VARIABLE_ELEMENT;
    }

    "|>" { return LexerTypes.PIPE_ELEMENT; }

    \- / [^}] {return LexerTypes.VARIABLE_ELEMENT;}

    [^\/\"'`{}\- \t]+ { return LexerTypes.VARIABLE_ELEMENT; }

    {WHITESPACE} { }

    {CVAR} {
        yybegin(YYINITIAL);
        return LexerTypes.VARIABLE_END;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

   [^] { return LexerTypes.ERROR; }
}

<JS_OBJECT> {

    \{ {
        objectDepth++;
        return LexerTypes.VARIABLE_ELEMENT;
    }

    [^}{\"]+ { return LexerTypes.VARIABLE_ELEMENT; }

    \} {
        objectDepth--;
        if (objectDepth == 0) {
            yybegin(VARIABLE_CONTENT);
        }
        return LexerTypes.VARIABLE_ELEMENT;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    \" {
        yybegin(JSON_STRING);
        return LexerTypes.STRING;
    }

}

<JSON_STRING> {

    [^\"]+ { return LexerTypes.STRING;}

    \" {
        yybegin(JS_OBJECT);
        return LexerTypes.STRING;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

}

<JS_STRING_DOUBLE_QOUTE> {

    "\\\"" { return LexerTypes.STRING;}

    [^\\\"]+ { return LexerTypes.STRING;}

    [\"]+ {
        yybegin(VARIABLE_CONTENT);
        return LexerTypes.STRING;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

}

<JS_STRING_SINGLE_QUOTE> {

    "\\'" { return LexerTypes.STRING;}
    [^']+ { return LexerTypes.STRING;}

    ' {
        yybegin(VARIABLE_CONTENT);
        return LexerTypes.STRING;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

}

<JS_STRING_BACK_TICK> {

    [^`]+ { return LexerTypes.STRING;}

    ` {
        yybegin(VARIABLE_CONTENT);
        return LexerTypes.STRING;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

}

<JS_REGEX> {

    \[ {
        yybegin(BRACKET);
        return LexerTypes.VARIABLE_ELEMENT;
    }

    [^\/\[]+ { return LexerTypes.VARIABLE_ELEMENT;}

    \/ {
        yybegin(VARIABLE_CONTENT);
        return LexerTypes.VARIABLE_ELEMENT;
    }

    <<EOF>> {
        yybegin(VARIABLE_CONTENT);
        return LexerTypes.ERROR;
    }

}

<BRACKET> {

    "]"        { yybegin(JS_REGEX); return LexerTypes.VARIABLE_ELEMENT; }
    [^\]]+     { return LexerTypes.VARIABLE_ELEMENT; }   // any char except ']'

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }
}

<JS_STRING_DOUBLE_QOUTE,JS_STRING_SINGLE_QUOTE,JS_STRING_BACK_TICK,JS_REGEX,BRACKET,JSON_STRING,JS_OBJECT> [^] { return LexerTypes.ERROR; }
