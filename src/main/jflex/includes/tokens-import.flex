// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state IMPORT
%state EXPORT
%state VALUES

OBLOCK = \{\{
CBLOCK = }}
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
WHITESPACE = [ \t\r\n]+

IMPORT = "import"
EXPORT = "export"
FROM = "from"

// BLOCK 2 - END
%%




<IMPORT> {
    {WHITESPACE}   {  }

    {IMPORT} / [ \t] {
          yybegin(VALUES);
          return VentoLexerTypes.IMPORT_KEY;
    }

    {FROM} { return VentoLexerTypes.IMPORT_FROM; }

    [\"][.]?[/]?.*".vto"[\"] / {WHITESPACE}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.IMPORT_FILE;
    }


    [^] {
          yypushback(yylength());
          yybegin(BLOCK);
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
