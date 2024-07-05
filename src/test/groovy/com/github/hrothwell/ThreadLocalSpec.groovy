package com.github.hrothwell

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class ThreadLocalSpec extends Specification {

  @Inject
  TestClassForThreadLocalRefreshable test

  def 'bean for same thread is refreshed every call'() {
    when:
    def results = (1..10).collect {
      test.getRandomValue()
    }
    def expectedThread = Thread.currentThread().toString()

    then:
    0 * _
    results.every { it.get('thread') == expectedThread }
    results.collect { it.get('number') }.flatten().unique().size() == 10
  }

  def 'when called from different threads value is associated with the corresponding thread'() {
    when:
    List<Map<String, String>> thread1Results = null
    List<Map<String, String>> thread2Results = null
    def thread1 = Thread.start {
      thread1Results = (1..10).collect {
        test.getRandomValue()
      }
    }

    def thread2 = Thread.start {
      thread2Results = (1..10).collect {
        test.getRandomValue()
      }
    }

    thread1.join()
    thread2.join()

    def thread1Key = thread1Results[0].get('thread')
    def thread2Key = thread2Results[0].get('thread')

    then:
    0 * _
    thread1Key != thread2Key
    thread1Results.every { it.get('thread') == thread1Key }
    thread2Results.every { it.get('thread') == thread2Key }
    thread1Results.collect { it.get('number') }.flatten().unique().sort() == thread2Results.collect { it.get('number') }.flatten().unique().sort()
  }
}
