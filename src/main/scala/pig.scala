import scala.language.strictEquality
import core._
import typeops._
import types._

object pig {

  final implicit class pig[t](
    @`inline` override val toString: String
  ) extends AnyVal

  final implicit class pig1[t[_]](
    @`inline` override val toString: String
  ) extends AnyVal

  final implicit class pig2[t[_, _]](
    @`inline` override val toString: String
  ) extends AnyVal

  @inline implicit val int: pig[Int] = "Int"
  @inline implicit val str: pig[String] = "String"
  @inline implicit val unit: pig[Unit] = "Unit"
  @inline implicit val float: pig[Float] = "Float"
  @inline implicit val double: pig[Double] = "Double"
  @inline implicit val Nothing: pig[Nothing] = "Nothing"

  object pig {
    inline def apply[t: pig]: pig[t] = implicitly
    inline def of[t: pig](never: => t): pig[t] = implicitly
  }
  object pig1 {
    inline def apply[t[_]: pig1]: pig1[t] = implicitly
  }
  object pig2 {
    inline def apply[t[_, _]: pig2]: pig2[t] = implicitly
  }

  @inline implicit val list: pig[List] = "List"
  @inline implicit val nil: pig[Nil] = "Nil"
  @inline implicit def headtail[h: pig, t <: List: pig]: pig[h :: t] =
    s"${pig[h]} :: ${pig[t]}"

  @inline implicit def conforms[a: pig, b: pig]: pig[a <:< b] =
    s"${pig[a]} <:< ${pig[b]}"
}
