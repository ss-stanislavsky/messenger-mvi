package com.example.messenger_mvi

import kotlin.random.Random

fun random(from: Int = 100): Int {
    return Random.nextInt(0, from)
}