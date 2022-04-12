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
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class GettextExtractor(
    private val messageCollector: MessageCollector,
    private val poEntries: MutableList<PoEntry>,
    private val keywords: List<KeywordSpec>,
    private val relativePath: String,
    private val fileEntry: IrFileEntry,
) : IrElementTransformerVoid() {
    override fun visitCall(expression: IrCall): IrExpression {
        super.visitCall(expression)

        val info = keywords.firstOrNull { it.matches(expression) }?.process(expression, "$relativePath:${fileEntry.getLineNumber(expression.startOffset) + 1}")
        if (info != null) {
            poEntries.add(info)
            messageCollector.report(CompilerMessageSeverity.OUTPUT, "[gettext] ${info.references[0]} \"${info.text}\"")
        }

        return expression
    }
}