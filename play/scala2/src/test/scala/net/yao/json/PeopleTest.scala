package net.zhenglai
package net.yao.json

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, PropertyNamingStrategies}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source


class PeopleTest extends AnyFunSuite {


  //  val objectMapper = new ObjectMapper() with ScalaObjectMapper
  val objectMapper = new ObjectMapper()

  objectMapper.registerModule(DefaultScalaModule)
  objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

  test("parse one people") {
    val people = objectMapper.readValue(Source.fromResource("json/1-one-people.json").mkString,
      new TypeReference[People] {})
    assert(people.id == 1)
    assert(people.hairColor == "black")
    assert(people.name == "zhang san")
    assert(people.age == 10)
    assert(people.favFruit.size == 2)
    assert(people.favFruit.head == "apple")

    assertDoesNotCompile("people.id = 1")
  }

  test("parse more people") {
    val peoples = objectMapper.readValue(Source.fromResource("json/2-more-people.json").mkString,
      new TypeReference[List[People]] {})
    assert(peoples.nonEmpty)
    assert(peoples.head.id == 1)
    assert(peoples.head.favFruit.isEmpty)
  }
}
