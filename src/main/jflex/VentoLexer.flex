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
%state JS_STRING
%state JSON_STRING
%state JS_OBJECT
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
OPEN_VARIABLE_PHRASE = \{\{-?
CLOSE_VARIABLE_PHRASE = -?}}
DEFAULT_HTML = [^{]+
TEXT=[^<{]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)

%{
  // Tracks nested `{` … `}` depth
  private int objectDepth = 0;

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
    {DEFAULT_HTML}            { return VentoParserTypes.HTML_ELEMENT; }


    {OPEN_COMMENT_PHRASE}    {
            yybegin(COMMENT);
            return VentoLexerTypes.OPEN_COMMENT_CLAUSE;
         }

    {JAVASCRIPT_START}    {
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

   //single line strings
   \" {
          yybegin(JS_STRING);
          return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   //objects
   \{ {
          objectDepth=1;
          yybegin(JS_OBJECT);
          return VentoLexerTypes.VARIABLE_ELEMENT;
   }

   \- / [^}] {return VentoLexerTypes.VARIABLE_ELEMENT;}


   [^\"{}\- \t]+ { return VentoLexerTypes.VARIABLE_ELEMENT; }

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

    [^] {return VentoLexerTypes.ERROR;}
}

<JSON_STRING> {
    [^\"]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    \" {
         yybegin(JS_OBJECT);
         return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    [^] {return VentoLexerTypes.ERROR;}
}

<JS_STRING> {
    [^\"]+ { return VentoLexerTypes.VARIABLE_ELEMENT;}

    \" {
          yybegin(VARIABLE_CONTENT);
          return VentoLexerTypes.VARIABLE_ELEMENT;
    }

    [^] {return VentoLexerTypes.ERROR;}
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
