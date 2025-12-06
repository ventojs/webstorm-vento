// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FRONTMATTER
%state FMLINE
%state FMVALUE

// BLOCK 2 - END
%%

<YYINITIAL> {

    {FMBLOCK} { pushbackall(); enter(FRONTMATTER); }

}

<FRONTMATTER> {
    {WHITESPACE} {  }

    {SYMBOL}{OWS}[:] { pushbackall(); enter(FMLINE);}
    "  -"{OWS} { pushbackall(); enter(FMLINE); }
    "#"[^\r\n]*/[\r\n] { pushbackall(); enter(FMLINE); }

    {FMBLOCK} {
       fmcount++;
       if (fmcount == 2){
            fmcount=0;
            leave();
            return LexerTokens.FRONTMATTER_CLOSE;
       }
       return LexerTokens.FRONTMATTER_OPEN;

    }

    [^]   { pushbackall(); enter(HTML); }

}

<FMLINE> {
    [ \t] {  }
    {SYMBOL}/{OWS}[:] { return LexerTokens.FRONTMATTER_KEY; }
    [:] { enter( FMVALUE); return LexerTokens.COLON; }
    "  -"{OWS}{SYMBOL} { return LexerTokens.FRONTMATTER_FLAG; }
    "#"[^\r\n]*/[\r\n] { return LexerTokens.COMMENT_CONTENT; }
    [\r\n] { leave(); }

}

<FMVALUE> {
    [ \t] { }
    [\"'`]~[\"'`] { pushbackall(); enter(STRING); }
    [^ \t\"'`\r\n][^\"'`\r\n]+  { return LexerTokens.FRONTMATTER_VALUE;}
    [\r\n] { pushbackall(); leave(); }
}


< FMLINE, FMVALUE > {

    <<EOF>> { leave(); }

    [^] {
          leave();
          return LexerTokens.UNKNOWN;
      }
}
