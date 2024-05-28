package com.github.hrothwell.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import jakarta.inject.Singleton

@Singleton
class ObjectMapperListener : BeanCreatedEventListener<ObjectMapper> {
  override fun onCreated(event: BeanCreatedEvent<ObjectMapper>): ObjectMapper {
    println("configuring OM bean")
    return configure(event.bean)
  }

  fun configure(objectMapper: ObjectMapper): ObjectMapper{
    objectMapper.registerKotlinModule()
    objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    return objectMapper
  }
}
