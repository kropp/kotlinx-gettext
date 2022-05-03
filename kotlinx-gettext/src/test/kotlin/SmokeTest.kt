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

import kotlin.test.Test
import kotlin.test.assertNotEquals

class SmokeTest {
    @Test
    fun smoke() {
        val rules = listOf(
            "n==1 || n%10==1 ? 0 : 1",
            "(n != 0)",
            "(n != 1)",
            "(n > 1)",
            "(n%10!=1 || n%100==11)",
            "(n%100==1 ? 0 : n%100==2 ? 1 : n%100==3 || n%100==4 ? 2 : 3)",
            "(n%10==1 && n%100!=11 ? 0 : n != 0 ? 1 : 2)",
            "(n%10==1 && n%100!=11 ? 0 : n%10>=2 && (n%100<10 || n%100>=20) ? 1 : 2)",
            "(n%10==1 && n%100!=11 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2)",
            "(n==0 ? 0 : n==1 ? 1 : 2)",
            "(n==0 ? 0 : n==1 ? 1 : n==2 ? 2 : n%100>=3 && n%100<=10 ? 3 : n%100>=11 ? 4 : 5)",
            "(n==1 ? 0 : (n==0 || (n%100 > 0 && n%100 < 20)) ? 1 : 2)",
            "(n==1 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2)",
            "(n==1 ? 0 : n==0 || ( n%100>1 && n%100<11) ? 1 : (n%100>10 && n%100<20 ) ? 2 : 3)",
            "(n==1 || n==11) ? 0 : (n==2 || n==12) ? 1 : (n > 2 && n < 20) ? 2 : 3",
            "(n==1) ? 0 : (n==2) ? 1 : (n != 8 && n != 11) ? 2 : 3",
            "(n==1) ? 0 : (n==2) ? 1 : (n == 3) ? 2 : 3",
            "(n==1) ? 0 : (n>=2 && n<=4) ? 1 : 2",
            "(n==1) ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2",
            "n%10==1 && n%100!=11 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2",
            "n==1 ? 0 : n==2 ? 1 : (n>2 && n<7) ? 2 :(n>6 && n<11) ? 3 : 4",
        )
        for (rule in rules) {
            assertNotEquals(EmptyRule, PluralRule(rule), "$rule incorrectly parsed as EmptyRule")
        }
    }
}