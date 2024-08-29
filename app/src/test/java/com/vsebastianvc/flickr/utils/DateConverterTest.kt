package com.vsebastianvc.flickr.utils

import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class DateConverterTest {

    private val dateConverter = DateConverter()

    @Test
    fun testTimestampToDate() {
        val timestamp = 1625097600000L
        val expectedDate = Date(timestamp)
        val result = dateConverter.fromTimestamp(timestamp)
        assertNotNull(result)
        assertEquals(expectedDate, result)

        // Test with null
        assertNull(dateConverter.fromTimestamp(null))
    }

    @Test
    fun testDateToTimestamp() {
        val date = Date(1625097600000L)
        val expectedTimestamp = date.time
        val result = dateConverter.dateToTimestamp(date)
        assertNotNull(result)
        assertEquals(expectedTimestamp, result)

        // Test with null
        assertNull(dateConverter.dateToTimestamp(null))
    }
}
