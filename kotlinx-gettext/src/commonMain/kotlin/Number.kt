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
 * An integer number
 */
public class Number(private val number: Int): PluralRuleExpression, PluralRuleToken {
    override fun evaluate(n: Int): Int = number

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is Number) return false

        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        return number
    }

    override fun toString(): String = number.toString()
}