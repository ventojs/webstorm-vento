/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.ParserTypes;
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


DEFAULT_HTML = [^{]+
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

%{
  private int objectDepth = 0;
  private boolean value = false;
  private boolean collection = false;
  private IElementType closeType = null;

%}

%%

<YYINITIAL> {

    {EMPTY_LINE} { return LexerTypes.EMPTY_LINE; }
    {WHITESPACE} { return WHITE_SPACE; }

    {OBLOCK} {
        yypushback(2);
        yybegin(BLOCK);
        // TODO: consider adding a Vento block token
    }

    {DEFAULT_HTML} { return ParserTypes.HTML_ELEMENT; }

    [^]   { return LexerTypes.ERROR; }

}

<BLOCK> {
    {WHITESPACE} { }

    {OBLOCK}{OWS}[/]/{OWS}{CBLOCK} {
        yypushback(yylength()-2);
        closeType = LexerTypes.VARIABLE_END;
        return LexerTypes.VARIABLE_START;
    }

    {OBLOCK}{WHITESPACE}{IMPORT} {
        yybegin(IMPORT);
        yypushback(yylength()-2);
        closeType = LexerTypes.IMPORT_END;
        return LexerTypes.IMPORT_START;
    }

    {OBLOCK}{OWS}{EXPORT}{OWS}{FUNCTION} {
        yybegin(EXPORT_FUNCTION_BLOCK);
        yypushback(yylength()-2);
        closeType = LexerTypes.EXPORT_FUNCTION_END;
        return LexerTypes.EXPORT_FUNCTION_START;
    }

    {OBLOCK}{OWS}{EXPORT} {
        yybegin(EXPORT);
        yypushback(yylength()-2);
        closeType = LexerTypes.EXPORT_END;
        return LexerTypes.EXPORT_START;
    }

    {OBLOCK}{OWS}[/]{EXPORT}{OWS}{CBLOCK} {
        yybegin(EXPORT_CLOSE);
        yypushback(yylength()-2);
        closeType = LexerTypes.EXPORT_CLOSE_END;
        return LexerTypes.EXPORT_CLOSE_START;
    }

    {CBLOCK} {
        yybegin(YYINITIAL);
        IElementType ct = closeType;
        closeType = null;
        if(ct != null){
           return ct;
        } else {
           return LexerTypes.ERROR;
        }
    }

    {OCOMMENT} {
        yybegin(COMMENT);
        return LexerTypes.COMMENT_START;
    }

    {OJS} {
        yybegin(SCRIPT_CONTENT);
        return LexerTypes.JAVASCRIPT_START;
    }

    {OVAR} {
        yybegin(VARIABLE_CONTENT);
        return LexerTypes.VARIABLE_START;
    }

    \{\{ / .*[/]?{FOR_KEY} {
        yybegin(FOR_CONTENT);
        return LexerTypes.FOR_START;
    }

    [^] { return LexerTypes.ERROR; }

}

<SCRIPT_CONTENT> {

   ([^}]|"}"[^}])+ { return ParserTypes.JAVASCRIPT_ELEMENT; }
   {CBLOCK} {
           yybegin(YYINITIAL);
           return LexerTypes.JAVASCRIPT_END;
      }

   [^] {
          yybegin(YYINITIAL);
          return LexerTypes.ERROR;
      }

}

<COMMENT> {

    // Match everything that is not the start of a closing comment sequence
    ([^#-]|"#"[^}]|"-"[^#])+ { return LexerTypes.COMMENT_CONTENT; }

    // Handle single characters that might be part of closing sequences
    "#" { return LexerTypes.COMMENT_CONTENT; }
    "-" { return LexerTypes.COMMENT_CONTENT; }

    {CCOMMENT} {
            yybegin(YYINITIAL);
            return LexerTypes.COMMENT_END;
        }

}

%include includes/tokens-for.flex
%include includes/tokens-variables.flex
%include includes/tokens-import.flex
%include includes/tokens-export.flex
%include includes/tokens-pipe.flex
// improved pipe implementation
%include includes/tokens-new-pipe.flex
%include includes/tokens-expression.flex

