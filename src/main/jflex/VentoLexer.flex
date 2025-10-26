/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.ParserElements;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%public
%class VentoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%char


// STATES
%state COMMENT
%state SCRIPT_CONTENT
%state BLOCK
%state BLOCK_CONTENT
%state UNKNOWN


%{
  private final java.util.ArrayDeque<Integer> stateStack = new java.util.ArrayDeque<>();
  private static final boolean DEBUG = false;

  // Map state ids to readable names. JFlex generates int constants named like YYINITIAL, BLOCK, etc.
  private static final java.util.Map<Integer, String> STATE_NAMES = new java.util.HashMap<>();
  static {
    STATE_NAMES.put(YYINITIAL, "YYINITIAL");
    STATE_NAMES.put(BLOCK, "BLOCK");
    STATE_NAMES.put(COMMENT, "COMMENT");
    STATE_NAMES.put(EXPORT, "EXPORT");
    STATE_NAMES.put(EXPRESSION, "EXPRESSION");
    STATE_NAMES.put(FILE, "FILE");
    STATE_NAMES.put(FOR, "FOR");
    STATE_NAMES.put(BEFORE_OF, "BEFORE_FOR");
    STATE_NAMES.put(FUNCTION, "FUNCTION");
    STATE_NAMES.put(IMPORT, "IMPORT");
    STATE_NAMES.put(KEYWORDS, "KEYWORDS");
    STATE_NAMES.put(KEYWORDS_CLOSE, "KEYWORDS_CLOSE");
    STATE_NAMES.put(NOKEYWORDS, "NOKEYWORDS");
    STATE_NAMES.put(SET, "SET");
    STATE_NAMES.put(SET_BLOCK_MODE, "SET_BLOCK_MODE");
    STATE_NAMES.put(SET_VALUE, "SET_VALUE");
    STATE_NAMES.put(OBJECT, "OBJECT");
    STATE_NAMES.put(ARRAY, "ARRAY");
    STATE_NAMES.put(SCRIPT_CONTENT, "SCRIPT_CONTENT");
    STATE_NAMES.put(STRING, "STRING");
    STATE_NAMES.put(STRING_DUBL, "STRING_DUBL");
    STATE_NAMES.put(STRING_SNGL, "STRING_SNGL");
    STATE_NAMES.put(STRING_BKTK, "STRING_BKTK");
//    BLOCK, KEYWORDS, EXPORT, EXPRESSION, FUNCTION, KEYWORDS_CLOSE
    // Add included states too (from include files) once compiled in:
    // e.g. STATE_NAMES.put(KEYWORDS, "KEYWORDS"); STATE_NAMES.put(EXPORT, "EXPORT"); STATE_NAMES.put(EXPRESSION, "EXPRESSION"); etc.
  }

  private String stName(int s) {
    String n = STATE_NAMES.get(s);
    return n != null ? n : ("STATE#" + s);
  }

  /** Enter 's', remembering where we came from (the caller). */
  private void enter(int s) {
    int caller = yystate();            // <-- push the *current* state
    stateStack.push(caller);
    if (DEBUG) System.out.println((stName(caller) + " -> " + stName(s) + " ("+yytext()+"):"+remaining()).indent(stateStack.size()));
    yybegin(s);
  }

  /** Return to the caller state saved by the most recent enter(). */
  private void leave() {
    if (stateStack.isEmpty())
      throw new IllegalStateException("leave() with empty state stack");
    int caller = stateStack.pop();
    if (DEBUG) System.out.println((stName(caller)+" <- " + stName(yystate()) + " ("+yytext()+"):"+remaining()).indent(stateStack.size()+1));
    yybegin(caller);
  }

  private String remaining() {
    // from current position to end of input
    return "\n"+zzBuffer.subSequence( zzCurrentPos, zzEndRead).toString()+"\n";
  }
  /** Optional: hard jump (not LIFO) if you need to abort nested states. */
  private void resetAt(int s) {
    stateStack.clear();
    if (DEBUG) System.out.println("resetAt -> " + stName(s));
    yybegin(s);
  }

  private void debug(){ debug(null); }

  private void debug(String rule){
      if(rule!= null) System.out.println("DEBUG rule :" + rule);
      System.out.println("DEBUG state:" + stName(yystate())+" ["+stateStack.size()+"]");
      System.out.println("DEBUG pos  :" + zzCurrentPos + "; text:" + yytext() );
      System.out.println("------------------");
  }

  private void pushbackall(){
      yypushback(yylength());
  }

%}


DEFAULT_HTML = [^]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
WHITESPACE = [ \t\r\n]+
OWS = [ \t\r\n]*
STRING = [\"][^\"\n\r]*[\"]
NUMBER = [0-9_]+
BOOLEAN = "true"|"false"

EQUAL = [=]
SYMBOL = [a-zA-Z_$]+[a-zA-Z_$0-9]*
PIPE = "|>"

OBLOCK = "{{"
CBLOCK = "}}"

OCOMMENT = {OBLOCK}#-?
CCOMMENT = -?#{CBLOCK}

OJS = {OBLOCK}>
OVAR = {OBLOCK}-?
CVAR = -?{CBLOCK}

%{
  private int objectDepth = 0;
  private boolean value = false;
  private boolean collection = false;
  private IElementType closeType = null;

%}

%{
  // Accumulator for template chunks
  private final StringBuilder sb = new StringBuilder();

  // Brace depth for nested blocks inside ${ ... }
  private int tplBraceDepth = 0;

  // --- IntelliJ skeleton fields (populated by generated code) ---
  /** The token start offset (generated skeleton uses zzStartRead). */
  private int tokenStart = 0;
  /** The token end offset (generated skeleton uses zzMarkedPos). */
  private int tokenEnd = 0;
  /** Current token type — the skeleton expects us to return it from actions. */
  private IElementType myTokenType;

  public IElementType getTokenType() { return myTokenType; }



  // Small helpers so actions read nicer
  private IElementType t(IElementType tt) { myTokenType = tt; return tt; }
%}

%%

<YYINITIAL> {

    {WHITESPACE} { return WHITE_SPACE; }

    {OBLOCK} {
        enter(BLOCK);
        return LexerTokens.VBLOCK_OPEN;
    }

    ([^\{][^\{]?)+ { return LexerTokens.HTML; }

    [^]   { return LexerTokens.HTML; }


}

//<FRONTMATTER> {
//
//}

<BLOCK> {
    {WHITESPACE} { }
    {CBLOCK} { leave(); return LexerTokens.VBLOCK_CLOSE;}
    {KEYWORDS} { pushbackall(); enter(KEYWORDS); }
    {CLOSING_KEYWORDS} { pushbackall(); enter(KEYWORDS_CLOSE); }

//    {COMMENT} {
//            yypushback(yylength());
//            enter(COMMENT);
//        }



}

<UNKNOWN> {

    [^}] { return LexerTokens.UNKNOWN;}
    "}}" { pushbackall(); leave();}

}




%include includes/keywords.flex
%include includes/keywords-export.flex
%include includes/keywords-for.flex
%include includes/keywords-import.flex
%include includes/keywords-set.flex

%include includes/no-keywords.flex

%include includes/general-expression.flex
%include includes/general-pipe.flex
%include includes/general-function.flex
%include includes/general-file.flex
%include includes/general-object.flex
%include includes/general-array.flex
%include includes/general-string.flex

<BLOCK> {
        <<EOF>> {
            //debug("<BLOCK> <<EOF>>");
            leave();
            return LexerTokens.UNKNOWN;
        }
        [^] {
            enter(UNKNOWN);
            return LexerTokens.UNKNOWN;
        }
        {OBLOCK} {
                return LexerTokens.UNKNOWN;
            }
    }

< EXPORT, EXPRESSION, FILE, FOR, FUNCTION, IMPORT, KEYWORDS, KEYWORDS_CLOSE, NOKEYWORDS, SET, SET_BLOCK_MODE, SET_VALUE, EXPRESSION,  ARRAY > {
        "}}"|"{{" {
            //debug("\"}}\"|\"{{\"");
            yypushback(yylength());
            leave();
        }
        <<EOF>> { leave(); }
        [^] {
            leave();
            return LexerTokens.UNKNOWN;
        }
    }

<BEFORE_OF> {
        <<EOF>> { leave(); }
        [^] {
            //debug("*: "+yytext());
            yypushback(yylength());
            leave();
        }
    }


<OBJECT> {
        "{{" {

            yypushback(yylength());
            leave();
        }

        [^] {
            leave();
            return LexerTokens.UNKNOWN;
        }

        <<EOF>> { leave(); }
    }
