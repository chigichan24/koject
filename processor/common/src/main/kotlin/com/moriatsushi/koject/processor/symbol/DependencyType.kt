package com.moriatsushi.koject.processor.symbol

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.moriatsushi.koject.internal.identifier.StringIdentifier
import com.moriatsushi.koject.processor.analytics.findQualifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class DependencyType(
    private val ksType: KSType,
    private val qualifier: QualifierAnnotation?,
) {
    companion object {
        fun of(node: KSValueParameter): DependencyType {
            val ksType = node.type.resolve()
            val qualifier = node.findQualifier()
            return DependencyType(ksType, qualifier)
        }
    }

    val identifier by lazy {
        StringIdentifier.of(ksType, qualifier)
    }

    fun asTypeName(): TypeName {
        return ksType.toTypeName()
    }
}
