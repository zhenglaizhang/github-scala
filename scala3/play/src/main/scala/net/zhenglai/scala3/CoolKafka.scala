package net.zhenglai
package net.zhenglai.scala3

case class KafkaRequestHeader(
    requestType: String,
    requestVersion: String,
    correlationId: String,
    clientId: String
)
