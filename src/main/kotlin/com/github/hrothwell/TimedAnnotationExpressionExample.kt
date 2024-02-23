package com.github.hrothwell

import io.micrometer.core.annotation.Timed
import jakarta.inject.Singleton


@Singleton
open class TimedAnnotationExpressionExample {

    protected val className = this.javaClass.simpleName

    @Timed(value = "my_metric", extraTags = ["method", "#{this.className}.doSomething"])
    open fun doSomething(){
        for(i in 1..10000){
            i * i
        }
    }
}