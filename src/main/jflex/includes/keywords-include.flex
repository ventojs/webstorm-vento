// BLOCK 1 - START
import org.bouncycastle.gpg.SExpression;import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state INCLUDE

INCLUDE = "include"

// BLOCK 2 - END
%%


<INCLUDE> {

    {WHITESPACE} {  }

    \"|\' {pushbackall(); enter(FILE); }

    \` {pushbackall(); enter(STRING); }

    \{ { pushbackall(); enter(EXPRESSION); }

    {PIPE} { enter(EXPRESSION); return LexerTokens.PIPE; }

    {CBLOCK} { pushbackall(); leave(); }

    {OBLOCK} { pushbackall(); leave(); }

    {SYMBOL} {pushbackall(); enter(EXPRESSION);}

}

