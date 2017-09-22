package fun
package list_typelevel

import list._
import typelevel.predicate.known._

import org.scalatest._

class ForAllSpec extends FlatSpec with Matchers {

  trait clue[_]
  implicit object clue1 extends clue[Int]
  implicit object clue2 extends clue[String]
  implicit object clue3 extends clue[Short]
  implicit object clue4 extends clue[Double]
  implicit object clue5 extends clue[Unit]

  type li = Int :: String :: Short :: Double :: Unit :: Nil

  def fa(implicit f: ForAll[li, clue]): f.type = f
  fa

}
