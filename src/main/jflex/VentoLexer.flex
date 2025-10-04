/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.VentoParserTypes;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%public
%class VentoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
//%debug



%state COMMENT
%state SCRIPT_CONTENT

%{
    // Ensure we handle EOF properly
    private boolean atEof = false;
%}

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


DEFAULT_HTML = [^{]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
WHITESPACE = [ \t\r\n]+

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
FROM = "from"

%{
  private int objectDepth = 0;
  private boolean value = false;
  private boolean collection = false;

%}


%%

<YYINITIAL> {

    {EMPTY_LINE}              { return VentoLexerTypes.EMPTY_LINE; }
    {WHITESPACE}              { return WHITE_SPACE; }
    {DEFAULT_HTML}            { return VentoParserTypes.HTML_ELEMENT; }


    {OBLOCK}[ \t]+{IMPORT}    {
            yybegin(IMPORT);
            return VentoLexerTypes.IMPORT_START;
    }

    {OCOMMENT}    {
            yybegin(COMMENT);
            return VentoLexerTypes.COMMENT_START;
    }

    {OJS}    {
            yybegin(SCRIPT_CONTENT);
            return VentoLexerTypes.JAVASCRIPT_START;
    }

    {OVAR}    {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_START;
    }

    \{\{ / [ \t]?"/fr"   {
            yybegin(FOR_CONTENT);
            return VentoLexerTypes.FOR_START;
    }

    \{\{ / .*[/]?{FOR_KEY}    {
            yybegin(FOR_CONTENT);
            return VentoLexerTypes.FOR_START;
    }

    [^] {
          //System.out.println("YYINITIAL error");
          return VentoLexerTypes.ERROR;
      }

}

%include includes/tokens-for.flex
%include includes/tokens-variables.flex
%include includes/tokens-import-export.flex



<SCRIPT_CONTENT> {

   ([^}]|"}"[^}])+ { return VentoParserTypes.JAVASCRIPT_ELEMENT; }
   {CBLOCK} {
            yybegin(YYINITIAL);
            return VentoLexerTypes.JAVASCRIPT_END;
   }

}

<COMMENT> {

    // Match everything that is not the start of a closing comment sequence
    ([^#-]|"#"[^}]|"-"[^#])+ { return VentoLexerTypes.COMMENT_CONTENT; }

    // Handle single characters that might be part of closing sequences
    "#" { return VentoLexerTypes.COMMENT_CONTENT; }
    "-" { return VentoLexerTypes.COMMENT_CONTENT; }

    {CCOMMENT} {
                yybegin(YYINITIAL);
                return VentoLexerTypes.COMMENT_END;
        }


    [^] {
        yybegin(YYINITIAL);
        yypushback(yylength());
        return VentoLexerTypes.ERROR;
    }

}

// CRITICAL: Handle EOF explicitly
<<EOF>>             {
    if (!atEof) {
        atEof = true;
        return null;
    }
    return null;
}
