// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state NOKEYWORDS


// BLOCK 2 - END
%%


<NOKEYWORDS> {

    {WHITESPACE} {  }

    [\w] {
          yypushback(yylength());
          enter(EXPRESSION);
      }

}



