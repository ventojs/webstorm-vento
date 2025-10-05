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

    [^] {
          yypushback(yylength());
          yybegin(FILE);
    }
}

<VALUES> {
    {WHITESPACE}   {  }

    \{  { return VentoLexerTypes.IMPORT_VALUES; }
    \,  { return VentoLexerTypes.IMPORT_VALUES; }
    \} / {WHITESPACE}{FROM}  {
          yybegin(IMPORT);
          return VentoLexerTypes.IMPORT_VALUES;
    }

    {FROM}.*{CBLOCK} {
        yypushback(yylength());
        yybegin(IMPORT);
    }

    [a-zA-Z_$]+[a-zA-Z_$0-9]*  { return VentoLexerTypes.IMPORT_VALUES; }
    [a-zA-Z_$]+[a-zA-Z_$0-9]* / {WHITESPACE}{FROM} {
          yybegin(IMPORT);
          return VentoLexerTypes.IMPORT_VALUES;
    }

    [^] {
          yypushback(yylength());
          yybegin(IMPORT);
    }

}

<FILE> {
    {WHITESPACE}   {  }

    [\"][.]?[/]?.*".vto"[\"] / {WHITESPACE}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.IMPORT_FILE;
    }

    [^ \t].+ / {WHITESPACE}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.UNKNOWN;
    }

    [^] {
          yypushback(yylength());
          yybegin(BLOCK);
    }

}
