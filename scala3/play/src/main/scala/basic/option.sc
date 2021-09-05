// Never write methods that can return null, instead returns Option
val m = Map.empty[Int, String]
m.get(1) // None
m.getOrElse(1, "one")
// m(1) // NoSuchElementException


import java.util.HashMap as JHashMap
val jhm = JHashMap[String, String]()
jhm.put("one", "1")
jhm.get("one")
// Null is subtype of all AnyRef types
val x: String | Null = jhm.get("one")
val y: String | Null = jhm.get("two")
// union type
y match {
  case _: Null => "null"
  case s: String => "a str"
}