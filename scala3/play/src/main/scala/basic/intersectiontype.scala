// todo intersection type under variance 

package net.zhenglai
package basic

// While you can think of union types as “either A or B”, 
// intersection types can be read as “both A and B”

// what if the two types share a method definition? 
// The answer is that the compiler doesn’t care. 
// The real type that will be passed to such a method will need to solve the conflict. 
// In other words, the real type that will be used will only have a single implementation of that method

trait Camera {
    def takePhoto(): Unit = println("snap")
    def charge(): Unit = println("charge")
}
trait Phone {
    def makeCall(): Unit = println("ring")
    def charge(): Unit = println("charge")
}

def useSmartDevice(sp: Camera & Phone): Unit = {
  sp.takePhoto()
  sp.makeCall()
}

class Smartphone extends Camera with Phone {
  override def charge(): Unit = super.charge()
}

transparent trait Flag
// Flag wont be part of the inferred type
// other transparent trait scala.Product, java.lang.Serializable, and java.lang.Comparable

trait T1A
trait T2B
class B

class C1 extends B with T1A with T2B

// Scala3 new syntax
class C extends B, T1A, T2B

@main def intersectiontype() = {
  // with as a type operator has been deprecated; use & instead
  // val b : B with T1 with T2 = new B with T1 with T2
  val b2 : B & T1 & T2 = new B with T1 with T2
  // println ( b.getClass )
  println ( b2.getClass )
  useSmartDevice(new Smartphone)
}

trait HostConfig 
trait HostController { 
  def get: Option[HostConfig]
}
trait PortConfig 
trait PortConroller {
  def get: Option[PortConfig]
}

// intersection type play nice with variance
// Option[A] & Option[B] is the same as Option[A & B]
def getConfigs(controller: HostController & PortConroller): Option[HostConfig & PortConfig] = controller.get
// Because the argument is of type HostController & PortController, 
// any real type that can extend both HostController and PortController must 
// implement the get method such that it returns both an Option[HostConfig] and Option[PortConfig]. 
// The only solution is to make get return Option[HostConfig] & Option[PortConfig]