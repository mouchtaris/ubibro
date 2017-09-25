package example

import
  fun._,
  list._,
  list.typelevel._,
  fun.typelevel._,
  fun.typelevel.predicate._,
  ops._

import
  scala.reflect.runtime.universe._

object poo {

  trait thinkaboutit {

    abstract class Get[list <: List, t] {
      def apply(l: list): t
    }

    object Get {
      implicit def getFromHead[h, t <: List]: Get[h :: t, h] =
        (l: h :: t) => l.head

      implicit def getFromTail[T, h, t <: List](implicit tGet: Get[t, T]): Get[h :: t, T] =
        (l: h :: t) => tGet(l.tail)
    }

  }

  object User {
    case class Email(self: String)
    case class Password(self: String)
  }
  type User = (User.Email, User.Password)
}

object Hello extends Greeting with App {
  import poo._
  object tai extends thinkaboutit
  import tai._

  println {
    Known[Get[Int :: String :: Unit :: Nil, String]]
      .apply(12 :: "Helllo" :: () :: Nil)
  }

  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
