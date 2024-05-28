package com.github.hrothwell

import com.github.hrothwell.service.FooService
import com.github.tomakehurst.wiremock.WireMockServer
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.ok

@MicronautTest
class ExampleTest extends Specification {

  @Inject
  EmbeddedApplication<?> app

  @Shared
  @AutoCleanup('stop')
  WireMockServer wiremock = new WireMockServer(8081)

  def setupSpec(){
    wiremock.start()
    wiremock.stubFor(get('/foo').willReturn(
      ok().withBody('{"key": "value"}')
    ))
  }

  def 'test we can hit the wiremock server'(){
    given:
    def client = HttpClient.newBuilder().build()
    def request = HttpRequest.newBuilder()
    .uri(URI.create(wiremock.url('/foo')))
    .GET()
    .build()

    expect:
    def response = client.send(request, HttpResponse.BodyHandlers.ofString())
    println(response.body())
  }

  def 'can call to get the response'(){
    given:
    def service = app.getApplicationContext().getBean(FooService)

    when:
    def result = service.example()

    then:
    println(result)
  }
}
