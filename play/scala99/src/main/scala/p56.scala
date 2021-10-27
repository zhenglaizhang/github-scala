package net.zhenglai

object p56 {
  def isSymmetric[A](tree: Tree[A]): Boolean = tree.isSymmetric

  def main(args: Array[String]): Unit = {
    assert(isSymmetric(Node('a', Node('b'), Node('c'))))
  }
}
