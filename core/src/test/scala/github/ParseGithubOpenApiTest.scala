package net.zhenglai
package github

import io.swagger.v3.parser.OpenAPIV3Parser
import org.scalatest.FunSuite


class ParseGithubOpenApiTest extends FunSuite {

  test("parse github open api") {
    val filePath = getClass.getClassLoader
      .getResource("api.github.com.json").getPath
    val openApi = new OpenAPIV3Parser().read(filePath)
    println(openApi.getInfo)
    //    openApi.getPaths.keySet().forEach(println _ )
  }
}
