// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state IMPORT
%state VALUES
%state FILE

IMPORT = "import"
FROM = "from"
IMP_ID = [a-zA-Z_$]+[a-zA-Z_$0-9]*([ \t]+as[ \t]+[a-zA-Z_$]+[a-zA-Z_$0-9]*)?

// BLOCK 2 - END
%%

<IMPORT> {
    {WHITESPACE} {  }

    {IMPORT} / [ \t] {
            yybegin(VALUES);
            return LexerTokens.IMPORT_KEY;
        }

    {FROM} {
            yybegin(FILE);
            return LexerTokens.IMPORT_FROM;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
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
            return LexerTokens.IMPORT_VALUES;
        }

    \, { return LexerTokens.IMPORT_VALUES; }

    {FROM}.*{CBLOCK} {
        yypushback(yylength());
        yybegin(IMPORT);
    }

    {IMP_ID} { return LexerTokens.IMPORT_VALUES; }
    {IMP_ID} / {WHITESPACE}{FROM} {
            yybegin(IMPORT);
            return LexerTokens.IMPORT_VALUES;
        }

    <<EOF>> {
            yybegin(IMPORT);
            return LexerTokens.UNKNOWN;
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
            return LexerTokens.IMPORT_FILE;
        }

    [^ \t].+ / {WHITESPACE}{CBLOCK} {
            yybegin(BLOCK);
            return LexerTokens.UNKNOWN;
        }

    <<EOF>> {
            yybegin(YYINITIAL);
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yypushback(yylength());
        yybegin(BLOCK);
    }

}
