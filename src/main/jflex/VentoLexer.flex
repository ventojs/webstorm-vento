/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.VentoParserTypes;
import org.js.vento.plugin.lexer.VentoLexerTypes;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%public
%class VentoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode



%state COMMENT
%state SCRIPT_CONTENT

%{
    // Ensure we handle EOF properly
    private boolean atEof = false;
%}


CLOSE_COMMENT_PHRASE = -?#}}
CLOSE_JAVASCRIPT = }}
DEFAULT_HTML = [^{]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
FOR_KEY = "for"
OPEN_COMMENT_PHRASE = \{\{#-?
OPEN_JAVASCRIPT = \{\{>
OPEN_VENTO_BLOCK = \{\{-?
WHITESPACE = [ \t\r\n]+

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


    {OPEN_COMMENT_PHRASE}    {
            yybegin(COMMENT);
            return VentoLexerTypes.OPEN_COMMENT_CLAUSE;
    }

    {OPEN_JAVASCRIPT}    {
            yybegin(SCRIPT_CONTENT);
            return VentoLexerTypes.JAVASCRIPT_START;
    }

    {OPEN_VENTO_BLOCK}    {
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

<JS_STRING_DOUBLE_QOUTE,JS_STRING_SINGLE_QUOTE,JS_STRING_BACK_TICK,JS_REGEX,BRACKET,JSON_STRING,JS_OBJECT> [^] { return VentoLexerTypes.ERROR; }

<SCRIPT_CONTENT> {

   ([^}]|"}"[^}])+ { return VentoParserTypes.JAVASCRIPT_ELEMENT; }
   {CLOSE_JAVASCRIPT} {
            yybegin(YYINITIAL);
            return VentoLexerTypes.JAVASCRIPT_END;
   }

}

<COMMENT> {

    // Match everything that is not the start of a closing comment sequence
    ([^#-]|"#"[^}]|"-"[^#])+ { return VentoLexerTypes.COMMENTED_CONTENT; }

    // Handle single characters that might be part of closing sequences
    "#" { return VentoLexerTypes.COMMENTED_CONTENT; }
    "-" { return VentoLexerTypes.COMMENTED_CONTENT; }

    {CLOSE_COMMENT_PHRASE} {
            yybegin(YYINITIAL);
            return VentoLexerTypes.CLOSE_COMMENT_CLAUSE;
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
