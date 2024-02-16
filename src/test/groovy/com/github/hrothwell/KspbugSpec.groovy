package com.github.hrothwell

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject

@MicronautTest
class KspbugSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void 'loads singleton'() {
        when:
        def bean = application.getApplicationContext().getBean(MyAbstractClassImplSingleton)

        then:
        bean.thing == 'thing'
        bean.value == 'value-one'
    }

    // Test not passable as KSP errors
//    void 'should load each bean for propperty'() {
//        when:
//        def beans = application.getApplicationContext().getBeansOfType(MyAbstractClassImplEachProperty)
//
//        then:
//        beans.size() == 2
//        def beanOne = beans.find{ it.name == 'one' }
//        beanOne.thing == 'thing'
//        beanOne.value == 'value-one'
//        def beanTwo = beans.find{ it.name == 'two' }
//        beanTwo.thing == 'thing'
//        beanTwo.value == 'value-two'
//    }

    void 'should load each bean for propperty'() {
        when:
        def beans = application.getApplicationContext().getBeansOfType(MyInterfaceImplEachProperty)

        then:
        beans.size() == 2
        def beanOne = beans.find{ it.name == 'one' }
        beanOne.thing == 'thing'
        beanOne.value == 'value-one'
        def beanTwo = beans.find{ it.name == 'two' }
        beanTwo.thing == 'thing'
        beanTwo.value == 'value-two'
    }

}
