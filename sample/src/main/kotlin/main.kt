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

import name.kropp.kotlinx.gettext.Gettext
import name.kropp.kotlinx.gettext.Locale

fun main() {
    val i18n = Gettext.load(Locale.GERMAN, Thread.currentThread().contextClassLoader.getResourceAsStream("de.po")!!)
    println(i18n.tr("Hello world!"))
    println(i18n.trn("You have a message", "You have {0} messages", 1, 1))
    println(i18n.trn("You have a message", "You have {0} messages", 3, 3))
}