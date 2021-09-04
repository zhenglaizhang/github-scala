package net.zhenglai
package types

case class Money(value: BigDecimal)
case object Money {
  // call the auto generated apply(value: BigDecimal)
  def apply(d: Double): Money = apply(BigDecimal(d))
  def apply(s: String): Money = apply((BigDecimal(s.toDouble)))
}
