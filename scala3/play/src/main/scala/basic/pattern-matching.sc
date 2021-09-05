val iarray = IArray(1, 2, 3, 4, 5)
iarray match
  case a: Array[Int] => a(2) = 300
// a hole
iarray

// pattern matching can only occur on values of type Matchable, not Any