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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun zero() {
        val rule = PluralRule("0")

        assertEquals(0, rule.evaluate(0))
    }

    @Test
    fun notZero() {
        val rule = PluralRule("n != 0")

        assertEquals(0, rule.evaluate(0))
        assertEquals(1, rule.evaluate(1))
        assertEquals(1, rule.evaluate(2))
        assertEquals(1, rule.evaluate(10))
        assertEquals(1, rule.evaluate(100))
    }

    @Test
    fun gtZero() {
        val rule = PluralRule("n > 0")

        assertEquals(0, rule.evaluate(0))
        assertEquals(1, rule.evaluate(1))
        assertEquals(1, rule.evaluate(2))
        assertEquals(1, rule.evaluate(10))
        assertEquals(1, rule.evaluate(100))
    }

    @Test
    fun gteZero() {
        val rule = PluralRule("n >= 0")

        assertEquals(1, rule.evaluate(0))
        assertEquals(1, rule.evaluate(1))
        assertEquals(1, rule.evaluate(2))
        assertEquals(1, rule.evaluate(10))
        assertEquals(1, rule.evaluate(100))
    }

    @Test
    fun ltTen() {
        val rule = PluralRule("n < 10")

        assertEquals(1, rule.evaluate(0))
        assertEquals(1, rule.evaluate(1))
        assertEquals(1, rule.evaluate(2))
        assertEquals(0, rule.evaluate(10))
        assertEquals(0, rule.evaluate(100))
    }

    @Test
    fun leTen() {
        val rule = PluralRule("n <= 10")

        assertEquals(1, rule.evaluate(0))
        assertEquals(1, rule.evaluate(1))
        assertEquals(1, rule.evaluate(2))
        assertEquals(1, rule.evaluate(10))
        assertEquals(0, rule.evaluate(100))
    }

    @Test
    fun reminder() {
        val rule = PluralRule("n % 10")

        assertEquals(0, rule.evaluate(0))
        assertEquals(1, rule.evaluate(1))
        assertEquals(2, rule.evaluate(2))
        assertEquals(0, rule.evaluate(10))
        assertEquals(3, rule.evaluate(13))
        assertEquals(7, rule.evaluate(17))
        assertEquals(0, rule.evaluate(100))
        assertEquals(9, rule.evaluate(129))
    }
}