// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FOR_COLLECTION
%state FOR_CONTENT
%state FOR_VALUE
%state FOR_OBJECT
%state FOR_ARRAY
%state FOR_PIPE

FOR_KEY = "for"

// BLOCK 2 - END
%%


<FOR_CONTENT> {

    {WHITESPACE} {  }

    {FOR_KEY} / [ \t].*"}}" {
            yybegin(FOR_VALUE);
            return LexerTokens.FOR_KEY;
        }

    [/]{FOR_KEY} { return LexerTokens.CLOSE_FOR_KEY; }

    [/][f]?[o]?[r]? { return LexerTokens.UNKNOWN; }

    "}}" {
            yybegin(YYINITIAL);
            return LexerTokens.FOR_END;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^/] {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<FOR_VALUE> {

    {WHITESPACE} {  }

    [^ \t]+"await"?"index,"?.+ / [ \t]+"of"[ \t]+   {
            value = true;
            return LexerTokens.FOR_VALUE;
        }

    "of" {
            if(value == true) {
                value= false;
                yybegin(FOR_COLLECTION);
                return LexerTokens.FOR_OF;
            }   else {
                value= false;
                yybegin(FOR_COLLECTION);
                return LexerTokens.UNKNOWN;
            }
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] {
            yybegin(FOR_COLLECTION);
            return LexerTokens.UNKNOWN;
        }

}

<FOR_COLLECTION> {

    {WHITESPACE} {  }

    \{ {
            objectDepth = 0;
            objectDepth++;
            yybegin(FOR_OBJECT);
            return LexerTokens.FOR_COLLECTION;
        }

    \[ {
            objectDepth = 0;
            objectDepth++;
            yybegin(FOR_ARRAY);
            return LexerTokens.FOR_COLLECTION;
        }

    [^}{\]\[]+ {
            collection = true;
            return LexerTokens.FOR_COLLECTION;
        }

    "}}"  {
            if(collection == true){
                collection = false;
                yybegin(YYINITIAL);
                return LexerTokens.FOR_END;
            } else {
                collection = false;
                yybegin(YYINITIAL);
                return LexerTokens.UNKNOWN;
            }
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yybegin(FOR_VALUE);
        yypushback(yylength());
    }
}

<FOR_OBJECT> {

    {WHITESPACE} {  }

    \{ {
            objectDepth++;
            return LexerTokens.FOR_COLLECTION;
        }

    [^}{]+ {return LexerTokens.FOR_COLLECTION;}

    \} {
            objectDepth--;
            if (objectDepth == 0) {
                yybegin(FOR_PIPE);
            }
            return LexerTokens.FOR_COLLECTION;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<FOR_ARRAY> {

    {WHITESPACE} {  }

    \[ {
            objectDepth++;
            return LexerTokens.FOR_COLLECTION;
        }

    [^\]\[]+ {return LexerTokens.FOR_COLLECTION;}

    \] {
            objectDepth--;
            if (objectDepth == 0) {
                yybegin(FOR_PIPE);
            }
            return LexerTokens.FOR_COLLECTION;
        }

    \][ \t]*[.].*\(.*\) {
            objectDepth--;
            if (objectDepth == 0) {
                yybegin(FOR_PIPE);
            }
            return LexerTokens.FOR_COLLECTION;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

}

<FOR_PIPE> {

     {WHITESPACE} {  }

    "|> ".+ / [ \t]"}}" {
            yybegin(FOR_CONTENT);
            return LexerTokens.FOR_COLLECTION;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yybegin(FOR_CONTENT);
        yypushback(yylength());
      }
}


