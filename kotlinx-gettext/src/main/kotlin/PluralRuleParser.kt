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

package com.github.kropp.kotlinx.gettext

internal class PluralRuleParser(private val rule: String) {
    private var offset: Int = 0

    /**
     * Parse plural rule as defined in PO file
     */
    fun parse(): PluralRuleExpression {
        return try {
            parseBinaryExpression() ?: EmptyRule
        } catch (_: Throwable) {
            EmptyRule
        }
    }

    private fun parseWhitespace() {
        while (rule[offset].isWhitespace()) {
            if (offset == rule.length - 1) {
                return
            }
            offset++
        }
    }

    private fun parseBinaryOperation(): BinaryOp? {
        if (offset >= rule.length) return null
        return when (rule[offset]) {
            '!' -> { offset += 2; BinaryOp.NotEquals }
            '=' -> { offset += 2; BinaryOp.Equals }
            '<' -> {
                offset++
                if (offset < rule.length && rule[offset] == '=') {
                    offset++
                    BinaryOp.LessOrEquals
                } else {
                    BinaryOp.Less
                }
            }
            '>' -> {
                offset++
                if (offset < rule.length && rule[offset] == '=') {
                    offset++
                    BinaryOp.GreaterOrEquals
                } else {
                    BinaryOp.Greater
                }
            }
            '%' -> { offset++; BinaryOp.Reminder }
            else -> null
        }
    }

    private fun parseBinaryExpression(): PluralRuleExpression? {
        val n = parseN() ?: return null
        parseWhitespace()
        val op = parseBinaryOperation() ?: return null
        parseWhitespace()
        val number = parseNumber() ?: return null
        return PluralRuleBinaryExpression(n, op, number)
    }

    private fun parseN(): PluralRuleN? {
        return if (rule[offset] == 'n') {
            offset++
            PluralRuleN
        } else null
    }

    private fun parseNumber(): Int? {
        var end = offset
        var result = 0
        while (end < rule.length && rule[end].isDigit()) {
            result = result * 10 + rule[end].digitToInt()
            end++
        }
        if (end == offset) {
            return null
        }
        offset = end
        return result
    }
}