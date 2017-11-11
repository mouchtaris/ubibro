package incubate3

import
  cross.lang._,
  list._,
  Console.{ println ⇒ cprintln },
  cross.reflect.Api._,
  incubate3.{ ind ⇒ pkg_ind }

final case class ind(n: Int, sigil: String = "  ") {
  val indent: String = Range inclusive (1, n + 1) map (_ ⇒ sigil) mkString
  def println(o: Any): Unit = { Console.println(indent + o.toString) }
  def next = copy(n = n + 1)
  def withSigil(sig: String) = copy(sigil = sig)
}

object TypeInfo {

  case object Alias {
    def unapply(tt: Type): Boolean =
      tt.dealias != tt
  }

  case object Baseable {
    def unapply(tt: Type): Option[Seq[Type]] =
      tt match {
        case st @ SingleType(_, _) ⇒
          Some {
            st.baseClasses
              .map { st.baseType }
          }
        case _ ⇒
          None
      }
  }

  def typeinfo(tt: Type)(
    implicit
    sb: StringBuilder = new StringBuilder,
    ind: ind = pkg_ind(0),
    mark: String = " |-|"
  ): String = {
    import ind._
    sb ++= indent ++= mark ++= tt.toString ++= "\n"
    //sb ++= tt.getClass.toString ++= "\n"
    tt match {
      case Alias() ⇒
        typeinfo(tt.dealias)(sb, ind, " ~> ")
      case Baseable(bases) ⇒
        bases.foreach { base ⇒
          typeinfo(base)(sb, ind, " => ")
        }
        sb.toString
      case _ ⇒
        tt.typeArgs.foreach { typeinfo(_)(sb, ind.next) }
        sb.toString
    }

  }

  def typeinfo[T](implicit tt: TypeTag[T]): String =
    typeinfo(typeOf[T](tt))
}
import TypeInfo._

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

  object X extends hell0.tag.Tagverse { type T = Int }
  type X = X.t

  def entry(args: Array[String]): Unit = {
    cprintln(typeinfo[X.t])
    cprintln(Nil)
  }

}
