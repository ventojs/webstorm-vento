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
%state IMPORT_EXPORT

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

    \{([a-zA-Z]+[_0-9]+[,]?)+} { return VentoLexerTypes.IMPORT_VALUES; }

    {FROM} { return VentoLexerTypes.IMPORT_FROM; }

    [\"][.]?[/]?.*".vto"[\"] {return VentoLexerTypes.IMPORT_FILE;}

    [^] {
              yybegin(IMPORT_EXPORT);
              return VentoLexerTypes.ERROR;
        }
}

