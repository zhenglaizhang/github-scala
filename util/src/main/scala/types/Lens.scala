package net.zhenglai
package types

case class Lens[A, B](get: A => B, set: (A, B) => A)
