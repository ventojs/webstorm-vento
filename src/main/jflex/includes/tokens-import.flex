// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state IMPORT
%state VALUES
%state FILE

IMPORT = "import"
FROM = "from"
IMP_ID = [a-zA-Z_$]+[a-zA-Z_$0-9]*([ \t]+as[ \t]+[a-zA-Z_$]+[a-zA-Z_$0-9]*)?
OWS =[ \t\n\r]*

// BLOCK 2 - END
%%

<IMPORT> {
    {WHITESPACE} {  }

    {IMPORT} / [ \t] {
        yybegin(VALUES);
        return LexerTypes.IMPORT_KEY;
    }

    {FROM} {
        yybegin(FILE);
        return LexerTypes.IMPORT_FROM;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yypushback(yylength());
        yybegin(FILE);
    }
}

<VALUES> {
    {WHITESPACE} {  }

    "{"{OWS}{IMP_ID}{OWS}(,{OWS}{IMP_ID}{OWS})*"}" {
        yybegin(IMPORT);
        return LexerTypes.IMPORT_VALUES;
    }

    \, { return LexerTypes.IMPORT_VALUES; }

    {FROM}.*{CBLOCK} {
        yypushback(yylength());
        yybegin(IMPORT);
    }

    {IMP_ID} { return LexerTypes.IMPORT_VALUES; }
    {IMP_ID} / {WHITESPACE}{FROM} {
        yybegin(IMPORT);
        return LexerTypes.IMPORT_VALUES;
    }

    <<EOF>> {
        yybegin(IMPORT);
        return LexerTypes.UNKNOWN;
    }

    [^] {
        yypushback(yylength());
        yybegin(IMPORT);
    }

}

<FILE> {
    {WHITESPACE} {  }

    [\"][.]?[/]?.*[\"] / {WHITESPACE}{CBLOCK} {
        yybegin(BLOCK);
        return LexerTypes.IMPORT_FILE;
    }

    [^ \t].+ / {WHITESPACE}{CBLOCK} {
        yybegin(BLOCK);
        return LexerTypes.UNKNOWN;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return LexerTypes.ERROR;
    }

    [^] {
        yypushback(yylength());
        yybegin(BLOCK);
    }

}
