package com.github.hrothwell

import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class TimedAnnotationExpressionExampleSpec extends Specification{

    @Inject
    EmbeddedApplication<?> app

    def 'calling method should make metric available with right tags'(){
        given:
        def context = app.getApplicationContext()
        def meterRegistry = context.getBean(MeterRegistry)
        def callMe = context.getBean(TimedAnnotationExpressionExample)

        when:
        callMe.doSomething()

        then:
        def meter = meterRegistry.getMeters().find{it.id.name == "my_metric" }
        meter.id.tags.contains("TimedAnnotationExpressionExample.doSomething")
    }
}
