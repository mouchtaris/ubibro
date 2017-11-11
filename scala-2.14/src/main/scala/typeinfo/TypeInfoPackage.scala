package typeinfo

import
  cross.reflect.Api._,
  console.ind,
  ind.{ apply ⇒ pkg_ind }

trait TypeInfoPackage {

  case object Alias {
    def unapply(tt: Type): Boolean =
      tt.dealias != tt
  }

  case object Baseable {
    def unapply(tt: Type): Option[Seq[Type]] =
      if (tt.isSingleType)
        Some {
          tt.baseClasses
            .map { tt.baseType }
        }
      else
        None
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
