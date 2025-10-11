// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
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
        return LexerTypes.FOR_KEY;
    }

    [/]{FOR_KEY} { return LexerTypes.CLOSE_FOR_KEY; }

    [/][f]?[o]?[r]? { return LexerTypes.ERROR; }

    "}}" {
        yybegin(YYINITIAL);
        return LexerTypes.FOR_END;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^/] {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

}

<FOR_VALUE> {

    {WHITESPACE} {  }

    [^ \t]+"await"?"index,"?.+ / [ \t]+"of"[ \t]+   {
        value = true;
        return LexerTypes.FOR_VALUE;
    }

    "of" {
        if(value == true) {
            value= false;
            yybegin(FOR_COLLECTION);
            return LexerTypes.FOR_OF;
        }   else {
            value= false;
            yybegin(FOR_COLLECTION);
            return LexerTypes.ERROR;
        }
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yybegin(FOR_COLLECTION);
        return LexerTypes.ERROR;
    }

}

<FOR_COLLECTION> {

    {WHITESPACE} {  }

    \{ {
        objectDepth = 0;
        objectDepth++;
        yybegin(FOR_OBJECT);
        return LexerTypes.FOR_COLLECTION;
    }

    \[ {
        objectDepth = 0;
        objectDepth++;
        yybegin(FOR_ARRAY);
        return LexerTypes.FOR_COLLECTION;
    }

    [^}{\]\[]+ {
        collection = true;
        return LexerTypes.FOR_COLLECTION;
    }

    "}}"  {
        if(collection == true){
            collection = false;
            yybegin(YYINITIAL);
            return LexerTypes.FOR_END;
        } else {
            collection = false;
            yybegin(YYINITIAL);
            return LexerTypes.ERROR;
        }
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
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
        return LexerTypes.FOR_COLLECTION;
    }

    [^}{]+ {return LexerTypes.FOR_COLLECTION;}

    \} {
        objectDepth--;
        if (objectDepth == 0) {
            yybegin(FOR_PIPE);
        }
        return LexerTypes.FOR_COLLECTION;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yybegin(FOR_COLLECTION);
        yypushback(yylength());
    }

}

<FOR_ARRAY> {

    {WHITESPACE} {  }

    \[ {
        objectDepth++;
        return LexerTypes.FOR_COLLECTION;
    }

    [^\]\[]+ {return LexerTypes.FOR_COLLECTION;}

    \] {
        objectDepth--;
        if (objectDepth == 0) {
            yybegin(FOR_PIPE);
        }
        return LexerTypes.FOR_COLLECTION;
    }

    \][ \t]*[.].*\(.*\) {
        objectDepth--;
        if (objectDepth == 0) {
            yybegin(FOR_PIPE);
        }
        return LexerTypes.FOR_COLLECTION;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yybegin(FOR_COLLECTION);
        yypushback(yylength());
    }

}

<FOR_PIPE> {

     {WHITESPACE} {  }

    "|> ".+ / [ \t]"}}" {
        yybegin(FOR_CONTENT);
        return LexerTypes.FOR_COLLECTION;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yybegin(FOR_CONTENT);
        yypushback(yylength());
      }
}


