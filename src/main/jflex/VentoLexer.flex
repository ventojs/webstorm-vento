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



%state BRACKET
%state COMMENT
%state JSON_STRING
%state JS_OBJECT
%state JS_REGEX
%state JS_STRING_BACK_TICK
%state JS_STRING_DOUBLE_QOUTE
%state JS_STRING_SINGLE_QUOTE
%state SCRIPT_CONTENT
%state VARIABLE_CONTENT

%{
    // Ensure we handle EOF properly
    private boolean atEof = false;
%}


CLOSE_COMMENT_PHRASE = -?#}}
CLOSE_JAVASCRIPT = }}
CLOSE_VARIABLE_PHRASE = -?}}
DEFAULT_HTML = [^{]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
OPEN_COMMENT_PHRASE = \{\{#-?
OPEN_JAVASCRIPT = \{\{>
OPEN_VARIABLE_PHRASE = \{\{-?
WHITESPACE = [ \t\r\n]+

%{
  // Tracks nested `{` … `}` depth
  private int objectDepth = 0;
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

    {OPEN_VARIABLE_PHRASE}    {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_START;
    }

    [^] { return VentoLexerTypes.ERROR; }

}

<VARIABLE_CONTENT> {

   //strings
   \" {
            yybegin(JS_STRING_DOUBLE_QOUTE);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   ' {
            yybegin(JS_STRING_SINGLE_QUOTE);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   ` {
            yybegin(JS_STRING_BACK_TICK);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   // regex segment
   \/  {
            yybegin(JS_REGEX);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   //objects
   \{ {
            objectDepth=1;
            yybegin(JS_OBJECT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   "|>" {
           return VentoLexerTypes.PIPE_ELEMENT;
   }

   \- / [^}] {return VentoLexerTypes.VARIABLE_ELEMENT;}


   [^\/\"'`{}\- \t]+ { return VentoLexerTypes.VARIABLE_ELEMENT; }

   {WHITESPACE} {}

   {CLOSE_VARIABLE_PHRASE} {
            yybegin(YYINITIAL);
            return VentoLexerTypes.VARIABLE_END;
   }

   [^] { return VentoLexerTypes.ERROR; }
}

<JS_OBJECT> {

    \{ {
            objectDepth++;
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    [^}{\"]+ {return VentoLexerTypes.VARIABLE_ELEMENT;}

    \} {
            objectDepth--;
            if (objectDepth == 0) {
             yybegin(VARIABLE_CONTENT);
            }
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    //single line strings
    \" {
            yybegin(JSON_STRING);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JSON_STRING> {

    [^\"]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    \" {
            yybegin(JS_OBJECT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_STRING_DOUBLE_QOUTE> {

    "\\\"" { return VentoLexerTypes.VARIABLE_ELEMENT;}

    [^\\\"]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    [\"]+ {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_STRING_SINGLE_QUOTE> {

    "\\'" { return VentoLexerTypes.VARIABLE_ELEMENT;}
    [^']+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    ' {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_STRING_BACK_TICK> {

    [^`]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    ` {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<JS_REGEX> {

    \[ {
            yybegin(BRACKET);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    [^\/\[]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    \/ {
            yybegin(VARIABLE_CONTENT);
            return VentoLexerTypes.VARIABLE_ELEMENT;
    }

}

<BRACKET> {

  "]"        { yybegin(JS_REGEX); return VentoLexerTypes.VARIABLE_ELEMENT; }
  [^\]]+     { return VentoLexerTypes.VARIABLE_ELEMENT; }   // any char except ']'

}

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
