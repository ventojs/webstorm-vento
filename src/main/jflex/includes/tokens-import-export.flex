// BLOCK 1 - START
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.js.vento.plugin.parser.VentoParserTypes;
import org.js.vento.plugin.lexer.VentoLexerTypes;
import static com.intellij.psi.TokenType.WHITE_SPACE;
// BLOCK 1 - END
%%

// BLOCK 2 - START

%state IMPORT
%state EXPORT
%state VALUES

OPEN_VENTO_BLOCK = \{\{
CLOSE_VENTO_BLOCK = }}
EMPTY_LINE=(\r\n|\r|\n)[ \t]*(\r\n|\r|\n)
WHITESPACE = [ \t\r\n]+

IMPORT = "import"
EXPORT = "export"
FROM = "from"

// BLOCK 2 - END
%%




<IMPORT> {
    {WHITESPACE}   {  }

    {IMPORT} / [ \t] {
          yybegin(VALUES);
          return VentoLexerTypes.IMPORT_KEY;
    }

    {FROM} { return VentoLexerTypes.IMPORT_FROM; }

    [\"][.]?[/]?.*".vto"[\"] / {WHITESPACE}{CBLOCK} {
          yybegin(BLOCK);
          return VentoLexerTypes.IMPORT_FILE;
    }

    [^] {
          yybegin(BLOCK);
          return VentoLexerTypes.ERROR;
    }
}

<VALUES> {
    {WHITESPACE}   {  }

    \{  { return VentoLexerTypes.IMPORT_VALUES; }
    \,  { return VentoLexerTypes.IMPORT_VALUES; }
    \}/ [ \t]+"from"  {
          yybegin(IMPORT);
          return VentoLexerTypes.IMPORT_VALUES;
    }

    [ \t]+ / {FROM} {
          yybegin(IMPORT);
    }

    [a-zA-Z_$]+[a-zA-Z_$0-9]+ { return VentoLexerTypes.IMPORT_VALUES; }

    [^] {
          yybegin(IMPORT);
          return VentoLexerTypes.ERROR;
    }

}
