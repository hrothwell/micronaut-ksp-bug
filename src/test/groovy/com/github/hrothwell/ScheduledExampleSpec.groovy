package com.github.hrothwell

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class ScheduledExampleSpec extends Specification {
    @Inject
    EmbeddedApplication<?> application

    def 'app starts and bean is found'(){
        expect:
        application.getApplicationContext().getBean(ScheduledExample) != null
    }
}
