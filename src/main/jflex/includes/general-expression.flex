// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;import org.js.vento.plugin.lexer.VentoLexer;
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
   \"|\'|\` {  pushbackall(); enter(STRING); }

   // regex
   \/ {
        enter(REGEX);
        return LexerTokens.REGEX;
   }

   "of"  {
          if(parentStateIs(BEFORE_OF)){
             pushbackall();
             leave();
          } else {
              return LexerTokens.SYMBOL;
          }
          }

   [.]  { return LexerTokens.DOT; }
   [,]  { return LexerTokens.COMMA; }
   [+]  { return LexerTokens.PLUS; }
   [-]  { return LexerTokens.MINUS; }
   [:]  { return LexerTokens.COLON; }
   [;]  { return LexerTokens.SEMICOLON; }
   [-] / "-}}"  { return LexerTokens.MINUS; }
   "true"|"false"  { return LexerTokens.BOOLEAN; }
   "new" { return LexerTokens.NEW; }
   "instanceof" { return LexerTokens.INSTANCEOF; }


    \{ {
        incObjDepth();
        return LexerTokens.BRACE;
    }

    \}/\} {

          if (currentDepth().getFirst() < 1) {
              pushbackall();
              leave();
          } else {
              decObjDepth();
            return LexerTokens.BRACE;
          }
    }

    \}/{OWS}[^}] {
        decObjDepth();
        return LexerTokens.BRACE;
    }

    \[ {
            incArrDepth();
            return LexerTokens.BRACKET;
        }


    \] {
            decArrDepth();
            return LexerTokens.BRACKET;
        }

    \( {
            incParDepth();
            return LexerTokens.PARENTHESIS;
        }


    \) {
            decParDepth();
            return LexerTokens.PARENTHESIS;
        }



   "|>" {
        pushbackall();
        leave();
    }

   ([0-9][0-9_]?)*[0-9]+ {return LexerTokens.NUMBER;}

   "break"|"case"|"catch"|"class"|"const"|"continue"|"debugger"|"default"|"delete"|"do"|"else"|"enum"|"export"|"extends"|"finally"|"for"|"function"|"if"|"implements"|"import"|"in"|"interface"|"let"|"package"|"private"|"protected"|"public"|"return"|"super"|"switch"|"static"|"this"|"throw"|"try"|"typeof"|"var"|"void"|"while"|"with"|"yield" { return LexerTokens.UNKNOWN; }


   [^\/\"'`()\[\]{}+\-:;,. \t\n\r]+ { return LexerTokens.SYMBOL; }

   "}}"|"{{" {
       yypushback(yylength());
       leave();
   }

   -/"}}" {
        if (currentDepth().getFirst() > 0) {
            return LexerTokens.MINUS;
        } else {
            yypushback(yylength());
            leave();
        }
   }

   <<EOF>> { leave(); }

   [^] {

       leave();
       return LexerTokens.UNKNOWN;
   }

}

<REGEX> {
    \/ { leave(); return LexerTokens.REGEX; }
    [\\] { pushbackall(); enter(REGEX_ESCAPE); }
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



