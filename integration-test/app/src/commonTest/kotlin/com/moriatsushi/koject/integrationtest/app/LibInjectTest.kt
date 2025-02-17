package com.moriatsushi.koject.integrationtest.app

import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.inject
import com.moriatsushi.koject.integrationtest.lib.LibClass1
import com.moriatsushi.koject.integrationtest.lib.LibClass2
import com.moriatsushi.koject.integrationtest.lib.LibClass3
import com.moriatsushi.koject.integrationtest.lib.checkInternalClassInject
import com.moriatsushi.koject.start
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class LibInjectTest {
    @AfterTest
    fun clear() {
        Koject.stop()
    }

    @Test
    fun successInject() {
        Koject.start()

        val libClass1 = inject<LibClass1>()
        assertIs<LibClass1>(libClass1)

        val libClass2 = inject<LibClass2>()
        assertIs<LibClass2>(libClass2)

        val libClass3 = inject<LibClass3>()
        assertIs<LibClass3>(libClass3)
    }

    @Test
    fun successInject_internal() {
        Koject.start()

        checkInternalClassInject()
    }
}
