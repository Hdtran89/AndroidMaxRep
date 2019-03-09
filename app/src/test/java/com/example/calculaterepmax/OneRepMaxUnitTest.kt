package com.example.calculaterepmax

import android.content.Context
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OneRepMaxUnitTest {
    @Mock
    private lateinit var mockContext: Context
    @Test
    fun calculateOneRepMax(){
        val weight = 0
        val reps = 10
        val product = weight * (36/(37-reps))
        Assert.assertEquals(0, product)
    }
}