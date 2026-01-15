/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.VentoLexer.*
import java.util.ArrayDeque
import kotlin.math.max

class LexerStrategyImpl(
    val lexer: VentoLexer,
    var debugConfig: Boolean = true,
    override var loopTolerance: Int = 100,
) : LexerStrategy {
    var stateNames: Map<Int, String> = mapOf()

    /** Current token type — the skeleton expects us to return it from actions.  */
    private var myTokenType: IElementType? = null

    // BRACE, BRACKET, PARENTHESIS
    private var objectDepth = Triple<Int, Int, Int>(0, 0, 0)

    private val stateStack = ArrayDeque<LexerState>()
    private var debug = false

    private var lastEnterState: Int = -1
    private var lastCurrentState: Int = -1
    private var lastPosition: Int = -1
    private var loopCount: Int = 100

    init {
        debug = debugConfig
        stateNames =
            mapOf(
                Pair(BEFORE_OF, "BEFORE_FOR"),
                Pair(BLOCK, "BLOCK"),
                Pair(COMMENT, "COMMENT"),
                Pair(ECHO, "ECHO"),
                Pair(ELSE, "ELSE"),
                Pair(ELSEIF, "ELSEIF"),
                Pair(EXPORT, "EXPORT"),
                Pair(EXPRESSION, "EXPRESSION"),
                Pair(FILE, "FILE"),
                Pair(FRONTMATTER, "FRONTMATTER"),
                Pair(FMLINE, "FMLINE"),
                Pair(FMVALUE, "FMVALUE"),
                Pair(HTML, "HTML"),
                Pair(FOR, "FOR"),
                Pair(FUNCTION, "FUNCTION"),
                Pair(FUNCTION_ARGS, "FUNCTION_ARGS"),
                Pair(FUNCTION_BODY, "FUNCTION_BODY"),
                Pair(FUNCTION_LAMBDA, "FUNCTION_LAMBDA"),
                Pair(IF, "IF"),
                Pair(IMPORT, "IMPORT"),
                Pair(INCLUDE, "INCLUDE"),
                Pair(KEYWORDS, "KEYWORDS"),
                Pair(KEYWORDS_CLOSE, "KEYWORDS_CLOSE"),
                Pair(LAYOUT, "LAYOUT"),
                Pair(NOKEYWORDS, "NOKEYWORDS"),
                Pair(REGEX, "REGEX"),
                Pair(REGEX_ESCAPE, "REGEX_ESCAPE"),
                Pair(REGEX_CLASS, "REGEX_CLASS"),
                Pair(SCRIPT_CONTENT, "SCRIPT_CONTENT"),
                Pair(SET, "SET"),
                Pair(SET_BLOCK_MODE, "SET_BLOCK_MODE"),
                Pair(SET_VALUE, "SET_VALUE"),
                Pair(SET_DESTRUCTURE_VARS, "SET_DESTRUCTURE_VARS"),
                Pair(SLOT, "SLOT"),
                Pair(STRING, "STRING"),
                Pair(STRING_BKTK, "STRING_BKTK"),
                Pair(STRING_DUBL, "STRING_DUBL"),
                Pair(STRING_SNGL, "STRING_SNGL"),
                Pair(UNKNOWN, "UNKNOWN"),
                Pair(VARIABLE, "VARIABLE"),
                Pair(YYINITIAL, "YYINITIAL"),
            )
    }

    override fun stName(s: Int): String {
        val n = stateNames[s]
        return n ?: "STATE#$s"
    }

    /** Enter 'nextState', remembering where we came from (the caller).  */
    override fun enter(nextState: Int) {
        val currentState: Int = lexer.yystate()
        val currentPos = lexer.getzzCurrentPos()

        if (currentState == lastCurrentState && nextState == lastEnterState && currentPos == lastPosition) {
            loopCount++
            if (loopCount >= loopTolerance) {
                val message =
                    "Infinite loop detected: entering ${stName(nextState)} from ${stName(currentState)} at position $currentPos repeated $loopCount times"
                dumpDiag(message)
                throw IllegalStateException(message)
            }
        } else {
            lastCurrentState = currentState
            lastEnterState = nextState
            lastPosition = currentPos
            loopCount = 0
        }

        stateStack.push(LexerState(currentState, stName(currentState), this.objectDepth))
        if (debug) {
            println(
                (
                    "${stName(currentState)}(${lexer.yytext()}) -> " +
                        "${stName(nextState)}(${remaining().replace("\n", "")})\n"
                ).prependIndent(" ".repeat(stateStack.size)),
            )
        }
        lexer.yybegin(nextState)
        this.objectDepth = Triple<Int, Int, Int>(0, 0, 0)
    }

    /** Return to the caller state saved by the most recent enter().  */
    override fun leave() {
        try {
            check(!stateStack.isEmpty()) { "leave() with empty state stack" }
            val nextState: LexerState = stateStack.pop()
            if (debug) {
                println(
                    (
                        "${nextState.name}(${remaining()}) <- " +
                            "${stName(lexer.yystate())}(${lexer.yytext()})\n"
                    ).prependIndent(" ".repeat(stateStack.size + 1)),
                )
            }
            lexer.yybegin(nextState.state)
            this.objectDepth = nextState.depth
        } catch (e: Exception) {
            throw IllegalStateException(
                """Unable to leave() ${
                    stName(
                        lexer.yystate(),
                    )
                } handling [${lexer.yytext()}] at position:${lexer.getzzCurrentPos()}; reason: ${e.message}""",
                e,
            )
        }
    }

    override fun remaining(): String {
        if (lexer.getzzEndRead() <= lexer.getzzCurrentPos()) return ""
        return "\n" +
            lexer
                .getzzBuffer()
                .subSequence(lexer.getzzCurrentPos() + lexer.yylength(), lexer.getzzEndRead())
                .toString() + "\n"
    }

    /** Optional: hard jump (not LIFO) if you need to abort nested states.  */
    override fun resetAt(s: Int) {
        stateStack.clear()
        if (debug) println("resetAt -> " + stName(s))
        lexer.yybegin(s)
    }

    override fun debug() {
        debug(null)
    }

    override fun debug(rule: String?) {
        if (debug) {
            if (rule != null) println("DEBUG rule :$rule")
            println("DEBUG state:" + stName(lexer.yystate()) + " [" + stateStack.size + "]")
            println("DEBUG pos  :" + lexer.getzzCurrentPos() + "; text:" + lexer.yytext())
            println("------------------")
        }
    }

    override fun pushbackall() {
        lexer.yypushback(lexer.yylength())
    }

    // 1) Provide a custom diagnostic printer we can call on demand
    override fun dumpDiag(reason: String?) {
        val start: Int = lexer.getzzStartRead()
        val cur: Int = lexer.getzzCurrentPos()
        val marked: Int = lexer.getzzMarkedPos()

        val previewRadius = 40
        val from = max(0, cur - previewRadius)
        val to = lexer.getzzEndRead().coerceAtMost(cur + previewRadius)

        val buf: CharSequence = lexer.getzzBuffer()
        val before = buf.subSequence(from, cur.coerceAtMost(lexer.getzzEndRead())).toString()
        val after = buf.subSequence(cur.coerceAtMost(lexer.getzzEndRead()), to).toString()

        val stateName = stName(lexer.yystate())
        val stackDepth = stateStack.size

        val caret = StringBuilder()
        for (i in 0..<before.length) caret.append(if (before[i] == '\n') '\n' else ' ')
        caret.append('^')

        System.err.println(
            "LEX ERROR: " + reason + "\n" + "  state      : " + stateName + " (depth=" + stackDepth + ")\n" + "  offsets    : start=" +
                start +
                ", current=" +
                cur +
                ", marked=" +
                marked +
                ", endRead=" +
                lexer.getzzEndRead() +
                "\n" +
                "  atEOF      : " +
                lexer.iszzAtEOF() +
                "\n" +
                "  preview    :\n" +
                before +
                after +
                "\n" +
                caret +
                "\n",
        )
    }

    override fun incObjDepth(): Int {
        objectDepth = Triple(objectDepth.first + 1, objectDepth.second, objectDepth.third)
        return objectDepth.first
    }

    override fun incArrDepth(): Int {
        objectDepth = Triple(objectDepth.first, objectDepth.second + 1, objectDepth.third)
        return objectDepth.second
    }

    override fun incParDepth(): Int {
        objectDepth = Triple(objectDepth.first, objectDepth.second, objectDepth.third + 1)
        return objectDepth.third
    }

    override fun decObjDepth(): Int {
        objectDepth = Triple(objectDepth.first - 1, objectDepth.second, objectDepth.third)
        return objectDepth.first
    }

    override fun decArrDepth(): Int {
        objectDepth = Triple(objectDepth.first, objectDepth.second - 1, objectDepth.third)
        return objectDepth.second
    }

    override fun decParDepth(): Int {
        objectDepth = Triple(objectDepth.first, objectDepth.second, objectDepth.third - 1)
        return objectDepth.third
    }

    override fun resetObjDepth() {
        objectDepth = Triple(0, objectDepth.second, objectDepth.third)
    }

    override fun resetArrDepth() {
        objectDepth = Triple(objectDepth.first, 0, objectDepth.third)
    }

    override fun resetParDepth() {
        objectDepth = Triple(objectDepth.first, objectDepth.second, 0)
    }

    override fun currentDepth(): Triple<Int, Int, Int> = objectDepth

    override fun debugModeOff() {
        debug = false
    }

    override fun debugModeRestore() {
        debug = debugConfig
    }

    override fun debugMsg(msg: String) {
        if (debug) println(msg)
    }

    override fun parentStateIs(state: Int): Boolean = stateStack.isNotEmpty() && stateStack.peek().state == state

    override fun getTokenType(): IElementType = myTokenType!!

    // Small helpers so actions read nicer
    override fun t(tt: IElementType): IElementType {
        myTokenType = tt
        return tt
    }
}
