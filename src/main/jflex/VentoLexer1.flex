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
//%debug


// STATES
%state COMMENT
%state SCRIPT_CONTENT
%state BLOCK


%{
  private final java.util.ArrayDeque<Integer> stateStack = new java.util.ArrayDeque<>();
  private static final boolean DEBUG = false;

  /** Enter 's', remembering where we came from (the caller). */
  private void enter(int s) {
    int caller = yystate();            // <-- push the *current* state
    stateStack.push(caller);
    yybegin(s);
  }

  /** Return to the caller state saved by the most recent enter(). */
  private void leave() {
    if (stateStack.isEmpty())
      throw new IllegalStateException("leave() with empty state stack");
    int caller = stateStack.pop();
    if (DEBUG) System.out.println("leave -> " + caller);
    yybegin(caller);
  }

  /** Optional: hard jump (not LIFO) if you need to abort nested states. */
  private void resetAt(int s) {
    stateStack.clear();
    yybegin(s);
  }
%}


DEFAULT_HTML = [^]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
WHITESPACE = [ \t\r\n]+
OWS = [ \t\r\n]*
STRING = [\"][^\"\n\r]*[\"]

IDENT = [a-zA-Z_$]+[a-zA-Z_$0-9]*
PIPE = "|>"

OBLOCK = "{{"
CBLOCK = "}}"

OCOMMENT = {OBLOCK}#-?
CCOMMENT = -?#{CBLOCK}

OJS = {OBLOCK}>
OVAR = {OBLOCK}-?
CVAR = -?{CBLOCK}

FOR_KEY = "for"

IMPORT = "import"
EXPORT = "export"
FUNCTION = "function"
FROM = "from"
SET = "set"
LAYOUT = "layout"
SLOT = "slot"

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

    {EMPTY_LINE} { return LexerTokens.EMPTY_LINE; }
    {WHITESPACE} { return WHITE_SPACE; }

    {OBLOCK} {
        yypushback(2);
        yybegin(BLOCK);
        // TODO: consider adding a Vento block token
    }

    ([^\{][^\{]?)+ { return LexerTokens.HTML; }

    [^]   { return LexerTokens.UNKNOWN; }

}

<BLOCK> {
    {WHITESPACE} { }

    // TODO: there's a problem here
    {OBLOCK}{OWS}[/]/{OWS}{CBLOCK} {
            yypushback(yylength()-2);
            closeType = LexerTokens.VARIABLE_END;
            return LexerTokens.VARIABLE_START;
        }

    {OBLOCK}{WHITESPACE}[/]{LAYOUT} {
            enter(LAYOUT);
            yypushback(yylength()-2);
            closeType = LexerTokens.LAYOUT_CLOSE_END;
            return LexerTokens.LAYOUT_CLOSE_START;
        }

    {OBLOCK}{WHITESPACE}{LAYOUT} {
            enter(LAYOUT);
            yypushback(yylength()-2);
            closeType = LexerTokens.LAYOUT_END;
            return LexerTokens.LAYOUT_START;
        }

    {OBLOCK}{WHITESPACE}[/]{SLOT} {
            enter(SLOT);
            yypushback(yylength()-2);
            closeType = LexerTokens.LAYOUT_SLOT_CLOSE_END;
            return LexerTokens.LAYOUT_SLOT_CLOSE_START;
        }

    {OBLOCK}{WHITESPACE}{SLOT} {
            enter(SLOT);
            yypushback(yylength()-2);
            closeType = LexerTokens.LAYOUT_SLOT_END;
            return LexerTokens.LAYOUT_SLOT_START;
        }

    {OBLOCK}-{WHITESPACE}{SLOT} {
            enter(SLOT);
            yypushback(yylength()-3);
            closeType = LexerTokens.LAYOUT_SLOT_END;
            return LexerTokens.LAYOUT_SLOT_START;
        }

    {OBLOCK}{WHITESPACE}{IMPORT} {
            yybegin(IMPORT);
            yypushback(yylength()-2);
            closeType = LexerTokens.IMPORT_END;
            return LexerTokens.IMPORT_START;
        }

    {OBLOCK}/{OWS}[/]{SET} {
            enter(SET);
            yypushback(yylength()-2);
            closeType = LexerTokens.SET_CLOSE_END;
            return LexerTokens.SET_CLOSE_START;
        }

    {OBLOCK}{WHITESPACE}{SET} {
                enter(SET);
                yypushback(yylength()-2);
                closeType = LexerTokens.SET_END;
                return LexerTokens.SET_START;
        }

    {OBLOCK}/{OWS}{SET}{WHITESPACE}{IDENT} {
            enter(SET);
            yypushback(yylength()-2);
            closeType = LexerTokens.SET_END;
            return LexerTokens.SET_START;
        }

    {OBLOCK}{OWS}{EXPORT}{OWS}{FUNCTION} {
            yybegin(EXPORT_FUNCTION_BLOCK);
            yypushback(yylength()-2);
            closeType = LexerTokens.EXPORT_FUNCTION_END;
            return LexerTokens.EXPORT_FUNCTION_START;
        }

    {OBLOCK}{OWS}{EXPORT} {
            yybegin(EXPORT);
            yypushback(yylength()-2);
            closeType = LexerTokens.EXPORT_END;
            return LexerTokens.EXPORT_START;
        }

    {OBLOCK}{OWS}[/]{EXPORT}{OWS}{CBLOCK} {
            yybegin(EXPORT_CLOSE);
            yypushback(yylength()-2);
            closeType = LexerTokens.EXPORT_CLOSE_END;
            return LexerTokens.EXPORT_CLOSE_START;
        }

    -?{CBLOCK} {
            yybegin(YYINITIAL);
            IElementType ct = closeType;
            closeType = null;
            if(ct != null){
               return ct;
            } else {
               return LexerTokens.UNKNOWN;
            }
        }

    {OCOMMENT} {
            yybegin(COMMENT);
            return LexerTokens.COMMENT_START;
        }

    {OJS} {
            yybegin(SCRIPT_CONTENT);
            return LexerTokens.JAVASCRIPT_START;
        }

    {OVAR} {
            yybegin(VARIABLE_CONTENT);
            return LexerTokens.VARIABLE_START;
        }

    \{\{ / .*[/]?{FOR_KEY} {
            yybegin(FOR_CONTENT);
            return LexerTokens.FOR_START;
        }

    [^] { return LexerTokens.UNKNOWN; }

}

<SCRIPT_CONTENT> {

   ([^}]|"}"[^}])+ { return ParserElements.JAVASCRIPT_ELEMENT; }
   {CBLOCK} {
              yybegin(YYINITIAL);
              return LexerTokens.JAVASCRIPT_END;
         }

   [^] {
             yybegin(YYINITIAL);
             return LexerTokens.UNKNOWN;
         }

}

<COMMENT> {

    // Match everything that is not the start of a closing comment sequence
    ([^#-]|"#"[^}]|"-"[^#])+ { return LexerTokens.COMMENT_CONTENT; }

    // Handle single characters that might be part of closing sequences
    "#" { return LexerTokens.COMMENT_CONTENT; }
    "-" { return LexerTokens.COMMENT_CONTENT; }

    {CCOMMENT} {
                yybegin(YYINITIAL);
                return LexerTokens.COMMENT_END;
            }

}

%include includes/tokens-for.flex
%include includes/tokens-variables.flex
%include includes/tokens-import.flex
%include includes/tokens-export.flex
%include includes/tokens-pipe.flex
// improved pipe implementation
%include includes/tokens-pipe.flex
%include includes/tokens-expression.flex
%include includes/tokens-set.flex
%include includes/tokens-layout.flex
%include includes/tokens-file.flex
%include includes/tokens-objects.flex
%include includes/tokens-string.flex

<LAYOUT, SLOT, FILE, PIPE> {
    <<EOF>> {
            leave();
            return LexerTokens.UNKNOWN;
        }

    [^] {
        yypushback(yylength());
        leave();
    }
}

