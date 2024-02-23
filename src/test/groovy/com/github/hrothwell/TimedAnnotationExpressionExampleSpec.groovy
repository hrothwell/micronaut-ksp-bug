package com.github.hrothwell

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest
class TimedAnnotationExpressionExampleSpec extends Specification{

    @Inject
    EmbeddedApplication<?> app

    @Unroll
    def 'calling method should make metric available with right tags'(){
        given:
        def context = app.getApplicationContext()
        def meterRegistry = context.getBean(MeterRegistry)
        def bean = context.getBean(type)

        when:
        bean.doSomething()

        then:
        def meter = meterRegistry.getMeters().find{ it.id.name == name }
        meter.id.tags.contains(tag)

        where:
        type                             | name                               | tag
        TimedAnnotationExpressionExample | "TimedAnnotationExpressionExample" | Tag.of("method", "TimedAnnotationExpressionExample.doSomething") // fails
        TimedThatDoesWork                | "working_metric"                   | Tag.of("method", "doSomething") // passes
    }
}
