package com.moriatsushi.koject.processor

import com.moriatsushi.koject.processor.assert.assertCompileSucceed
import com.moriatsushi.koject.processor.assert.assertFileExists
import com.moriatsushi.koject.processor.assert.assertFileTextEquals
import com.moriatsushi.koject.processor.compiletesting.KotlinCompilationFactory
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DIProcessorSingletonTest {
    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder()

    private val compilationFactory = KotlinCompilationFactory()

    @Test
    fun compile() {
        val folder = tempFolder.newFolder()
        val complication = compilationFactory.create(folder)
        complication.sources = listOf(inputCode)
        val result = complication.compile()

        assertCompileSucceed(result)

        val expectedSingletonClass1FactoryFile =
            folder.resolve(expectedSingletonClass1FactoryFilePath)
        assertFileExists(expectedSingletonClass1FactoryFile)
        assertFileTextEquals(
            expectedSingletonClass1FactoryText,
            expectedSingletonClass1FactoryFile,
        )

        val expectedSingletonClass2FactoryFile =
            folder.resolve(expectedSingletonClass2FactoryFilePath)
        assertFileExists(expectedSingletonClass2FactoryFile)
        assertFileTextEquals(
            expectedSingletonClass2FactoryText,
            expectedSingletonClass2FactoryFile,
        )

        val expectedSingletonInterfaceFactoryFile =
            folder.resolve(expectedSingletonInterfaceFactoryFilePath)
        assertFileExists(expectedSingletonInterfaceFactoryFile)
        assertFileTextEquals(
            expectedSingletonInterfaceFactoryText,
            expectedSingletonInterfaceFactoryFile,
        )

        val expectedSingletonHolderFactoryFile =
            folder.resolve(expectedSingletonHolderFactoryFilePath)
        assertFileExists(expectedSingletonHolderFactoryFile)
        assertFileTextEquals(
            expectedSingletonHolderFactoryText,
            expectedSingletonHolderFactoryFile,
        )

        val expectedContainerFile = folder.resolve(expectedContainerFilePath)
        assertFileExists(expectedContainerFile)
        assertFileTextEquals(expectedContainerText, expectedContainerFile)
    }

    private val inputCode = SourceFile.kotlin(
        "Test.kt",
        """
                package com.testpackage

                import com.moriatsushi.koject.Provides
                import com.moriatsushi.koject.Singleton

                @Singleton
                @Provides
                class SingletonClass1

                @Singleton
                @Provides
                class SingletonClass2(
                    private val class1: SingletonClass1
                )

                interface SingletonInterface
                
                @Provides
                @Singleton
                fun provideSingletonInterface(): SingletonInterface {
                    return object : SingletonInterface {}
                }

                @Provides
                class SingletonHolderClass(
                    val singletonClass: SingletonClass2,
                    val singletonInterface: SingletonInterface,
                )
            """,
    )

    private val expectedSingletonClass1FactoryFilePath =
        "ksp/sources/kotlin/com/moriatsushi/koject/generated/factory/" +
            "_com_testpackage_SingletonClass1_Factory.kt"

    private val expectedSingletonClass2FactoryFilePath =
        "ksp/sources/kotlin/com/moriatsushi/koject/generated/factory/" +
            "_com_testpackage_SingletonClass2_Factory.kt"

    private val expectedSingletonInterfaceFactoryFilePath =
        "ksp/sources/kotlin/com/moriatsushi/koject/generated/factory/" +
            "_com_testpackage_SingletonInterface_Factory.kt"

    private val expectedSingletonHolderFactoryFilePath =
        "ksp/sources/kotlin/com/moriatsushi/koject/generated/factory/" +
            "_com_testpackage_SingletonHolderClass_Factory.kt"

    private val expectedContainerFilePath =
        "ksp/sources/kotlin/com/moriatsushi/koject/generated/" +
            "_AppContainer.kt"

    @Language("kotlin")
    private val expectedSingletonClass1FactoryText = """
        |// Generated by Koject. Do not modify!
        |package com.moriatsushi.koject.generated.factory
        |
        |import com.moriatsushi.koject.Singleton
        |import com.moriatsushi.koject.`internal`.InternalKojectApi
        |import com.moriatsushi.koject.`internal`.identifier.Identifier
        |import com.moriatsushi.koject.`internal`.identifier.StringIdentifier
        |import com.testpackage.SingletonClass1
        |import kotlin.Any
        |
        |@InternalKojectApi
        |@Singleton
        |@StringIdentifier("com.testpackage.SingletonClass1")
        |public class _com_testpackage_SingletonClass1_Factory() {
        |    public fun create(): Any = SingletonClass1()
        |
        |    public companion object {
        |        public val identifier: Identifier = Identifier.of<SingletonClass1>()
        |    }
        |}
        |
    """.trimMargin()

    @Language("kotlin")
    private val expectedSingletonClass2FactoryText = """
        |// Generated by Koject. Do not modify!
        |package com.moriatsushi.koject.generated.factory
        |
        |import com.moriatsushi.koject.Singleton
        |import com.moriatsushi.koject.`internal`.InternalKojectApi
        |import com.moriatsushi.koject.`internal`.identifier.Identifier
        |import com.moriatsushi.koject.`internal`.identifier.StringIdentifier
        |import com.testpackage.SingletonClass1
        |import com.testpackage.SingletonClass2
        |import kotlin.Any
        |
        |@InternalKojectApi
        |@Singleton
        |@StringIdentifier("com.testpackage.SingletonClass2")
        |public class _com_testpackage_SingletonClass2_Factory(
        |    @StringIdentifier("com.testpackage.SingletonClass1")
        |    private val provide_com_testpackage_SingletonClass1: () -> Any,
        |) {
        |    public fun create(): Any = SingletonClass2(
        |        provide_com_testpackage_SingletonClass1() as SingletonClass1,
        |    )
        |
        |    public companion object {
        |        public val identifier: Identifier = Identifier.of<SingletonClass2>()
        |    }
        |}
        |
    """.trimMargin()

    @Language("kotlin")
    private val expectedSingletonInterfaceFactoryText = """
        |// Generated by Koject. Do not modify!
        |package com.moriatsushi.koject.generated.factory
        |
        |import com.moriatsushi.koject.Singleton
        |import com.moriatsushi.koject.`internal`.InternalKojectApi
        |import com.moriatsushi.koject.`internal`.identifier.Identifier
        |import com.moriatsushi.koject.`internal`.identifier.StringIdentifier
        |import com.testpackage.SingletonInterface
        |import com.testpackage.provideSingletonInterface
        |import kotlin.Any
        |
        |@InternalKojectApi
        |@Singleton
        |@StringIdentifier("com.testpackage.SingletonInterface")
        |public class _com_testpackage_SingletonInterface_Factory() {
        |    public fun create(): Any = provideSingletonInterface()
        |
        |    public companion object {
        |        public val identifier: Identifier = Identifier.of<SingletonInterface>()
        |    }
        |}
        |
    """.trimMargin()

    @Language("kotlin")
    private val expectedSingletonHolderFactoryText = """
        |// Generated by Koject. Do not modify!
        |package com.moriatsushi.koject.generated.factory
        |
        |import com.moriatsushi.koject.`internal`.InternalKojectApi
        |import com.moriatsushi.koject.`internal`.identifier.Identifier
        |import com.moriatsushi.koject.`internal`.identifier.StringIdentifier
        |import com.testpackage.SingletonClass2
        |import com.testpackage.SingletonHolderClass
        |import com.testpackage.SingletonInterface
        |import kotlin.Any
        |
        |@InternalKojectApi
        |@StringIdentifier("com.testpackage.SingletonHolderClass")
        |public class _com_testpackage_SingletonHolderClass_Factory(
        |    @StringIdentifier("com.testpackage.SingletonClass2")
        |    private val provide_com_testpackage_SingletonClass2: () -> Any,
        |    @StringIdentifier("com.testpackage.SingletonInterface")
        |    private val provide_com_testpackage_SingletonInterface: () -> Any,
        |) {
        |    public fun create(): Any = SingletonHolderClass(
        |        provide_com_testpackage_SingletonClass2() as SingletonClass2,
        |        provide_com_testpackage_SingletonInterface() as SingletonInterface,
        |    )
        |
        |    public companion object {
        |        public val identifier: Identifier = Identifier.of<SingletonHolderClass>()
        |    }
        |}
        |
    """.trimMargin()

    @Language("kotlin")
    private val expectedContainerText = """
        |// Generated by Koject. Do not modify!
        |package com.moriatsushi.koject.generated
        |
        |import com.moriatsushi.koject.`internal`.Container
        |import com.moriatsushi.koject.`internal`.InternalKojectApi
        |import com.moriatsushi.koject.`internal`.identifier.Identifier
        |import com.moriatsushi.koject.generated.factory._com_testpackage_SingletonClass1_Factory
        |import com.moriatsushi.koject.generated.factory._com_testpackage_SingletonClass2_Factory
        |import com.moriatsushi.koject.generated.factory._com_testpackage_SingletonHolderClass_Factory
        |import com.moriatsushi.koject.generated.factory._com_testpackage_SingletonInterface_Factory
        |import kotlin.Any
        |
        |@InternalKojectApi
        |public class _AppContainer : Container {
        |    private val com_testpackage_SingletonClass1: Any by lazy {
        |                _com_testpackage_SingletonClass1_Factory().create()
        |            }
        |
        |    private val com_testpackage_SingletonClass2: Any by lazy {
        |                _com_testpackage_SingletonClass2_Factory(
        |                    { com_testpackage_SingletonClass1 },
        |                ).create()
        |            }
        |
        |    private val com_testpackage_SingletonInterface: Any by lazy {
        |                _com_testpackage_SingletonInterface_Factory().create()
        |            }
        |
        |    private val provide_com_testpackage_SingletonHolderClass: () -> Any by lazy {
        |                _com_testpackage_SingletonHolderClass_Factory(
        |                    { com_testpackage_SingletonClass2 },
        |                    { com_testpackage_SingletonInterface },
        |                )::create
        |            }
        |
        |    public override fun resolve(id: Identifier): Any? = when (id) {
        |        _com_testpackage_SingletonClass1_Factory.identifier -> com_testpackage_SingletonClass1
        |        _com_testpackage_SingletonClass2_Factory.identifier -> com_testpackage_SingletonClass2
        |        _com_testpackage_SingletonHolderClass_Factory.identifier ->
        |                provide_com_testpackage_SingletonHolderClass()
        |        _com_testpackage_SingletonInterface_Factory.identifier -> com_testpackage_SingletonInterface
        |        else -> null
        |    }
        |}
        |
    """.trimMargin()
}
