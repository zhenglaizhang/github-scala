import scala.annotation.tailrec

def sortList(xs : List[Int]) : List[Int] = {
  ???
}
def insertionSort(xs : List[Int]) : List[Int] = {
  def insertSorted(elem : Int, sortedList : List[Int]) : List[Int] =
    if (sortedList.isEmpty || elem < sortedList.head) elem :: sortedList
    else sortedList.head :: insertSorted(elem, sortedList.tail)
  if (xs.isEmpty || xs.tail.isEmpty) xs
  else insertSorted(xs.head, insertionSort(xs.tail))
}
@main def testSorting() : Unit = {
  println(insertionSort(List(2, 1, 0, 4)))

  // Exception in thread "main" java.lang.StackOverflowError
  // insertionSort((1 to 100000).reverse.toList)
  println(insertSortSmarter((1 to 100000).reverse.toList).take(33))
}

// The solution is to use tail calls, or tail recursion, so that the stack doesn’t crash. Tail recursion is a
// mechanism by which the recursive stack frames are reused, so they don’t occupy additional stack memory. This can
// only happen when recursive calls are the last expressions on their code path
// TODO: support Ordering[A] 
def insertSortSmarter(xs: List[Int]): List[Int] = {
  @tailrec
  def insertTailRec(elem : Int, sortedList : List[Int], accumulator : List[Int]) : List[Int] =
    if (sortedList.isEmpty || elem <= sortedList.head) accumulator.reverse ++ (elem :: sortedList)
    else insertTailRec(elem, sortedList.tail, sortedList.head :: accumulator)

  def sortTailRec(xs: List[Int], accumulator: List[Int]): List[Int] =
    if (xs.isEmpty) accumulator
    else sortTailRec(xs.tail, insertTailRec(xs.head, accumulator, Nil))

  sortTailRec(xs, Nil)
}

//insertTailrec(4, [1,2,3,5], []) ---> else branch --->
//insertTailrec(4, [2,3,5], [1]) ---> else branch --->
//insertTailrec(4, [3,5], [2,1]) ---> else branch --->
//insertTailrec(4, [5], [3,2,1]) ---> first branch --->
//[3,2,1].reverse ++ (4 :: [5]) --->
//[1,2,3,4,5]