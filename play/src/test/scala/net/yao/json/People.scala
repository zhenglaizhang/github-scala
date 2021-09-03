package net.zhenglai
package net.yao.json

import com.fasterxml.jackson.annotation.JsonProperty

final case class People(
                   id: Int,
                   @JsonProperty("c_name") name: String,
                   height: Int,
                   money: Float,
                   hairColor: String,
                   age:Int = 10,
                   favFruit: List[String] = List.empty
                 )
