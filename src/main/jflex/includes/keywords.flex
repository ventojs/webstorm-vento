// BLOCK 1 - START
import org.js.vento.plugin.lexer.LexerTokens;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state KEYWORDS
%state KEYWORDS_CLOSE
%state ECHO
%state ELSE
%state ELSEIF
%state EXPORT
%state IMPORT
%state FOR
%state FUNCTION
%state IF
%state IMPORT
%state INCLUDE
%state LAYOUT
%state SET
%state SLOT

ECHO = "echo"
ELSE = "else"
ELSEIF = "elseif"
EXPORT = "export"
FOR = "for"
FUNCTION = "function"
IF = "if"
IMPORT = "import"
INCLUDE = "include"
LAYOUT = "layout"
SET = "set"
SLOT = "slot"

KEYWORDS =  {ECHO}|{ELSE}|{ELSEIF}|{EXPORT}|{FOR}|{ASYNC}{WHITESPACE}{FUNCTION}|{FUNCTION}|{IF}|{IMPORT}|{INCLUDE}|{LAYOUT}|{SET}|{SLOT}
CLOSING_KEYWORDS = "/"{ECHO}|"/"{EXPORT}|"/"{FOR}|"/"{FUNCTION} |"/"{IF} |"/"{LAYOUT} |"/"{SET} |"/"{SLOT}

// BLOCK 2 - END
%%


<KEYWORDS> {

    {WHITESPACE} {  }

    {CBLOCK} { pushbackall(); leave(); }
    {ECHO} { enter(ECHO); return LexerTokens.ECHO_KEY; }
    {EXPORT} { enter(EXPORT); return LexerTokens.EXPORT_KEY; }
    {FOR} / {WHITESPACE} { enter(FOR); return LexerTokens.FOR_KEY; }
    {ASYNC}{WHITESPACE}{FUNCTION} { debugMsg("af"); pushbackall(); enter(FUNCTION);  }
    {FUNCTION} { pushbackall(); enter(FUNCTION);  }
    {IMPORT} { enter(IMPORT); return LexerTokens.IMPORT_KEY; }
    {INCLUDE} { enter(INCLUDE); return LexerTokens.INCLUDE_KEY; }
    {LAYOUT} {enter(LAYOUT); return LexerTokens.LAYOUT_KEY; }
    {SET} { enter(SET); return LexerTokens.SET_KEY; }
    {SLOT} { enter(SLOT); return LexerTokens.LAYOUT_SLOT_KEY; }
//    {ELSEIF} { enter(LAYOUT); return LexerTokens.ELSEIF_KEY; }
//    {ELSE} { enter(LAYOUT); return LexerTokens.ELSE_KEY; }
//    {IF} { enter(IF); return LexerTokens.IF_KEY; }


}

<KEYWORDS_CLOSE> {
    {WHITESPACE} {  }
    "/"{ECHO} { return LexerTokens.ECHO_CLOSE_KEY;}
    "/"{EXPORT} { return LexerTokens.EXPORT_CLOSE_KEY;}
    "/"{FOR} { return LexerTokens.FOR_CLOSE_KEY;}
    "/"{FUNCTION} { return LexerTokens.FUNCTION_CLOSE_KEY;}
    "/"{LAYOUT} { return LexerTokens.LAYOUT_CLOSE_KEY;}
    "/"{SET} { return LexerTokens.SET_CLOSE_KEY;}
    "/"{SLOT} { return LexerTokens.LAYOUT_SLOT_CLOSE_KEY;}
    {CBLOCK}  {
          yypushback(yylength());
          leave();
      }
}


