/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lexer.FlexLexer
import junit.framework.TestCase
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Regression guard for #244: a reported IDE freeze whose thread dump showed a pooled thread stuck
 * inside [LexerStrategyImpl.enter], with the state stack ([LexerStrategyImpl]'s `ArrayDeque`)
 * growing unboundedly. No single deterministic trigger string was found, but this exercises every
 * new #142 completion snippet's inserted text, several malformed/nested-brace shapes (the kind a
 * mistranslated editor offset could produce), and deeply nested JS object literals, each against a
 * hard wall-clock timeout on a background thread so a genuine hang fails loudly here instead of
 * freezing the IDE. [LexerStrategyImpl.enter] also now has its own defensive depth bound as a
 * second line of defense.
 */
class LexerHangDiagnosticTest : TestCase() {
    fun testCandidatesForHang() {
        val candidates =
            listOf(
                "{{ slot name }} content {{ /slot }}",
                "{{ slot  }} content {{ /slot }}",
                "{{ for key, value of collection }}\n{{ /for }}",
                "{{ for key, value of  }}\n{{ /for }}",
                "{{ for key,  of collection }}\n{{ /for }}",
                "{{ export function name(arg) }} content {{ /export }}",
                "{{ export function (arg) }} content {{ /export }}",
                "{{ export function name() }} content {{ /export }}",
                "{{ break }}",
                "{{ continue }}",
                "{{ break",
                "{{ continue",
                // Malformed / nested brace sequences, simulating template text getting inserted
                // at a mistranslated offset (e.g. into an injected-JS document instead of the
                // real host document).
                "{{ echo \"{{ /echo }}\" }}",
                "{{ if x }}{{ if y }}{{ if z }}{{ /if }}",
                "{{{{ if x }}}}",
                "{{ if {{ /if }} }}",
                "{{ echo \"foo\" {{ /echo }}",
                "{{ echo \"foo\"\n{{ /echo }}\n{{ /echo }}",
                "{{{{{{{{{{ if x",
                "{{ if x }}\n{{ /if }}{{ /if }}{{ /if }}{{ /if }}",
                "{{ slot name }}\n{{ /slot }}\n{{ /slot }}",
                // Deeply nested JS object literals inside an expression.
                "{{ if " + "{a:".repeat(50) + "1" + "}".repeat(50) + " }}",
                "{{ if {a:{b:{c:1}}}==1 }}",
                "{{ echo {a:{b:1}} }}",
                "{{ if {}=={} }}",
                "{{ set x = " + "{a:".repeat(200) + "1" + "}".repeat(200) + " }}",
                "{{ if " + "{".repeat(100) + "}".repeat(100) + " }}",
            )
        val failures = mutableListOf<String>()
        for (code in candidates) {
            if (!lexesWithinTimeout(code, 3000)) {
                failures.add(code)
            }
        }
        if (failures.isNotEmpty()) {
            fail("Lexer hung on: ${failures.joinToString(" | ")}")
        }
    }

    private fun lexesWithinTimeout(code: String, timeoutMs: Long): Boolean {
        val done = CountDownLatch(1)
        var error: Throwable? = null
        val thread =
            Thread {
                try {
                    val lexer: FlexLexer = LexerAdapter.createTestLexer(false)
                    lexer.reset(code, 0, code.length, 0)
                    var iterations = 0
                    var tokenType = lexer.advance()
                    while (tokenType != null && lexer.tokenEnd < code.length) {
                        tokenType = lexer.advance()
                        iterations++
                        if (iterations > 100_000) {
                            error = AssertionError("exceeded 100_000 iterations without EOF for [$code]")
                            break
                        }
                    }
                } catch (t: Throwable) {
                    error = t
                } finally {
                    done.countDown()
                }
            }
        thread.isDaemon = true
        thread.start()
        val finished = done.await(timeoutMs, TimeUnit.MILLISECONDS)
        if (!finished) {
            println("HANG detected for input: [$code]")
            println("Thread stack:\n" + thread.stackTrace.joinToString("\n") { "  at $it" })
            return false
        }
        if (error != null) {
            println("Error (not a hang) for input [$code]: $error")
        }
        return true
    }
}
