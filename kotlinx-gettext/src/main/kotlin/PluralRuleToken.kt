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
 * A token in plural rule, such as number, logical or comparison operation, or parenthesis
 */
interface PluralRuleToken {
    object LeftParen : PluralRuleToken
    object RightParen : PluralRuleToken
    object QuestionMark : PluralRuleToken
    object Colon : PluralRuleToken
}