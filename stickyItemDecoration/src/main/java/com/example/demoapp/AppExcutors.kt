package com.example.demoapp

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExcutors {
    companion object {
        fun diskIO(): Executor {
            return Executors.newSingleThreadExecutor()
        }

        fun networkIO(): Executor {
            return Executors.newFixedThreadPool(3)
        }

        fun mainThread(): Executor {
            return MainThreadExecutor()
        }
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}