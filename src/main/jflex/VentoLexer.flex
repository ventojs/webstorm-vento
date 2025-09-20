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



%state MACRO_START
%state VENTO_ELEMENT_STATE
%state PURE_JS
%state COMMENT
%state SCRIPT_CONTENT
%state FRONT_MATTER_STATE
%state TEMPLATE_SWITCH
%state VARIABLE_CONTENT
%state EOF

%{
    // Ensure we handle EOF properly
    private boolean atEof = false;
%}


WHITESPACE = [ \t\r\n]+
OPEN_COMMENT_PHRASE = \{\{#-?
CLOSE_COMMENT_PHRASE = -?#}}
JAVASCRIPT_START = \{\{>
VARIABLE_START = \{\{
HTML_TAG = <[/!]?[a-zA-Z][a-zA-Z0-9\-_]*(\s+[a-zA-Z\-_][a-zA-Z0-9\-_]*(\s*=\s*("[^"]*"|'[^']*'|[^"'<>\/\s]+))?)*\s*\/?>
TEXT=[^<{]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)

%{
  private void yyclose() throws java.io.IOException {
    if (zzReader != null) {
      zzReader.close();
    }
  }
%}


%%

<YYINITIAL> {

    {EMPTY_LINE}              { return VentoLexerTypes.EMPTY_LINE; }
    {WHITESPACE}              { return com.intellij.psi.TokenType.WHITE_SPACE; }
    {HTML_TAG}                { return VentoLexerTypes.HTML_TAG; }
    {TEXT}                    { return VentoLexerTypes.TEXT; }


    {OPEN_COMMENT_PHRASE}    {
            yybegin(COMMENT);
            return VentoLexerTypes.OPEN_COMMENT_CLAUSE;
         }

    {JAVASCRIPT_START}    {
        yybegin(SCRIPT_CONTENT);
        return VentoLexerTypes.JAVASCRIPT_START;
    }

    {VARIABLE_START}    {
        yybegin(VARIABLE_CONTENT);
        return VentoLexerTypes.VARIABLE_START;
    }

    [^] { return VentoLexerTypes.ERROR; }

}

<VARIABLE_CONTENT> {
    \|\| {return VentoParserTypes.VARIABLE_PIPES;}
    ([^}\|]|"}"[^}\|])+ { return VentoLexerTypes.VARIABLE_ELEMENT; }
    "}}" {
           yybegin(YYINITIAL);
           return VentoLexerTypes.VARIABLE_END;
        }
}


<SCRIPT_CONTENT> {
   ([^}]|"}"[^}])+ { return VentoParserTypes.JAVASCRIPT_ELEMENT; }
   "}}" {
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
