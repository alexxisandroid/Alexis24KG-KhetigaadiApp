package com.khetigaadi.commons.utils

import com.khetigaadi.util.Utils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ConstantsTest {
    @ParameterizedTest
    @MethodSource("mobileNumberData")
    fun isValidPhone(expected: Boolean, mobile: String) {
        Assertions.assertTrue(Utils.isValidPhone(mobile))
    }

    companion object {
        @JvmStatic
        fun mobileNumberData(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(true, "8888888888"),
                Arguments.of(true, "+91 9767667670"),
                Arguments.of(true, "9767667670")
            )
        }
    }
}