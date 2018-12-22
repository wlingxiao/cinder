package cinder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

trait BaseTest extends FunSuite with Matchers with BeforeAndAfter {

  protected val objectMapper: ObjectMapper with ScalaObjectMapper = Json.objectMapper

}
