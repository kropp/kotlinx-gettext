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

/**
 * A ternary expression, like in C or Java.
 *
 * If [condition] evaluates to a non-zero value, the result is the evaluation result of the [left] part,
 * otherwise it is the result of the evaluation of the [right] part.
 */
public class TernaryExpression(
    private val condition: PluralRuleExpression,
    private val left: PluralRuleExpression,
    private val right: PluralRuleExpression,
) : PluralRuleExpression {
    override fun evaluate(n: Int): Int {
        return if (condition.evaluate(n) != 0) left.evaluate(n) else right.evaluate(n)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is TernaryExpression) return false

        if (condition != other.condition) return false
        if (left != other.left) return false
        if (right != other.right) return false

        return true
    }

    override fun hashCode(): Int {
        var result = condition.hashCode()
        result = 31 * result + left.hashCode()
        result = 31 * result + right.hashCode()
        return result
    }

    override fun toString(): String {
        return "$condition ? $left : $right"
    }
}