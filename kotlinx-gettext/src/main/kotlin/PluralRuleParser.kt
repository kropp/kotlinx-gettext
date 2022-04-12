/*
 * Copyright 2022 Victor Kropp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.kropp.kotlinx.gettext

import name.kropp.kotlinx.gettext.BinaryOp.*
import name.kropp.kotlinx.gettext.PluralRuleToken.*

internal class PluralRuleParser(private val rule: String) {
    /**
     * Parse plural rule as defined in PO file
     */
    fun parse(): PluralRuleExpression {
        return try {
            // a plural rule is an expression, see grammar below
            parseExpression(lexer()) ?: EmptyRule
        } catch (t: Throwable) {
            EmptyRule
        }
    }

    /**
     * Tokenize input string, skipping all whitespace
     */
    fun lexer(): List<PluralRuleToken> = buildList {
        var offset = 0
        while (offset < rule.length) {
            if (rule[offset].isWhitespace()) {
                offset++
            } else {
                when(rule[offset]) {
                    'n' -> { add(N); offset++ }
                    '(' -> { add(LeftParen); offset++ }
                    ')' -> { add(RightParen); offset++ }
                    '?' -> { add(QuestionMark); offset++ }
                    ':' -> { add(Colon); offset++ }
                    '!' -> { add(NotEquals); offset += 2 }
                    '=' -> { add(Equals); offset += 2 }
                    '&' -> { add(And); offset += 2 }
                    '|' -> { add(Or); offset += 2 }
                    '<' -> {
                        offset++
                        if (offset < rule.length && rule[offset] == '=') {
                            offset++
                            add(LessOrEquals)
                        } else {
                            add(Less)
                        }
                    }
                    '>' -> {
                        offset++
                        if (offset < rule.length && rule[offset] == '=') {
                            offset++
                            add(GreaterOrEquals)
                        } else {
                            add(Greater)
                        }
                    }
                    '%' -> { add(Remainder); offset++ }
                    else -> {
                        var end = offset
                        var result = 0
                        while (end < rule.length && rule[end].isDigit()) {
                            result = result * 10 + rule[end].digitToInt()
                            end++
                        }
                        if (end != offset) {
                            offset = end
                            add(Number(result))
                        }
                    }
                }
            }
        }
    }

    /**
     * Plural rule grammar
     * Expression = ( Expression ) |
     *              TernaryExpression |
     *              BinaryExpression |
     *              n |
     *              <number>
     * BinaryExpression = Expression op Expression
     *   where op is [BinaryOp] in the listed priority
     */
    private fun parseExpression(tokens: List<PluralRuleToken>): PluralRuleExpression? {
        val first = tokens.firstOrNull()
        val ternary = parseTernaryExpression(tokens)
        if (ternary != null) {
            return ternary
        }
        if (first == LeftParen && tokens.last() == RightParen) {
            val braced = parseExpression(tokens.subList(1, tokens.size - 1))
            if (braced != null) return braced
        }
        if (tokens.size > 2) {
            for (ops in arrayOf(
                arrayOf(Or),
                arrayOf(And),
                arrayOf(NotEquals, Equals, Less, LessOrEquals, Greater, GreaterOrEquals),
                arrayOf(Remainder),
            )) {
                val parsed = parseBinaryExpression(tokens, *ops)
                if (parsed != null) {
                    return parsed
                }
            }
        }
        if (first is N && tokens.size == 1) {
            return first
        }
        if (first is Number && tokens.size == 1) {
            return first
        }
        return null
    }

    private fun parseTernaryExpression(tokens: List<PluralRuleToken>): TernaryExpression? {
        var level = 0
        val questionMark = tokens.indices.firstOrNull {
            val token = tokens[it]
            if (token == LeftParen) level++
            if (token == RightParen) level--
            token == QuestionMark && level == 0
        } ?: return null

        val condition = parseExpression(tokens.subList(0, questionMark)) ?: return null

        level = 0
        val colon = (questionMark+1 until tokens.size).firstOrNull {
            val token = tokens[it]
            if (token == LeftParen) level++
            if (token == RightParen) level--
            token == Colon && level == 0
        } ?: return null

        val left = parseExpression(tokens.subList(questionMark + 1, colon)) ?: return null
        val right = parseExpression(tokens.subList(colon + 1, tokens.size)) ?: return null

        return TernaryExpression(condition, left, right)
    }

    private fun parseBinaryExpression(tokens: List<PluralRuleToken>, vararg operators: BinaryOp): BinaryExpression? {
        for (i in tokens.indices) {
            val token = tokens[i]
            if (token !is BinaryOp || token !in operators) continue

            val left = parseExpression(tokens.subList(0, i)) ?: continue
            val right = parseExpression(tokens.subList(i + 1, tokens.size)) ?: continue

            return BinaryExpression(left, token, right)
        }

        return null
    }
}