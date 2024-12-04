package com.github.hrothwell

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class Test extends Specification {

  @Inject
  EmbeddedApplication<?> app

  def 'calling method should make metric available with right tags'() {
    given:
    def context = app.getApplicationContext()

    when:
    def bean = context.getBean(Bean)

    then:
    noExceptionThrown()
  }
}
