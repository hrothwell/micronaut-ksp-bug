package com.github.hrothwell.client

import com.github.hrothwell.domain.FooResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("foo")
interface FooClient {

  /**
   * This is non-nullable, would expect that a 404 / null body results in an exception being thrown
   */
  @Get("/foo")
  suspend fun getFoo(): FooResponse
}
