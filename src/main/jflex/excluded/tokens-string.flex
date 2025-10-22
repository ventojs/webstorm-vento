// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%
// BLOCK 2 - START
%state STRING
%state STRING_BKTK
%state STRING_DUBL
%state STRING_SNGL


/* ---------- Macros ---------- */
/* Full set of Unicode line terminators; JS classic strings may NOT contain these raw */
NL = \R

/* Backslash escapes (common + hex + unicode + code point + line continuation) */
EscCommon = \\\\
EscLineCont = \\\r?\n | \\\u2028 | \\\u2029
EscSimple   = \\[\'\"\\`bfnrtv0]
EscHex      = \\x[0-9a-fA-F]{2}
EscUnicode4 = \\u[0-9a-fA-F]{4}
EscUnicodeP = \\u\{[0-9a-fA-F]{1,6}\}
EscapeSeq   = {EscCommon}|{EscLineCont}|{EscSimple}|{EscHex}|{EscUnicode4}|{EscUnicodeP}

/* Inside classic strings: allow escapes but not raw terminators */
SQChar = [^\\'\r\n\u2028\u2029] | {EscapeSeq}
DQChar = [^\\\"\r\n\u2028\u2029] | {EscapeSeq}

/* Template literal text chunk:
   - includes escapes
   - excludes ` and ${ (handled by explicit rules)
   - DOES allow raw newlines (JS feature) */
TplChunk = ([^\\`\$\r\n\u2028\u2029] | {EscapeSeq})+
TplEOL   = {NL}

// BLOCK 2 - END
%%

<STRING> {

    \" {
          enter(STRING_DUBL);
          return LexerTokens.STRING;
      }

    \' {
          enter(STRING_SNGL);
          return LexerTokens.STRING;
      }

    \` {
          enter(STRING_BKTK);
          return LexerTokens.STRING;
      }
}

<STRING_DUBL> {

    {DQChar}+  { return LexerTokens.STRING;}
    \\\"       { return LexerTokens.STRING; }
    \"         { leave();leave(); return LexerTokens.STRING; }
    {NL}       { leave(); leave(); return LexerTokens.UNKNOWN; }
    .          { return LexerTokens.STRING;}

}

<STRING_SNGL> {

    {SQChar}+  { return LexerTokens.STRING;}
    \\'        { return LexerTokens.STRING; }
    '          { leave();leave(); return LexerTokens.STRING; }
    {NL}       { leave(); leave(); return LexerTokens.UNKNOWN; }
    .          { return LexerTokens.STRING;}

}

<STRING_BKTK> {

    {TplChunk}+  { return LexerTokens.STRING;}
    {TplEOL}+  { return LexerTokens.STRING;}
    \\`        { return LexerTokens.STRING; }
    `          { leave();leave(); return LexerTokens.STRING; }
    {NL}       { leave(); leave(); return LexerTokens.UNKNOWN; }
    .          { return LexerTokens.STRING;}

}


<STRING,STRING_DUBL,STRING_SNGL,STRING_BKTK> {
    [^] { return LexerTokens.UNKNOWN; }

    <<EOF>> {
            leave();
            return LexerTokens.UNKNOWN;
        }
}
