package com.github.hrothwell

import io.micronaut.aop.Around
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Type
import io.micronaut.core.annotation.AnnotationMetadata
import io.micronaut.runtime.context.scope.ThreadLocal
import jakarta.inject.Singleton
import kotlin.reflect.KClass

interface ThreadLocalRefreshable {
  fun refresh()
}

/**
 * Refreshes thread local beans of specific types. Does not recreate the beans
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Around
@Type(RefreshThreadLocalInterceptor::class)
annotation class RefreshesThreadLocal(
  val beans: Array<KClass<*>> = []
)

@Singleton
class RefreshThreadLocalInterceptor(
  private val applicationContext: ApplicationContext
) : MethodInterceptor<Any, Any> {

  override fun intercept(context: MethodInvocationContext<Any, Any>?): Any? {
    val annotationData = context?.annotationMetadata
    if (annotationData != null) {
      refreshBeans(annotationData)
    }
    return context?.proceed()
  }

  // TODO I feel like this coule be simpler
  private fun refreshBeans(annotationData: AnnotationMetadata) {
    val beanTypes =
      annotationData.classValues<ThreadLocalRefreshable>(RefreshesThreadLocal::class.java, "beans").toList()
    val beans = applicationContext.getBeansOfType(ThreadLocalRefreshable::class.java).filter { bean ->
      beanTypes.any { beanType -> (beanType.isInstance(bean)) }
    }

    beans.forEach { it.refresh() }
  }
}

@ThreadLocal
class ThreadLocalRefreshableValue : ThreadLocalRefreshable {
  val thread = Thread.currentThread().toString()
  var number = 0

  override fun refresh() {
    this.number++
  }

  // We should see that "thread" stays the same while "random" updates, whether this is done via
  // the current "refresh" or destroying and recreating it (probably the ideal solution for supporting DI)
  fun getCurrentValues(): Map<String, String> {
    return mapOf("thread" to thread, "number" to "$number")
  }

  override fun toString(): String {
    return "$thread - ${this.number}"
  }
}

@Singleton
open class TestClassForThreadLocalRefreshable(
  private val threadLocalRefreshableValue: ThreadLocalRefreshableValue
) {

  @RefreshesThreadLocal(beans = [ThreadLocalRefreshableValue::class])
  open fun getRandomValue(): Map<String, String> {
    println(threadLocalRefreshableValue.getCurrentValues())
    return threadLocalRefreshableValue.getCurrentValues()
  }
}
