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

/**
 * Parsed plural rule expression from PO file
 */
class PluralRule(rule: String) : PluralRuleExpression {
    private val parsedRule = PluralRuleParser(rule).parse()

    override fun evaluate(n: Int): Int {
        return parsedRule.evaluate(n)
    }
}

object EmptyRule : PluralRuleExpression {
    override fun evaluate(n: Int): Int = 0
}

/**
 * The `n` literal as used in plural rule
 */
object PluralRuleN : PluralRuleExpression {
    override fun evaluate(n: Int): Int = n
}

class PluralRuleBinaryExpression(
    private val left: PluralRuleExpression,
    private val op: BinaryOp,
    private val right: Int
) : PluralRuleExpression {
    override fun evaluate(n: Int): Int {
        return when(op) {
            BinaryOp.NotEquals -> if (left.evaluate(n) != right) 1 else 0
            BinaryOp.Equals -> if (left.evaluate(n) == right) 1 else 0
            BinaryOp.Less -> if (left.evaluate(n) < right) 1 else 0
            BinaryOp.LessOrEquals -> if (left.evaluate(n) <= right) 1 else 0
            BinaryOp.Greater -> if (left.evaluate(n) > right) 1 else 0
            BinaryOp.GreaterOrEquals -> if (left.evaluate(n) >= right) 1 else 0
            BinaryOp.Reminder -> left.evaluate(n) % right
        }
    }
}

enum class BinaryOp {
    NotEquals,
    Equals,
    Less,
    LessOrEquals,
    Greater,
    GreaterOrEquals,
    Reminder
}
