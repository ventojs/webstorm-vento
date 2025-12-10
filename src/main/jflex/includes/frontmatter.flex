// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state FRONTMATTER
%state FMLINE
%state FMVALUE

FMBLOCK = "---"[ \t\f]*([\r\n]|[\n])

// BLOCK 2 - END
%%

<YYINITIAL> {

    "---" { yybegin(FRONTMATTER); return LexerTokens.FRONTMATTER_OPEN;}

}

<FRONTMATTER> {
    {WHITESPACE} {  }

    ^{FMBLOCK} { yybegin(YYINITIAL); return LexerTokens.FRONTMATTER_CLOSE; }

    ^{SYMBOL}{OWS}[:] { pushbackall(); yybegin(FMLINE);}
    ^"  -"{OWS} { pushbackall(); yybegin(FMLINE); }
    ^"#"[^\r\n]*/[\r\n] { pushbackall(); yybegin(FMLINE); }

}

<FMLINE> {
    [ \t] {  }
    {SYMBOL}/{OWS}[:] { return LexerTokens.FRONTMATTER_KEY; }
    [:] { enter( FMVALUE); return LexerTokens.COLON; }
    "  -"{OWS}{SYMBOL} { return LexerTokens.FRONTMATTER_FLAG; }
    "#"[^\r\n]*/[\r\n] { return LexerTokens.COMMENT_CONTENT; }
    [\r\n] { yybegin(FRONTMATTER); }

}

<FMVALUE> {
    [ \t] { }
    [\"'`]~[\"'`] { pushbackall(); enter(STRING); }
    [^ \t\"'`\r\n][^\"'`\r\n]+  { return LexerTokens.FRONTMATTER_VALUE;}
    [\r\n] { pushbackall(); yybegin(FMLINE); }
}


< FRONTMATTER, FMLINE, FMVALUE > {

    <<EOF>> { yybegin(YYINITIAL); }

    [^] {
          yybegin(YYINITIAL);
          return LexerTokens.UNKNOWN;
      }
}
