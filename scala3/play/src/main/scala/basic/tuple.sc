val tup1: (String, Int, Double) = ("hello", 1, 1.2)
tup1._1
tup1._2
tup1._3
tup1(0)
tup1(1)
tup1(2)

// pattern matching
val (s, i, d) = tup1

// 2 elements tuple is called `pairs`
1 -> "one"
(1, "one")
Tuple2(1, "one")