/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/

import org.js.vento.webstormvento.VentoTypes;
import java_cup.runtime.Symbol;

%%

%class VentoLexer
%unicode
%cup
%implements java_cup.runtime.Scanner
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
COMMENT = /\*[^]*?\*/ | //.*
PURE_JS_START = \{\{>
COMMENTED_CODE_START = \{\{#
FRONT_MATTER_START = \-\-\-
FRONT_MATTER_END = \-\-\-
TEMPLATE_TAG_START = \{\{(-)?
TEMPLATE_TAG_END = (-)?\}\}

/* Keywords in Vento */
KEYWORD = \b(for|of|if|else\s+if|else|include|set|layout|echo|function|async\s+function|import|from|export|await)\b

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
        return new Symbol(VentoTypes.COMMENT, yytext());
    }

    /* Handle commented Vento code */
    {COMMENTED_CODE_START}    {
        yybegin(COMMENTED_CODE);
        /* Capture the start of commented code */
    }

    /* Handle pure JavaScript code */
    {PURE_JS_START}           {
        yybegin(PURE_JS);
        return new Symbol(VentoTypes.PURE_JS_START, yytext());
    }

    /* Handle template tags */
    {TEMPLATE_TAG_START}      {
        yybegin(MACRO_START);
        /* Enter MACRO_START state when '{{' or '{{-' is encountered */
    }

    {KEYWORD}                 { return new Symbol(VentoTypes.KEYWORD, yytext()); }

    {IDENTIFIER}              { return new Symbol(VentoTypes.IDENTIFIER, yytext()); }
    {NUMBER}                  { return new Symbol(VentoTypes.NUMBER, yytext()); }
    {STRING}                  { return new Symbol(VentoTypes.STRING, yytext()); }

    /* Operators and punctuation */
    "="                       { return new Symbol(VentoTypes.EQUALS); }
    "+"                       { return new Symbol(VentoTypes.PLUS); }
    "-"                       { return new Symbol(VentoTypes.MINUS); }
    "*"                       { return new Symbol(VentoTypes.MULTIPLY); }
    "/"                       { return new Symbol(VentoTypes.DIVIDE); }
    ";"                       { return new Symbol(VentoTypes.SEMICOLON); }
    "{"                       { return new Symbol(VentoTypes.LBRACE); }
    "}"                       { return new Symbol(VentoTypes.RBRACE); }

    .                         { /* Handle any other character as error */
        return new Symbol(VentoTypes.ERROR, yytext());
    }
}

<MACRO_START> {
    ">"                        {
        yybegin(PURE_JS);
        return new Symbol(VentoTypes.PURE_JS_START, "{{>" );
    }

    "#"                        {
        yybegin(COMMENTED_CODE);
        return new Symbol(VentoTypes.COMMENTED_CODE_START, "{{#");
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
        return new Symbol(VentoTypes.TEMPLATE_TAG_END, "}}");
    }

    {KEYWORD}                 { return new Symbol(VentoTypes.KEYWORD, yytext()); }
    {IDENTIFIER}              { return new Symbol(VentoTypes.IDENTIFIER, yytext()); }
    {NUMBER}                  { return new Symbol(VentoTypes.NUMBER, yytext()); }
    {STRING}                  { return new Symbol(VentoTypes.STRING, yytext()); }

    /* Operators and punctuation */
    "="                       { return new Symbol(VentoTypes.EQUALS); }
    "+"                       { return new Symbol(VentoTypes.PLUS); }
    "-"                       { return new Symbol(VentoTypes.MINUS); }
    "*"                       { return new Symbol(VentoTypes.MULTIPLY); }
    "/"                       { return new Symbol(VentoTypes.DIVIDE); }
    ";"                       { return new Symbol(VentoTypes.SEMICOLON); }
    "{"                       { return new Symbol(VentoTypes.LBRACE); }
    "}"                       { return new Symbol(VentoTypes.RBRACE); }

    .                         { /* Handle any other character as error */
        return new Symbol(VentoTypes.ERROR, yytext());
    }
}

<PURE_JS> {
    "}}"                       {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.PURE_JS_END, "}}");
    }

    /* Consume all characters inside Pure JS block */
    .|\n                       { /* You can accumulate JavaScript content or tokenize further if needed */ }
}

<COMMENTED_CODE> {
    "#}}"                      {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.COMMENTED_CODE_END, "#}}");
    }

    /* Consume all characters inside commented code block */
    .|\n                       { /* Handle commented code content */ }
}

<FRONT_MATTER_STATE> {
    {FRONT_MATTER_END}         {
        yybegin(YYINITIAL);
        return new Symbol(VentoTypes.FRONT_MATTER_END, "---");
    }

    /* Consume YAML front matter content */
    .|\n                       { /* You can process YAML content or tokenize further if needed */ }
}