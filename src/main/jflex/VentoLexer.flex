/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.webstormvento;

import org.js.vento.webstormvento.VentoTypes;
import java_cup.runtime.Symbol;
import jflex.core.sym;


%%

%class VentoLexer
%public
%unicode
%cup
%function next_token
%type java_cup.runtime.Symbol

%state MACRO_START
%state VENTO_ELEMENT_STATE
%state PURE_JS
%state COMMENTED_CODE
%state FRONT_MATTER_STATE

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

    /* Handle front matter */
    {FRONT_MATTER_START}      {
        yybegin(FRONT_MATTER_STATE);
        /* Capture the start of front matter */
    }

    /* Handle comments */
    {COMMENT}                 {
        return new Symbol(VentoTypes.COMMENT.getIndex(), yytext());
    }

    /* Handle commented Vento code */
    {COMMENTED_CODE_START}    {
        yybegin(COMMENTED_CODE);
        /* Capture the start of commented code */
    }

    /* Handle pure JavaScript code */
    {PURE_JS_START}           {
        yybegin(PURE_JS);
        return new Symbol(VentoTypes.PURE_JS_START.getIndex(), yytext());
    }

    /* Handle template tags */
    {TEMPLATE_TAG_START}      {
        yybegin(MACRO_START);
        /* Enter MACRO_START state when '{{' or '{{-' is encountered */
    }

    {KEYWORD}                 { return new Symbol(VentoTypes.KEYWORD.getIndex(), yytext()); }

    {IDENTIFIER}              { return new Symbol(VentoTypes.IDENTIFIER.getIndex(), yytext()); }
    {NUMBER}                  { return new Symbol(VentoTypes.NUMBER.getIndex(), yytext()); }
    {STRING}                  { return new Symbol(VentoTypes.STRING.getIndex(), yytext()); }

    /* Operators and punctuation */
    "="                       { return new Symbol(VentoTypes.EQUALS.getIndex()); }
    "+"                       { return new Symbol(VentoTypes.PLUS.getIndex()); }
    "-"                       { return new Symbol(VentoTypes.MINUS.getIndex()); }
    "*"                       { return new Symbol(VentoTypes.MULTIPLY.getIndex()); }
    "/"                       { return new Symbol(VentoTypes.DIVIDE.getIndex()); }
    ";"                       { return new Symbol(VentoTypes.SEMICOLON.getIndex()); }
    "{"                       { return new Symbol(VentoTypes.LBRACE.getIndex()); }
    "}"                       { return new Symbol(VentoTypes.RBRACE.getIndex()); }

    .                         { /* Handle any other character as error */
        return new Symbol(VentoTypes.ERROR.getIndex(), yytext());
    }
}

<MACRO_START> {
    ">"                        {
        yybegin(PURE_JS);
        return new Symbol(VentoTypes.PURE_JS_START.getIndex(), "{{>" );
    }

    "#"                        {
        yybegin(COMMENTED_CODE);
        return new Symbol(VentoTypes.COMMENTED_CODE_START.getIndex(), "{{#");
    }

    [a-zA-Z_-]                 {
        yybegin(VENTO_ELEMENT_STATE);
        /* Handle other template tags */
        /* You can return a specific token if needed */
    }
}

<VENTO_ELEMENT_STATE> {
    "}}"                       {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.TEMPLATE_TAG_END.getIndex(), "}}");
    }

    {KEYWORD}                 { return new Symbol(VentoTypes.KEYWORD.getIndex(), yytext()); }
    {IDENTIFIER}              { return new Symbol(VentoTypes.IDENTIFIER.getIndex(), yytext()); }
    {NUMBER}                  { return new Symbol(VentoTypes.NUMBER.getIndex(), yytext()); }
    {STRING}                  { return new Symbol(VentoTypes.STRING.getIndex(), yytext()); }

    /* Operators and punctuation */
    "="                       { return new Symbol(VentoTypes.EQUALS.getIndex()); }
    "+"                       { return new Symbol(VentoTypes.PLUS.getIndex()); }
    "-"                       { return new Symbol(VentoTypes.MINUS.getIndex()); }
    "*"                       { return new Symbol(VentoTypes.MULTIPLY.getIndex()); }
    "/"                       { return new Symbol(VentoTypes.DIVIDE.getIndex()); }
    ";"                       { return new Symbol(VentoTypes.SEMICOLON.getIndex()); }
    "{"                       { return new Symbol(VentoTypes.LBRACE.getIndex()); }
    "}"                       { return new Symbol(VentoTypes.RBRACE.getIndex()); }

    .                         { /* Handle any other character as error */
        return new Symbol(VentoTypes.ERROR.getIndex(), yytext());
    }
}

<PURE_JS> {
    "}}"                       {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.PURE_JS_END.getIndex(), "}}");
    }

    /* Consume all characters inside Pure JS block */
    .|\n                       { /* You can accumulate JavaScript content or tokenize further if needed */ }
}

<COMMENTED_CODE> {
    "#}}"                      {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.COMMENTED_CODE_END.getIndex(), "#}}");
    }

    /* Consume all characters inside commented code block */
    .|\n                       { /* Handle commented code content */ }
}

<FRONT_MATTER_STATE> {
    {FRONT_MATTER_END}         {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.FRONT_MATTER_END.getIndex(), "---");
    }

    /* Consume YAML front matter content */
    .|\n                       { /* You can process YAML content or tokenize further if needed */ }
}