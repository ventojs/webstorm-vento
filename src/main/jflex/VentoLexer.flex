/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

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
%state COMMENTED_CODE
%state FRONT_MATTER_STATE
%state TEMPLATE_SWITCH

/* Definitions of token patterns */
IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
NUMBER = [0-9]+(\.[0-9]+)?
STRING = \"([^\"\\]|\\.)*\"
WHITESPACE = [ \t\r\n]+
LINE_COMMENT   = "//".*
BLOCK_COMMENT  = "/*" [^*]* \*+ ([^/*] [^*]* \*+)* "*/"
COMMENT        = {LINE_COMMENT} | {BLOCK_COMMENT}
PURE_JS_START = \{\{>
COMMENTED_CODE_START = \{\{#
TRIMMED_COMMENTED_CODE_START = \{\{#-
FRONT_MATTER_START = \-\-\-
FRONT_MATTER_END = \-\-\-
TEMPLATE_TAG_START = \{\{(-)?
TEMPLATE_TAG_END = (-)?\}\}

/* keywords (list them explicitly; no \b, no \s) */
KEYWORD = "for" | "of" | "if" | "else" | "include" | "set" | "layout" | "echo" | "function" | "import" | "from" | "export" | "await"

%{
  // Add this method definition in the user code section
  private void yyclose() throws java.io.IOException {
    // Add any cleanup code you need here
    // For a basic lexer, this can often be empty
    if (zzReader != null) {
      zzReader.close();
    }
  }
%}


%%

<YYINITIAL> {
    {WHITESPACE}              { /* Skip whitespace */ }
    {COMMENTED_CODE_START}    {
              yybegin(COMMENTED_CODE);
              return VentoTypes.COMMENTED_CODE_START;
          }
    {TRIMMED_COMMENTED_CODE_START}    {
                yybegin(COMMENTED_CODE);
                return VentoTypes.TRIMMED_COMMENTED_CODE_START;
      }
}

<TEMPLATE_SWITCH> {
    "#" {
          yybegin(COMMENTED_CODE);
          return VentoTypes.COMMENTED_CODE_START;
      }
    ">" { }
}

<COMMENTED_CODE> {
    [^-#{]+ { return VentoTypes.COMMENTED_CODE_CONTENT; }
    "#}}" {
        yybegin(YYINITIAL);
        return VentoTypes.COMMENTED_CODE_END;
      }
    "-#}}" {
         yybegin(YYINITIAL);
         return VentoTypes.TRIMMED_COMMENTED_CODE_END;
      }
}

/* Fallback rule - this is crucial to prevent null returns */
[^]                           { return VentoTypes.ERROR; }
