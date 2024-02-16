package com.github.hrothwell

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

abstract class MyAbstractClass(
  val value: String = ""
) {
  abstract val thing: String
}

interface MyInterface {
  val thing: String
}

@Singleton
open class MyAbstractClassImplSingleton(
  @Value("\${foo.one.value}")
  value: String
): MyAbstractClass(value = value){
  override val thing: String = "thing"
}

// Will not compile with KSP
//@EachProperty(value = "foo")
//open class MyAbstractClassImplEachProperty @ConfigurationInject constructor(
//  @Parameter("name") val name: String,
//  value: String
//): MyAbstractClass(value = value){
//  override val thing: String = "thing"
//}

// If an interface is used instead of abstract class, no issues
@EachProperty(value = "foo")
open class MyInterfaceImplEachProperty @ConfigurationInject constructor(
  @Parameter("name") val name: String,
  val value: String
): MyInterface {
  override val thing: String = "thing"
}


