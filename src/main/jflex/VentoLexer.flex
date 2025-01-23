/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex                    |
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

IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
NUMBER = [0-9]+(\.[0-9]+)?
STRING = \"([^\"\\]|\\.)*\"
WHITESPACE = [ \t\r\n]+
COMMENT = \/\*[^]*?\*\/ | \/\/.*
PURE_JS = \{\{>[^}]*\}\}
COMMENTED_CODE = \{\{#[^}]*#\}\}
FRONT_MATTER = \-\-\-[\s\S]*?\-\-\-

%%

<YYINITIAL> {

    {WHITESPACE}              { /* Skip whitespace */ }

    {FRONT_MATTER}            { /* Handle front matters */ return new Symbol(VentoTypes.FRONT_MATTER, yytext()); }

    {COMMENT}                 { /* Handle comments */ return new Symbol(VentoTypes.COMMENT, yytext()); }

    {COMMENTED_CODE}          { /* Handle commented Vento code */ return new Symbol(VentoTypes.COMMENTED_CODE, yytext()); }

    {PURE_JS}                 { /* Handle pure JavaScript code */ return new Symbol(VentoTypes.PURE_JS, yytext()); }

    "{{"                      { /* Enter MACRO_START state when '{{' is encountered */ yybegin(MACRO_START); }

    "if"                      { return new Symbol(VentoTypes.IF); }
    "else"                    { return new Symbol(VentoTypes.ELSE); }
    "for"                     { return new Symbol(VentoTypes.FOR); }
    "while"                   { return new Symbol(VentoTypes.WHILE); }
    "function"                { return new Symbol(VentoTypes.FUNCTION); }
    "return"                  { return new Symbol(VentoTypes.RETURN); }

    {IDENTIFIER}              { return new Symbol(VentoTypes.IDENTIFIER, yytext()); }
    {NUMBER}                  { return new Symbol(VentoTypes.NUMBER, yytext()); }
    {STRING}                  { return new Symbol(VentoTypes.STRING, yytext()); }

    "="                       { return new Symbol(VentoTypes.EQUALS); }
    "+"                       { return new Symbol(VentoTypes.PLUS); }
    "-"                       { return new Symbol(VentoTypes.MINUS); }
    "*"                       { return new Symbol(VentoTypes.MULTIPLY); }
    "/"                       { return new Symbol(VentoTypes.DIVIDE); }
    ";"                       { return new Symbol(VentoTypes.SEMICOLON); }
    "{"                       { return new Symbol(VentoTypes.LBRACE); }
    "}"                       { return new Symbol(VentoTypes.RBRACE); }

    .                         { /* Handle any other character */ return new Symbol(VentoTypes.ERROR, yytext()); }
}

<MACRO_START> {

    ">"                        { /* Handle PURE_JS */
                                 yybegin(YYINITIAL);
                                 return new Symbol(VentoTypes.PURE_JS, "{{>" + yytext());
                               }

    "#"                        { /* Handle COMMENTED_CODE */
                                 yybegin(YYINITIAL);
                                 return new Symbol(VentoTypes.COMMENTED_CODE, "{{#" + yytext());
                               }

    [^#>]                      { /* Enter VENTO_ELEMENT_STATE for other '{{' patterns */
                                 yybegin(VENTO_ELEMENT_STATE);
                                 yycharat = yycharat; /* Placeholder if needed */
                               }
}

<VENTO_ELEMENT_STATE> {

    "}}"                       { /* End of VENTO_ELEMENT */
                                 yybegin(YYINITIAL);
                                 return new Symbol(VentoTypes.VENTO_ELEMENT, yytext());
                               }

    [^}]+                      { /* Collect content inside VENTO_ELEMENT */ /* You can accumulate the text as needed */ }

    .                          { /* Handle any other character */ return new Symbol(VentoTypes.ERROR, yytext()); }
}