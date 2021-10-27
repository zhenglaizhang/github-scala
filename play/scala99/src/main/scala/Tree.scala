package net.zhenglai

sealed abstract class Tree[+A] {
  // covers structure but not the content
  def isMirrorOf[B](tree: Tree[B]): Boolean

  def isSymmetric: Boolean

  def addValue[B >: A](vaule: B)(implicit ev: Ordered[B]): Tree[B]
}

case class Node[+A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A] {
  override def toString: String = {
    "T(" + value.toString + " " + left.toString + " " + right.toString + ")"
  }

  override def isMirrorOf[B](tree: Tree[B]): Boolean = tree match {
    case t: Node[B] => left.isMirrorOf(t.right) && right.isMirrorOf(left)
    case _ => false
  }

  override def isSymmetric: Boolean = left.isMirrorOf(right)

  override def addValue[B >: A](vaule: B)(implicit ev: Ordered[B]): Tree[B] = ???
}

case object End extends Tree[Nothing] {
  override def toString: String = "."

  override def isMirrorOf[A](tree: Tree[A]): Boolean = tree == End

  override def isSymmetric: Boolean = true

  override def addValue[B >: Nothing](vaule: B)(implicit ev: Ordered[B]): Tree[B] = ???
}

object Node {
  def apply[A](value: A): Node[A] = Node(value, End, End)
}

