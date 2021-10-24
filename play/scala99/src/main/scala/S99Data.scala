trait S99Data {
  val symbolList = List(Symbol("a"), Symbol("a"), Symbol("a"), Symbol("a"), Symbol("b"), Symbol("c"), Symbol("c"),
    Symbol("a"), Symbol("a"), Symbol("d"), Symbol("e"), Symbol("e"), Symbol("e"), Symbol("e"))
  val symbolsEncoded = List((4, Symbol("a")), (1, Symbol("b")), (2, Symbol("c")), (2, Symbol("a")), (1, Symbol("d")),
    (4, Symbol("e")))
  val symbolList2 = List(Symbol("a"), Symbol("b"), Symbol("c"), Symbol("c"), Symbol("d"))
  val charList = List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k')
}
