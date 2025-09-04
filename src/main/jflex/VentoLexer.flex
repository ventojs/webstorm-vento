/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.webstormvento;

import org.js.vento.webstormvento.VentoTypes;
import java_cup.runtime.Symbol;

%%

%class VentoLexer
%public
%unicode
%implements com.intellij.lexer.FlexLexer
%function advance
%type com.intellij.psi.tree.IElementType


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

%%

<YYINITIAL> {
    {WHITESPACE}              { return com.intellij.psi.TokenType.WHITE_SPACE; }

    /* Handle front matter */
    {FRONT_MATTER_START}      {
        yybegin(FRONT_MATTER_STATE);
        /* Capture the start of front matter */
    }

    /* Handle comments */
    {COMMENT}                 {
        return VentoTypes.COMMENT;
    }

    /* Handle commented Vento code */
    {COMMENTED_CODE_START}    {
        yybegin(COMMENTED_CODE);
        /* Capture the start of commented code */
    }

    /* Handle pure JavaScript code */
    {PURE_JS_START}           {
        yybegin(PURE_JS);
        return VentoTypes.PURE_JS_START;
    }

    /* Handle template tags */
    {TEMPLATE_TAG_START}      {
        yybegin(MACRO_START);
        /* Enter MACRO_START state when '{{' or '{{-' is encountered */
    }

    {KEYWORD}                 { return VentoTypes.KEYWORD; }

    {IDENTIFIER}              { return VentoTypes.IDENTIFIER; }
    {NUMBER}                  { return VentoTypes.NUMBER; }
    {STRING}                  { return VentoTypes.STRING; }

    /* Operators and punctuation */
    "="                       { return VentoTypes.EQUALS; }
    "+"                       { return VentoTypes.PLUS; }
    "-"                       { return VentoTypes.MINUS; }
    "*"                       { return VentoTypes.MULTIPLY; }
    "/"                       { return VentoTypes.DIVIDE; }
    ";"                       { return VentoTypes.SEMICOLON; }
    "{"                       { return VentoTypes.LBRACE; }
    "}"                       { return VentoTypes.RBRACE; }

    .                         { /* Handle any other character as error */
        return VentoTypes.ERROR;
    }
}

<MACRO_START> {
    ">"                        {
        yybegin(PURE_JS);
        return VentoTypes.PURE_JS_START;
    }

    "#"                        {
        yybegin(COMMENTED_CODE);
        return VentoTypes.COMMENTED_CODE_START;
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
        return VentoTypes.TEMPLATE_TAG_END;
    }

    {KEYWORD}                 { return VentoTypes.KEYWORD; }
    {IDENTIFIER}              { return VentoTypes.IDENTIFIER; }
    {NUMBER}                  { return VentoTypes.NUMBER; }
    {STRING}                  { return VentoTypes.STRING; }

    /* Operators and punctuation */
    "="                       { return VentoTypes.EQUALS; }
    "+"                       { return VentoTypes.PLUS; }
    "-"                       { return VentoTypes.MINUS; }
    "*"                       { return VentoTypes.MULTIPLY; }
    "/"                       { return VentoTypes.DIVIDE; }
    ";"                       { return VentoTypes.SEMICOLON; }
    "{"                       { return VentoTypes.LBRACE; }
    "}"                       { return VentoTypes.RBRACE; }

    .                         { /* Handle any other character as error */
        return VentoTypes.ERROR;
    }
}

<PURE_JS> {
    "}}"                       {
        yybegin(YYINITIAL);
        return VentoTypes.PURE_JS_END;
    }

    /* Consume all characters inside Pure JS block */
    .|\n                       { /* You can accumulate JavaScript content or tokenize further if needed */ }
}

<COMMENTED_CODE> {
    "#}}"                      {
        yybegin(YYINITIAL);
        return VentoTypes.COMMENTED_CODE_END;
    }

    /* Consume all characters inside commented code block */
    .|\n                       { /* Handle commented code content */ }
}

<FRONT_MATTER_STATE> {
    {FRONT_MATTER_END}         {
        yybegin(YYINITIAL);
        return VentoTypes.FRONT_MATTER_END;
    }

    /* Consume YAML front matter content */
    .|\n                       { /* You can process YAML content or tokenize further if needed */ }
}