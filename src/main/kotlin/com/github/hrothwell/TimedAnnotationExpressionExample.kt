package com.github.hrothwell

import io.micrometer.core.annotation.Timed
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton


@Singleton
open class TimedAnnotationExpressionExample {

    protected val className = this.javaClass.simpleName


    @Timed(value = "#{this.className}", extraTags = ["method", "#{this.className}.doSomething"]) // treated as evaluated annotation metadata
//    @Timed(value = "TimedAnnotationExpressionExample", extraTags = ["method", "#{this.className}.doSomething"]) // not treated as evaluated annotation metadata
    open fun doSomething() {
        for(i in 1..500){
            i * i
        }
    }
}

// Strictly for proving in tests that metrics can be found/have proper tags
@Singleton
open class TimedThatDoesWork {
    @Timed(value = "working_metric", extraTags = ["method", "doSomething"])
    open fun doSomething() {
        for(i in 1..500){
            i * i
        }
    }
}