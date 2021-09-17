package net.zhenglai
package basic


trait Base:
  var str = "Base"
  def m(): String = "Base"

trait T1 extends Base:
  str = str + " T1"
  override def m(): String = "T1 " +  super.m()

trait T2 extends Base:
  str = str + " T2"
  override def m(): String = "T2 " +  super.m()

trait T3 extends Base:
  str = str + " T3"
  override def m(): String = "T3 " +  super.m()

class C2 extends T2:
  str = str + " C2"
  override def m(): String = "C2 " + super.m()

class C3A extends C2 with T1 with T2 with T3:
  str = str + " C3A"
  override def m(): String = "C3A " + super.m()

class C3B extends C2 with T3 with T2 with T1:
  str = str + " C3B"
  override def m(): String = "C3B " + super.m()


@main def line() = {
  val c3a = new C3A
  val c3b = new C3B
  println ( c3a.str ) //
  println ( c3b.str ) //
}