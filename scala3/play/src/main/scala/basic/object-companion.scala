
// Singleton pattern in one line
object MySingleton
object ClusterSingleton {
  val MAX_NODES = 20
  def getNumberOfNodes() : Int = ???
}
@main def testsingleton() : Unit = {
  val singleton = ClusterSingleton
  val nodes = ClusterSingleton.getNumberOfNodes()
  val maxNodes = ClusterSingleton.MAX_NODES
}
// In short, it is possible to have a class and an object with the same name in the same file.
// We call these companions.
class Kid(name : String, age : Int) {
  def greet() : String = s" Hi I am $name and I am $age years old"
}
// The object Kid is the companion object of the class Kid.
// Companions have the property that they can access each other’s private fields and methods.
// Their fields’ and methods’ access modifiers are otherwise unchanged.
object Kid {
  val LIKES_VEGETABLES : Boolean = false
}

// In Scala, we separated the code in the class (for instance-dependent logic) and the companion object (for
// instance-independent logic). The secret purpose of a companion object as a best practice is to store “static”
// fields and methods. Because class/object companions can access each other’s private fields and methods, there’s
// some extra convenience for us

// All the logic we can write in Scala (at least in Scala 2) can only exist within a class or an object. Therefore,
// all the logic we write belongs to some instance of some type, which means that Scala is purely object-oriented.
// Scala 3 will change that, because we’ll soon be allowed to write top-level value and method declarations.

// The type of a class is different from the type of its companion object.
// The type of the Kid object is known to the compiler as Kid.type, which is different than Kid (the class name).
// The class/object types are different, but they’re “compatible” to the compiler