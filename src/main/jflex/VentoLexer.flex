/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex                    |
 *---------------------------------------------------------------------------*/

%%

%class VentoLexer
%unicode
%cup
%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol

%{
    // Imports and additional code can be placed here
    import com.intellij.psi.tree.IElementType;
    import org.js.vento.webstormvento.psi.VentoTypes;
    import org.js.vento.webstormvento.VentoTypes.*;
    import java_cup.runtime.Symbol;
%}

IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
NUMBER = [0-9]+(\.[0-9]+)?
STRING = \"([^\"\\]|\\.)*\"
WHITESPACE = [ \t\r\n]+
COMMENT = \/\*(.|\n)*?\*\/ | \/\/.*

%%

<YYINITIAL> {

    {WHITESPACE}              { /* Skip whitespace */ }

    {COMMENT}                 { /* Handle comments */ return new Symbol(VentoTypes.COMMENT); }

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