package com.moriatsushi.koject.processor.factory

import com.google.devtools.ksp.processing.Resolver
import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.processor.file.FileGenerator
import com.moriatsushi.koject.processor.symbol.ProviderDeclaration

internal class FactoryGenerator(
    private val fileGenerator: FileGenerator,
    private val factoryFileSpecFactory: FactoryFileSpecFactory,
) {
    fun generate(resolver: Resolver) {
        searchProviders(resolver).forEach {
            processNode(it)
        }
    }

    private fun searchProviders(resolver: Resolver): Sequence<ProviderDeclaration> {
        return resolver.getSymbolsWithAnnotation(Provides::class.qualifiedName!!)
            .mapNotNull { ProviderDeclaration.of(it) }
    }

    private fun processNode(provider: ProviderDeclaration) {
        val fileSpec = factoryFileSpecFactory.create(provider)
        fileGenerator.createNewFile(fileSpec, false)
    }
}
