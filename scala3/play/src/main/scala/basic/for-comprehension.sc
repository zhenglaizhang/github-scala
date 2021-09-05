for
    i <- 0 until 10 // generator
    if i % 2 == 0   // guard
do println(i)
// do indicates only site effects are performed


for {
    i <- 0 until 10
    if i % 2 == 0 // guard
    square = i * i // define immutable value
} yield square.toString      // yielding new value


val xs = List(Some(1), None, Some(2))
for {
    case Some(i) <- xs // pattern matching and filter None
} yield i