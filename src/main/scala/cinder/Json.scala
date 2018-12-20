package cinder

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, PropertyNamingStrategy}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object Json {
  val objectMapper: ObjectMapper with ScalaObjectMapper = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
}
