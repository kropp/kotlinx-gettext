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

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

class KeywordSpec(
    private val keyword: String,
    private val args: List<KeywordSpecArgument>,
    private val messageCollector: MessageCollector = MessageCollector.NONE
) {
    constructor(spec: String, messageCollector: MessageCollector = MessageCollector.NONE) : this(spec.substringBefore(':'), spec.substringAfter(':', "").let { if (it.isEmpty()) emptyList() else it.split(',').map { KeywordSpecArgument(it) } }, messageCollector)

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    fun matches(expression: IrCall): Boolean {
        return expression.symbol.owner.name.asString() == keyword
    }

    fun process(expression: IrCall, reference: String): PoEntry? {
        when(args.size) {
            0 -> {
                val text = getStringOrNull(expression, 0) ?: return null
                return PoEntry(emptyList(), emptyList(), listOf(reference), flags(text), emptyList(), null, text, null, emptyList())
            }
            2 -> {
                return if (args[0].isContext) {
                    val text = getStringOrNull(expression, args[1].index - 1) ?: return null
                    PoEntry(emptyList(), emptyList(), listOf(reference), flags(text), emptyList(), getStringOrNull(expression, args[0].index - 1), text, null, emptyList())
                } else {
                    val text = getStringOrNull(expression, args[0].index - 1) ?: return null
                    PoEntry(emptyList(), emptyList(), listOf(reference), flags(text), emptyList(), null, text, getStringOrNull(expression, args[1].index - 1), listOf("", ""))
                }
            }
            else -> {
                return null
            }
        }
    }

    private fun getStringOrNull(expression: IrCall, index: Int): String? {
        // IrMemberAccessExpression.getValueArgument is annotated @DeprecatedForRemovalCompilerApi
        // (ERROR level since Kotlin 2.1.20), so we call it via reflection to keep the plugin
        // source-compatible across compiler versions. IrConst.kind/value, on the other hand, are
        // plain abstract vars in Kotlin 2.3 (verified by javap) and can be accessed directly.
        val arg = try {
            IrCall::class.java.getMethod("getValueArgument", Int::class.javaPrimitiveType)
                .invoke(expression, index)
        } catch (e: Exception) {
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] Failed to call getValueArgument at index $index: ${e.message}")
            return null
        } ?: run {
            messageCollector.report(CompilerMessageSeverity.INFO, "[gettext] arg at index $index is null")
            return null
        }

        return if (arg is IrConst && arg.kind == IrConstKind.String) {
            arg.value as? String
        } else {
            null
        }
    }

    private fun flags(text: String): String? {
        return if ('{' in text) "java-format" else null
    }
}
