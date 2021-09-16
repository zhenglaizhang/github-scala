package net.zhenglai
package types

trait Observer[State] {
  def receiveUpdate(state: State): Unit
}

trait Subject[State] {
  private var observers: Vector[Observer[State]] = Vector.empty
  def addObserver(observer: Observer[State]): Unit =
    observers.synchronized { observers :+= observer }

  def notifyObservers(state: State): Unit =
    observers foreach (_.receiveUpdate(state))
}
