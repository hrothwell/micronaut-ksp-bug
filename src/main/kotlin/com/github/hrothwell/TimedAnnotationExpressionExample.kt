package com.github.hrothwell

import io.micrometer.core.annotation.Timed
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton


@Singleton
open class TimedAnnotationExpressionExample {

    protected val className = this.javaClass.simpleName


    @Timed(value = "#{this.className}", extraTags = ["method", "#{this.className}.doSomething"]) // treated as evaluated annotation metadata
//    @Timed(value = "my_metric", extraTags = ["method", "#{this.className}.doSomething"]) // not treated as evaluated annotation metadata
    open fun doSomething() {
        for(i in 1..500){
            i * i
        }
    }
}