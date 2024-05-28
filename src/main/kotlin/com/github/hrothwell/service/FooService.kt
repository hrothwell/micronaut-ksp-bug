package com.github.hrothwell.service

import com.github.hrothwell.client.FooClient
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
class FooService(
  private val fooClient: FooClient
) {

  fun example() = runBlocking {
    println("running example")
    val fooResponse = fooClient.getFoo()

//    assert(fooResponse != null)

    println("returning fooResponse")
    fooResponse
  }
}
