// BLOCK 1 - START
import org.js.vento.plugin.lexer.VentoLexerTypes;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state EXPORT
%state EXPORT_VALUE

EXPORT = "export"
// Simple identifiers and string literals used in export value
IDENT = [a-zA-Z_$]+[a-zA-Z_$0-9]*
STRING = [\"][^\"\n\r]*[\"]

// BLOCK 2 - END
%%


<EXPORT> {
    {WHITESPACE}   {  }

    {EXPORT} / {WHITESPACE} {
          yybegin(EXPORT_VALUE);
          return VentoLexerTypes.EXPORT_KEY;
    }

    // Fallback to block if not actually an export keyword
    [^] {
          System.out.println("ERR1!");
          yypushback(yylength());
          yybegin(BLOCK);
    }
}

<EXPORT_VALUE> {
    {WHITESPACE}   {  }

   // Basic tokens for a simple variable export: name = "value"
    {IDENT} { return VentoLexerTypes.EXPORT_VAR; }
    "=" { return VentoLexerTypes.EXPORT_EQ; }
    {STRING} { return VentoLexerTypes.EXPORT_VALUE; }

    // When we hit the close-block token sequence, let the outer state handle it
    {CBLOCK} {
          yybegin(BLOCK);
          yypushback(yylength());
    }

    // Any other single character, consume as generic content to avoid no-match errors
    [^] {
          System.out.println("ERR2!");
          return VentoLexerTypes.ERROR; }

}
