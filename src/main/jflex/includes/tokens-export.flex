// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state EXPORT
%state EXPORT_CLOSE
%state EXPORT_VALUE
%state EXPORT_BLOCK_MODE
%state EXPORT_FUNCTION_BLOCK

EXPORT = "export"
// Simple identifiers and string literals used in export value
IDENT = [a-zA-Z_$]+[a-zA-Z_$0-9]*
STRING = [\"][^\"\n\r]*[\"]
OWS =[ \t\n\r]*
PIPE = "|>"

// BLOCK 2 - END
%%


<EXPORT> {
    {WHITESPACE}   {  }

    {EXPORT} / {WHITESPACE} {
          yybegin(EXPORT_VALUE);
          return VentoLexerTypes.EXPORT_KEY;
    }

    {EXPORT}{WHITESPACE}.*{OBLOCK}{OWS}[/]{EXPORT}{OWS}{CBLOCK} {
          yybegin(EXPORT_BLOCK_MODE);
          yypushback(yylength()-6);
          return VentoLexerTypes.EXPORT_KEY;
    }

    [^] {
          yypushback(yylength());
          yybegin(BLOCK);
    }
}

<EXPORT_VALUE> {
    {WHITESPACE}   {  }

    {IDENT} { return VentoLexerTypes.EXPORT_VAR; }
    "=" { return VentoLexerTypes.EXPORT_EQ; }
    {STRING} { return VentoLexerTypes.EXPORT_VALUE; }

    {PIPE} {
         yypushback(yylength());
         enter(PIPE);
    }

    {CBLOCK} {
          yybegin(BLOCK);
          yypushback(yylength());
    }

    [^] {
          return VentoLexerTypes.UNKNOWN;
    }

}

<EXPORT_BLOCK_MODE> {
    {WHITESPACE}   {  }

    {IDENT} { return VentoLexerTypes.EXPORT_VAR; }

    {CBLOCK} {
         yybegin(BLOCK);
         yypushback(yylength());
    }

    [^] {
         return VentoLexerTypes.UNKNOWN;
    }
}

<EXPORT_CLOSE> {
    {WHITESPACE}   {  }

    [/]{EXPORT} / {OWS}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.EXPORT_CLOSE_KEY;
    }

    [^] {
          yypushback(yylength());
          yybegin(BLOCK);
    }
}

<EXPORT_FUNCTION_BLOCK> {

    {WHITESPACE}   { }

    {EXPORT} { return VentoLexerTypes.EXPORT_KEY; }

    {FUNCTION} { return VentoLexerTypes.EXPORT_FUNCTION_KEY; }

    {IDENT} { return VentoLexerTypes.EXPORT_VAR; }

    "("{IDENT}?([,]{IDENT})*")" { return VentoLexerTypes.EXPORT_FUNCTION_ARGS; }

    {PIPE} {
         yypushback(yylength());
         enter(PIPE);
    }

    {CBLOCK} {
         yypushback(yylength());
         yybegin(BLOCK);
    }

    [^] {
         return VentoLexerTypes.UNKNOWN;
    }
}


