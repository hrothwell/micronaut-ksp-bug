package com.github.hrothwell

import io.micronaut.cache.annotation.Cacheable
import jakarta.inject.Singleton

@Singleton
open class Bean {

  @Cacheable(condition = "#{ ctx[io.micronaut.context.env.Environment].activeNames.contains('test') }")
  open fun doSomething() {
    println("Doing something")
  }
}
