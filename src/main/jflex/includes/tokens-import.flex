// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
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
    {WHITESPACE}   {  }

    {IMPORT} / [ \t] {
          yybegin(VALUES);
          return VentoLexerTypes.IMPORT_KEY;
    }

    {FROM} {
          yybegin(FILE);
          return VentoLexerTypes.IMPORT_FROM;
    }

    <<EOF>> {
              // Unterminated pipe at EOF: reset and consume safely
              yybegin(YYINITIAL);
              return VentoLexerTypes.ERROR;
        }

    [^] {
          yypushback(yylength());
          yybegin(FILE);
    }
}

<VALUES> {
    {WHITESPACE}   {  }

    "{"{OWS}{IMP_ID}{OWS}(,{OWS}{IMP_ID}{OWS})*"}" {
          yybegin(IMPORT);
          return VentoLexerTypes.IMPORT_VALUES;
    }
//    \{  { return VentoLexerTypes.IMPORT_VALUES; }
    \,  { return VentoLexerTypes.IMPORT_VALUES; }
//    \} / {WHITESPACE}{FROM}  {
//          yybegin(IMPORT);
//          return VentoLexerTypes.IMPORT_VALUES;
//    }

    {FROM}.*{CBLOCK} {
        yypushback(yylength());
        yybegin(IMPORT);
    }

    {IMP_ID}  { return VentoLexerTypes.IMPORT_VALUES; }
    {IMP_ID} / {WHITESPACE}{FROM} {
          yybegin(IMPORT);
          return VentoLexerTypes.IMPORT_VALUES;
    }

    <<EOF>> {

              yybegin(IMPORT);
              return VentoLexerTypes.UNKNOWN;
        }

    [^] {
          yypushback(yylength());
          yybegin(IMPORT);
    }

}

<FILE> {
    {WHITESPACE}   {  }

    [\"][.]?[/]?.*[\"] / {WHITESPACE}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.IMPORT_FILE;
    }

    [^ \t].+ / {WHITESPACE}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.UNKNOWN;
    }

    <<EOF>> {
              // Unterminated pipe at EOF: reset and consume safely
              yybegin(YYINITIAL);
              return VentoLexerTypes.ERROR;
        }

    [^] {
          yypushback(yylength());
          yybegin(BLOCK);
    }

}
