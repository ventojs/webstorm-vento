// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
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
            return LexerTokens.STRING;
        }

    ' {
            yybegin(JS_STRING_SINGLE_QUOTE);
            return LexerTokens.STRING;
          }

    ` {
            yybegin(JS_STRING_BACK_TICK);
            return LexerTokens.STRING;
        }

    //regex
    \/  {
            yybegin(JS_REGEX);
            return LexerTokens.VARIABLE_ELEMENT;
        }

    //object
    \{ {
            objectDepth=1;
            yybegin(JS_OBJECT);
            return LexerTokens.VARIABLE_ELEMENT;
        }

    "|>" { return LexerTokens.PIPE; }

    \- / [^}] {return LexerTokens.VARIABLE_ELEMENT;}

    [^\/\"'`{}\- \t]+ { return LexerTokens.VARIABLE_ELEMENT; }

    {WHITESPACE} { }

    {CVAR} {
            yybegin(YYINITIAL);
            return LexerTokens.VARIABLE_END;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

   [^] { return LexerTokens.UNKNOWN; }
}

<JS_OBJECT> {

    \{ {
            objectDepth++;
            return LexerTokens.VARIABLE_ELEMENT;
        }

    [^}{\"]+ { return LexerTokens.VARIABLE_ELEMENT; }

    \} {
            objectDepth--;
            if (objectDepth == 0) {
                yybegin(VARIABLE_CONTENT);
            }
            return LexerTokens.VARIABLE_ELEMENT;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    \" {
            yybegin(JSON_STRING);
            return LexerTokens.STRING;
        }

}

<JSON_STRING> {

    [^\"]+ { return LexerTokens.STRING;}

    \" {
            yybegin(JS_OBJECT);
            return LexerTokens.STRING;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<JS_STRING_DOUBLE_QOUTE> {

    "\\\"" { return LexerTokens.STRING;}

    [^\\\"]+ { return LexerTokens.STRING;}

    [\"]+ {
            yybegin(VARIABLE_CONTENT);
            return LexerTokens.STRING;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<JS_STRING_SINGLE_QUOTE> {

    "\\'" { return LexerTokens.STRING;}
    [^']+ { return LexerTokens.STRING;}

    ' {
            yybegin(VARIABLE_CONTENT);
            return LexerTokens.STRING;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<JS_STRING_BACK_TICK> {

    [^`]+ { return LexerTokens.STRING;}

    ` {
            yybegin(VARIABLE_CONTENT);
            return LexerTokens.STRING;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<JS_REGEX> {

    \[ {
            yybegin(BRACKET);
            return LexerTokens.VARIABLE_ELEMENT;
        }

    [^\/\[]+ { return LexerTokens.VARIABLE_ELEMENT;}

    \/ {
            yybegin(VARIABLE_CONTENT);
            return LexerTokens.VARIABLE_ELEMENT;
        }

    <<EOF>> {
            yybegin(VARIABLE_CONTENT);
            return LexerTokens.UNKNOWN;
        }

}

<BRACKET> {

    "]"        { yybegin(JS_REGEX); return LexerTokens.VARIABLE_ELEMENT; }
    [^\]]+     { return LexerTokens.VARIABLE_ELEMENT; }   // any char except ']'

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }
}

<JS_STRING_DOUBLE_QOUTE,JS_STRING_SINGLE_QUOTE,JS_STRING_BACK_TICK,JS_REGEX,BRACKET,JSON_STRING,JS_OBJECT> [^] { return LexerTokens.UNKNOWN; }
