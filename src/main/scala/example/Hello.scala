package example

import
  fun._,
  list._,
  predicate._,
  typelevel._,
  typelevel.predicate._

import scala.reflect.runtime.universe._

object Hello extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
