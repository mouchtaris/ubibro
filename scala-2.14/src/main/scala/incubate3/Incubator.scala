package incubate3

import
  cross.lang._,
  list._,
  Console.{ println ⇒ cprintln },
  TypeInfo._

object hell0 {

  object tag {

    trait Tagverse {
      sealed abstract trait tag extends Any

      type T
      final type t = T & tag

      def apply(t: T): t =
        Tagger(t)(this)
    }

    final case class Tagger[T](obj: T) {
      def apply(tv: Tagverse): T & tv.tag =
        obj.asInstanceOf[T & tv.tag]
    }

    def apply[T](obj: T) = Tagger(obj)
  }

}

object Incubator {
  import
    hell0.tag._

  object X extends Tagverse { type T = Int }
  type X = X.t

  def entry(args: Array[String]): Unit = {
    cprintln(typeinfo[X.t])
    cprintln(typeinfo[Vector[(Unit, Int)]])
    cprintln(Nil)
  }

}
