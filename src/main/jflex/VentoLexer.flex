/*---------------------------------------------------------------------------*
 | VentoLexer.flex                                                            |
 | Lexer specification for the Vento language using JFlex for WebStorm IDE      |
 *---------------------------------------------------------------------------*/
package org.js.vento.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import kotlin.Triple;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.ParserElements;
import static com.intellij.psi.TokenType.WHITE_SPACE;import java.util.HashMap;

%%

%public
%class VentoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%char


// STATES
%state COMMENT
%state SCRIPT_CONTENT
%state BLOCK
%state UNKNOWN
%state HTML
%state FRONTMATTER
%state FMLINE
%state FMVALUE


%{
    public LexerStrategy strategy = null;
    public void setStrategy(LexerStrategy strategy){ this.strategy = strategy; }
    public int getzzStartRead() { return zzStartRead;}
    public int getzzEndRead() { return zzEndRead;}
    public int getzzCurrentPos() { return zzCurrentPos;}
    public int getzzMarkedPos() { return zzMarkedPos;}
    public CharSequence getzzBuffer() { return zzBuffer;}
    public boolean iszzAtEOF() { return zzAtEOF;}

    public void leave() { this.strategy.leave();}
    public void enter(int state) { this.strategy.enter(state);}
    public void pushbackall() { this.strategy.pushbackall();}
    public void decObjDepth() { this.strategy.decObjDepth();}
    public void incObjDepth() { this.strategy.incObjDepth();}
    public void resetObjDepth() { this.strategy.resetObjDepth();}

    public void decArrDepth() { this.strategy.decArrDepth();}
    public void incArrDepth() { this.strategy.incArrDepth();}
    public void resetArrDepth() { this.strategy.resetArrDepth();}

    public void decParDepth() { this.strategy.decParDepth();}
    public void incParDepth() { this.strategy.incParDepth();}
    public void resetParDepth() { this.strategy.resetParDepth();}

    public Triple<Integer, Integer, Integer> currentDepth() { return this.strategy.currentDepth();}

    public void debugModeOff() { this.strategy.debugModeOff();}
    public void debugModeRestore() { this.strategy.debugModeRestore();}
    public void debugMsg(String msg) { this.strategy.debugMsg(msg);}
    public boolean parentStateIs(int state) { return this.strategy.parentStateIs(state);}

    public int fmcount =0;
%}


DEFAULT_HTML = [^]+
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
WHITESPACE = [ \t\r\n]+
OWS = [ \t\r\n]*
STRING = [\"][^\"\n\r]*[\"]
NUMBER = [0-9_]+
BOOLEAN = "true"|"false"

EQUAL = [=]
SYMBOL = [a-zA-Z_$]+[a-zA-Z_$0-9]*
PIPE = "|>"

OBLOCK = "{{"
JSBLOCK = "{{>"
OTBLOCK = "{{-"
CBLOCK = "}}"
CTBLOCK = "-}}"

OCOMMENT = {OBLOCK}#-?
CCOMMENT = -?#{CBLOCK}

OJS = {OBLOCK}>
OVAR = {OBLOCK}-?
CVAR = -?{CBLOCK}

FMBLOCK = "---"

%%

<YYINITIAL> {

    {FMBLOCK} { pushbackall(); enter(FRONTMATTER); }

    [^-] { pushbackall(); enter(HTML); }

}

<FRONTMATTER> {
    {WHITESPACE} {  }

    {SYMBOL}{OWS}[:] { pushbackall(); enter(FMLINE);}
    "  -"{OWS} { pushbackall(); enter(FMLINE); }

    {FMBLOCK} {
       fmcount++;
       if (fmcount == 2){
            fmcount=0;
            leave();
            return LexerTokens.FRONTMATTER_CLOSE;
       }
       return LexerTokens.FRONTMATTER_OPEN;

    }

    [^]   { pushbackall(); enter(HTML); }

}

<FMLINE> {

    {SYMBOL} { return LexerTokens.FRONTMATTER_KEY; }
    [:] { enter( FMVALUE); return LexerTokens.COLON; }
    "  -"{OWS}{SYMBOL} { return LexerTokens.FRONTMATTER_FLAG; }
    [\r\n]+ { leave(); }

}

<FMVALUE> {
    \"|\'|\` {  pushbackall(); enter(STRING); }
    [^\r\n]+  { leave(); return LexerTokens.FRONTMATTER_VALUE;}
    [\r\n]+ { leave(); }
}

<HTML> {

    {WHITESPACE} { return WHITE_SPACE; }

    {OCOMMENT} {
          enter(COMMENT);
          return LexerTokens.COMMENT_START;
      }

    {OBLOCK} {
          enter(BLOCK);
          return LexerTokens.VBLOCK_OPEN;
      }

    {OTBLOCK} {
          enter(BLOCK);
          return LexerTokens.VBLOCK_OPEN;
      }

    {JSBLOCK} {
          enter(SCRIPT_CONTENT);
          return LexerTokens.JSBLOCK_OPEN;
      }

    ([^\{][^\{]?)+ { return LexerTokens.HTML; }

    [^]   { return LexerTokens.HTML; }

}

<BLOCK> {
    {WHITESPACE} { }
    {CBLOCK} { leave(); return LexerTokens.VBLOCK_CLOSE;}
    {CTBLOCK} { leave(); return LexerTokens.VBLOCK_CLOSE;}
    {KEYWORDS} { pushbackall(); enter(KEYWORDS); }
    {CLOSING_KEYWORDS} { pushbackall(); enter(KEYWORDS_CLOSE); }

    "|>" { return LexerTokens.PIPE; }

    [=] { return LexerTokens.EQUAL;}

    [^=] {
          pushbackall();
          enter(EXPRESSION);
      }

    {OBLOCK} { return LexerTokens.UNKNOWN; }

    <<EOF>> {
          leave();
          return LexerTokens.UNKNOWN;
      }


}

<UNKNOWN> {

    [^}] { return LexerTokens.UNKNOWN;}
    "}}" { pushbackall(); leave();}
    "}"{OWS}"}"{OWS}"}" { yypushback(2); leave();return LexerTokens.UNKNOWN;}

}

<COMMENT> {

    // Match everything that is not the start of a closing comment sequence
    ([^#-]|"#"[^}]|"-"[^#])+ { return LexerTokens.COMMENT_CONTENT; }

    // Handle single characters that might be part of closing sequences
    "#" { return LexerTokens.COMMENT_CONTENT; }
    "-" { return LexerTokens.COMMENT_CONTENT; }

    {CCOMMENT} {
          leave();
          return LexerTokens.COMMENT_END;
      }

}

%include includes/general-expression.flex
%include includes/general-file.flex
%include includes/general-function.flex
%include includes/general-pipe.flex
%include includes/general-string.flex
%include includes/keywords-echo.flex
%include includes/keywords-export.flex
%include includes/keywords-for.flex
%include includes/keywords-if.flex
%include includes/keywords-import.flex
%include includes/keywords-include.flex
%include includes/keywords-layout.flex
%include includes/keywords-set.flex
%include includes/keywords.flex
%include includes/no-keywords.flex
%include includes/variables.flex

< EXPORT, FILE, FOR, FUNCTION, FUNCTION_ARGS,FUNCTION_LAMBDA, IF, IMPORT, KEYWORDS, KEYWORDS_CLOSE, FMLINE, FMVALUE, NOKEYWORDS, SET, SET_BLOCK_MODE, SET_VALUE, EXPRESSION,  INCLUDE, LAYOUT, SLOT, ECHO> {

    "}}"|"{{" {
          pushbackall();
          leave();
      }

    "-}}" {
          pushbackall();
          leave();
      }

    <<EOF>> { leave(); }

    [^] {
          leave();
          return LexerTokens.UNKNOWN;
      }
}


