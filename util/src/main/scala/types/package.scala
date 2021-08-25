package net.zhenglai

package object types {
  type <=[B, A] = A => B
  type Error = RuntimeException
  type ActualResult[B] = B
  type Or[A, B] = Either[A, B]
  type PossibleResult[A] = ActualResult[A] Or Error
}
