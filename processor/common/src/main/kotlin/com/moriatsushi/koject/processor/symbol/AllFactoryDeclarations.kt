package com.moriatsushi.koject.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.moriatsushi.koject.internal.identifier.StringIdentifier
import com.moriatsushi.koject.processor.code.Names

internal class AllFactoryDeclarations(
    private val map: Map<StringIdentifier, FactoryDeclaration>,
) {
    companion object {
        fun of(resolver: Resolver): AllFactoryDeclarations {
            @OptIn(KspExperimental::class)
            val all = resolver
                .getDeclarationsFromPackage(Names.factoryPackageName)
                .filterIsInstance<KSClassDeclaration>()
                .map { FactoryDeclaration(it) }
                .associateBy { it.identifier }
            return AllFactoryDeclarations(all)
        }
    }

    val all = map.values.sortedBy { it.identifier.value }
    val normals = all.filter { !it.isSingleton }
    val singletons = all.filter { it.isSingleton }

    fun get(identifier: StringIdentifier): FactoryDeclaration {
        return getOrNull(identifier) ?: error("not found : $identifier")
    }

    fun getOrNull(identifier: StringIdentifier): FactoryDeclaration? {
        return map[identifier]
    }
}
