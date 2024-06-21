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

/**
 * This should be implemented on ThreadLocal beans only
 */
interface ThreadLocalRefreshable {
  /**
   * refreshes the state of impl. Won't re-inject anything,
   * so in the case that that is desired maybe the bean could simply be destroyed and recreated? That would likely
   * also remove the need for mutating internal values of the bean (my use case focused mostly around date times at time of
   * processing kafka batch messages starting)
   */
  fun refresh()
}


/**
 * Refreshes thread local beans of specific types at the time of this function getting called
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
  // the current "refresh" or destroying and recreating it (probably the ideal solution for supporting DI?)
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
