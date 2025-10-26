// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START

%state EXPRESSION
%state REGEX
%state REGEX_ESCAPE
%state REGEX_CLASS

// BLOCK 2 - END
%%

<EXPRESSION> {

    {WHITESPACE} { }

   //strings
   \"|\'|\` {  yypushback(yylength()); enter(STRING); }

   // regex
   \/ {
        enter(REGEX);
        return LexerTokens.REGEX;
   }

   [.]  { return LexerTokens.DOT; }
   [,]  { return LexerTokens.COMMA; }

   [()]  { return LexerTokens.PARENTHESIS; }

   \[ {
            objectDepth=0;
            yypushback(yylength());
            enter(ARRAY);
        }

   \{ {
        objectDepth=0;
        yypushback(yylength());
        enter(OBJECT);
      }

   "|>" {
        yypushback(yylength());
        leave();
    }

   ([0-9][0-9_]?)*[0-9]+ {return LexerTokens.NUMBER;}

   [^\/\"'`()\[\]{},. \t\n\r]+ { return LexerTokens.SYMBOL; }

   [\]\}] {
          yypushback(yylength());
          leave();
      }
}

<REGEX> {
    \/ { leave(); return LexerTokens.REGEX; }
    [\\] { yypushback(yylength()); enter(REGEX_ESCAPE); }
    \[ { enter(REGEX_CLASS);return LexerTokens.REGEX; }
    [^/\\]+ { return LexerTokens.REGEX; }
}

<REGEX_CLASS> {
    \] { leave(); return LexerTokens.REGEX;}
    "\[" { return LexerTokens.REGEX; }
    [^\]]+ { return LexerTokens.REGEX;}
}

<REGEX_ESCAPE> {
    [\\][^] { leave(); return LexerTokens.REGEX;}
}



