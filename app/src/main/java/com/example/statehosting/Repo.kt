package com.example.statehosting

import kotlinx.coroutines.delay
import kotlin.random.Random

class Repo {
    companion object {
        suspend fun getData(): Int {
            delay(2000)
            return Random.nextInt(100, 1000)
        }
    }
}