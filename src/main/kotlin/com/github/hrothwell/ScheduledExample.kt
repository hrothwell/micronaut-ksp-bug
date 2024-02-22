package com.github.hrothwell

import io.micronaut.context.annotation.Value
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton

abstract class AbstractScheduledExample {
    abstract val cron: String

    @Scheduled(cron = "#{this.cron}")
    fun scheduledRun() {
        execute()
        println("job ran")
    }

    abstract fun execute()
}

@Singleton
class ScheduledExample(
    @Value("\${my.cron}")
    override val cron: String,
): AbstractScheduledExample(){
    override fun execute() {
        println("execute")
    }
}

@Singleton
class NoAbstractInvolved(
    // if private it cannot compile
    @Value("\${my.cron}")
//    private val cron: String,
    // if non-private it can be found but has same error as abstract
//    protected val cron: String,
){


    @Scheduled(cron = "#{this.cron}")
    fun scheduledRun() {
        println("job ran")
    }
}