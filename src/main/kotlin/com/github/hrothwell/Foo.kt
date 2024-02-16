package com.github.hrothwell

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter

abstract class Foo(
  val value: String = ""
) {
  abstract val thing: String
}

@EachProperty(value = "foo")
open class FooImpl @ConfigurationInject constructor(
  @Parameter("name") val name: String,
  value: String
): Foo(value = value){
  override val thing: String = "thing"
}
