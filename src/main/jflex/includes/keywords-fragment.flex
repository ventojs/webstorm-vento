// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state FRAGMENT

FRAGMENT = "fragment"

// BLOCK 2 - END
%%

<KEYWORDS> {FRAGMENT} { enter(FRAGMENT); return LexerTokens.FRAGMENT_KEY; }

<KEYWORDS_CLOSE> "/"{FRAGMENT} { return LexerTokens.FRAGMENT_CLOSE_KEY;}


<FRAGMENT> {

    {WHITESPACE} {  }

    {SYMBOL} { leave(); return LexerTokens.SYMBOL; }

}
