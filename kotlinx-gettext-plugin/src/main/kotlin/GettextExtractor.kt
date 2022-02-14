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

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class GettextExtractor(
    private val messageCollector: MessageCollector,
    private val keywords: List<String>,
    private val relativePath: String,
    private var fileEntry: IrFileEntry,
) : IrElementTransformerVoid() {
    private val myMsgIds: MutableList<MsgId> = mutableListOf()
    val msgIds: List<MsgId> get() = myMsgIds

    override fun visitCall(expression: IrCall): IrExpression {
        super.visitCall(expression)

        val signature = expression.symbol.signature as? IdSignature.CommonSignature
        if (signature?.shortName in keywords) {
            val valueArgument = expression.getValueArgument(0)
            if (valueArgument is IrConst<*> && valueArgument.kind == IrConstKind.String) {
                val reference = "$relativePath:${fileEntry.getLineNumber(expression.startOffset) + 1}"
                val info = MsgId(listOf(reference), null, valueArgument.value.toString())
                myMsgIds.add(info)
                messageCollector.report(CompilerMessageSeverity.OUTPUT, "[gettext] $reference \"${info.text}\"")
            }
        }

        return expression
    }
}