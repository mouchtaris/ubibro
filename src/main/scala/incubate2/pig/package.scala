package incubate2

import scala.reflect.ClassTag
import pkg._

package object Pig {

  final case class pig[t](override val toString: String)

  object pig {
    implicit def fromString[t](v: String): pig[t] = pig[t](v)
    def apply[t: pig]: pig[t] = implicitly

    implicit def XXX[xxx]: pig[xxx] =
      s" XXX -- PIG UNKNOWN: -- XXX"

    implicit val unit: pig[Unit] = "Unit"
    implicit val int: pig[Int] = "Int"
    implicit val str: pig[String] = "String"
    implicit val dbl: pig[Double] = "Double"
    implicit val flt: pig[Float] = "Float"

    implicit val nil: pig[Nil] = "Nil"
    implicit def htlist[h: pig, t <: List: pig]: pig[h :: t] =
      s"${pig[h]} :: ${pig[t]}"
    implicit def list: pig[List] = "List"
  }

  def apply[t: pig] = pig.apply[t]

}
