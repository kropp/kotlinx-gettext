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
 * Any binary expression used in plural rule
 */
public class BinaryExpression(
    private val left: PluralRuleExpression,
    private val op: BinaryOp,
    private val right: PluralRuleExpression,
) : PluralRuleExpression {

    override fun evaluate(n: Int): Int {
        return when(op) {
            BinaryOp.NotEquals -> if (left.evaluate(n) != right.evaluate(n)) 1 else 0
            BinaryOp.Equals -> if (left.evaluate(n) == right.evaluate(n)) 1 else 0
            BinaryOp.Less -> if (left.evaluate(n) < right.evaluate(n)) 1 else 0
            BinaryOp.LessOrEquals -> if (left.evaluate(n) <= right.evaluate(n)) 1 else 0
            BinaryOp.Greater -> if (left.evaluate(n) > right.evaluate(n)) 1 else 0
            BinaryOp.GreaterOrEquals -> if (left.evaluate(n) >= right.evaluate(n)) 1 else 0
            BinaryOp.Remainder -> left.evaluate(n) % right.evaluate(n)
            BinaryOp.And -> if ((left.evaluate(n) != 0) && (right.evaluate(n) != 0)) 1 else 0
            BinaryOp.Or -> if ((left.evaluate(n) != 0) || (right.evaluate(n) != 0)) 1 else 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is BinaryExpression) return false

        if (left != other.left) return false
        if (op != other.op) return false
        if (right != other.right) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + op.hashCode()
        result = 31 * result + right.hashCode()
        return result
    }

    override fun toString(): String {
        return "$left $op $right"
    }
}