class Person(val name: String, age: Int):
    def this() = this("unknown", 0) // auxiliar constructor

// universal apply methods
// apply is called constructor proxies
Person().name

import java.io.File

// no new needed, even for Java classes
val f = File("readme.md")


trait Welcome:
    def hello(name: String): Unit

// anonymous class require new
val hello = new Welcome:
    override def hello(name : String) : Unit = println(s"Hello $name")