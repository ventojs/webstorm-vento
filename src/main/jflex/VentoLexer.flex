/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.VentoTypes;
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
%state COMMENTED_CONTENT
%state SCRIPT_CONTENT
%state FRONT_MATTER_STATE
%state TEMPLATE_SWITCH

WHITESPACE = [ \t\r\n]+
COMMENT_START = \{\{#
TRIMMED_COMMENT_START = \{\{#-
JAVASCRIPT_START = \{\{

%{
  private void yyclose() throws java.io.IOException {
    if (zzReader != null) {
      zzReader.close();
    }
  }
%}


%%

<YYINITIAL> {

    {WHITESPACE}              { /* Skip whitespace */ }

    (\r?\n)?\{\{     { yybegin(SCRIPT_CONTENT);    return VentoTypes.JAVASCRIPT_START; }
    {JAVASCRIPT_START}    {
        yybegin(SCRIPT_CONTENT);
        return VentoTypes.JAVASCRIPT_START;
    }

    (\r?\n)?\{\{    { yybegin(COMMENTED_CONTENT); return VentoTypes.COMMENTED_START; }
    {COMMENT_START}    {
        yybegin(COMMENTED_CONTENT);
        return VentoTypes.COMMENTED_START;
    }

    (\r?\n)?\{\{#    { yybegin(COMMENTED_CONTENT); return VentoTypes.TRIMMED_COMMENTED_START; }
    {TRIMMED_COMMENT_START}    {
        yybegin(COMMENTED_CONTENT);
        return VentoTypes.TRIMMED_COMMENTED_START;
    }


}

<SCRIPT_CONTENT> {
   ([^}]|"}"[^}])+ { return VentoTypes.JAVASCRIPT_ELEMENT; }
   "}}" {
       yybegin(YYINITIAL);
       return VentoTypes.JAVASCRIPT_END;
   }
}

<COMMENTED_CONTENT> {

    [^-#{]+ { return VentoTypes.COMMENTED_CONTENT; }

    "#}}" {
        yybegin(YYINITIAL);
        return VentoTypes.COMMENTED_END;
    }

    "-#}}" {
        yybegin(YYINITIAL);
        return VentoTypes.TRIMMED_COMMENTED_END;
    }
}

[^] { return VentoTypes.ERROR; }
