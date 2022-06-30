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
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class LexerTest {
    @Test
    fun zero() {
        val rule = PluralRuleParser("0")

        assertContentEquals(listOf(Number(0)), rule.lexer())
    }

    @Test
    fun notZero() {
        val rule = PluralRuleParser("n != 0")

        assertContentEquals(listOf(N, NotEquals, Number(0)), rule.lexer())
    }

    @Test
    fun gtZero() {
        val rule = PluralRuleParser("n > 0")

        assertContentEquals(listOf(N, Greater, Number(0)), rule.lexer())
    }

    @Test
    fun gteZero() {
        val rule = PluralRuleParser("n >= 0")

        assertContentEquals(listOf(N, GreaterOrEquals, Number(0)), rule.lexer())
    }

    @Test
    fun ltTen() {
        val rule = PluralRuleParser("n < 10")

        assertContentEquals(listOf(N, Less, Number(10)), rule.lexer())
    }

    @Test
    fun leTen() {
        val rule = PluralRuleParser("n <= 10")

        assertContentEquals(listOf(N, LessOrEquals, Number(10)), rule.lexer())
    }

    @Test
    fun remainder() {
        val rule = PluralRuleParser("n % 10")

        assertContentEquals(listOf(N, Remainder, Number(10)), rule.lexer())
    }

    @Test
    fun bracesNotZero() {
        val rule = PluralRuleParser("(n != 0)")

        assertContentEquals(listOf(LeftParen, N, NotEquals, Number(0), RightParen), rule.lexer())
    }

    @Test
    fun andZero() {
        val rule = PluralRuleParser("n && 0")

        assertContentEquals(listOf(N, And, Number(0)), rule.lexer())
    }

    @Test
    fun orOne() {
        val rule = PluralRuleParser("n || 1")

        assertContentEquals(listOf(N, Or, Number(1)), rule.lexer())
    }

    @Test
    fun ternary() {
        val rule = PluralRuleParser("n ? 123 : 321")

        assertContentEquals(listOf(N, QuestionMark,
            Number(123), Colon,
            Number(321)
        ), rule.lexer())
    }

    @Test
    fun ternaryOrCondition() {
        val rule = PluralRuleParser("(n==1 || n%10==1) ? 0 : 1")

        assertContentEquals(listOf(
            LeftParen,
                N, Equals, Number(1),
                Or,
                N, Remainder, Number(10), Equals, Number(1),
            RightParen,
            QuestionMark, Number(0), Colon, Number(1)
        ), rule.lexer())
    }

    @Test
    fun parens() {
        val rule = PluralRuleParser("((n%10==1) ? 0 : (1))")

        assertContentEquals(listOf(
            LeftParen,
            LeftParen, N, Remainder, Number(10), Equals, Number(1), RightParen,
            QuestionMark, Number(0), Colon, LeftParen, Number(1), RightParen,
            RightParen
        ), rule.lexer())
    }

    @Test
    fun ru() {
        val rule = PluralRuleParser("((n%10==1 && n%100!=11) ? 0 : ((n%10 >= 2 && n%10 <=4 && (n%100 < 12 || n%100 > 14)) ? 1 : ((n%10 == 0 || (n%10 >= 5 && n%10 <=9)) || (n%100 >= 11 && n%100 <= 14)) ? 2 : 3))")

        val tokens = rule.lexer()

        assertEquals(91, tokens.size, "tokenized sequence size differs")
        assertEquals(LeftParen, tokens[0])
        assertEquals(QuestionMark, tokens[14])
        assertEquals(RightParen, tokens[90])
    }
}