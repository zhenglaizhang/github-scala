package net.zhenglai
package basic

// Unit is an AnyVal type, but involves no storage at all
//  - Java void is a keyword
//  - Unit is a real type with one literal value () and we rarely use that value explicitly
//  - Unit behaves like a tuple with zero elements ()
//  - Name unit comes from algegra, where adding the unit value to any value returns the original value

@main def upperbounds() = {
  val xs : Seq[Float] = Seq(1, 1.1f)
  val xs2 : Seq[Double] = Seq(1.1f, 1.1)
  val xs3 : Seq[AnyVal] = Seq(true, 1)
  val xs4 : Seq[Int] = Seq(1, 'a')
}