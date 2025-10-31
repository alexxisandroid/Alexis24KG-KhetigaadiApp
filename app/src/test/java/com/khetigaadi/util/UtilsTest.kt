package com.khetigaadi.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class UtilsTest {
    @ParameterizedTest
    @MethodSource("priceConvertData")
    fun priceConvert(expected: String, price: String) {
        Assertions.assertEquals(expected, Utils.priceConvert(price))
    }

    companion object {
        @JvmStatic
        fun priceConvertData(): Stream<Arguments> {
            return Stream.of(Arguments.of("100", "100"),
                Arguments.of("1,000", "1000"),
                Arguments.of("10,000", "10000"))
        }
    }
}