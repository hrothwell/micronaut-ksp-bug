package com.github.hrothwell

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

abstract class Foo(
  val value: String = ""
) {
  abstract val thing: String
}

// Will not compile with KSP
//@EachProperty(value = "foo")
//open class FooImplEachProperty @ConfigurationInject constructor(
//  @Parameter("name") val name: String,
//  value: String
//): Foo(value = value){
//  override val thing: String = "thing"
//}

@Singleton
open class FooImplSingleton(
  @Value("\${foo.one.value}")
  value: String
): Foo(value = value){
  override val thing: String = "thing"
}
