// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state IMPORT

IMPORT = "import"
FROM = "from"
IMP_ID = [a-zA-Z_$]+[a-zA-Z_$0-9]*([ \t]+as[ \t]+[a-zA-Z_$]+[a-zA-Z_$0-9]*)?

// BLOCK 2 - END
%%

<IMPORT> {

    {WHITESPACE} {  }

    "{"{OWS}{IMP_ID}{OWS}(,{OWS}{IMP_ID}{OWS})*"}" {
          return LexerTokens.IMPORT_VALUES;
      }

    "{"{OWS}{IMP_ID}{OWS}(,{OWS}{IMP_ID}{OWS})* { yypushback(2); return LexerTokens.UNKNOWN;}

    {FROM} {
          enter(FILE);
          return LexerTokens.IMPORT_FROM;
      }

    \" {
          yypushback(yylength());
          enter(FILE);
      }

    \, { return LexerTokens.IMPORT_VALUES; }

    {IMP_ID} { return LexerTokens.IMPORT_VALUES; }

    {IMP_ID} / {WHITESPACE}{FROM} {
          return LexerTokens.IMPORT_VALUES;
      }

}

