package incubate3

import
  list._,
  Console.{ println ⇒ cprintln }

object hell0 {

  object tag {

    trait Tag {
      sealed trait tag
    }

    final case class Tagger[T](obj: T) {
      def apply[tag <: Tag](implicit tag: tag): T with tag.tag =
        obj.asInstanceOf[T with tag.tag]
    }

    def apply[T](obj: T) = Tagger(obj)
  }

}

object Incubator {

  def entry(args: Array[String]): Unit = {
    cprintln(Nil)
  }

}
