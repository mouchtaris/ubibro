import
  java.net.{URI ⇒ Uri},
  list._,
  disjunction._


package object pig extends AnyRef
  with VeryLowPriorityPig
{

  final implicit class pig[t](@inline override val toString: String) extends AnyVal

  @inline implicit val int: pig[Int] = "Int"
  @inline implicit val string: pig[String] = "String"
  @inline implicit val uri: pig[Uri] = "Uri"
  @inline implicit val unit: pig[Unit] = "Unit"
  @inline implicit val nil: pig[Nil] = "Nil"

  @inline def apply[t: pig]: pig[t] = implicitly

  @inline implicit def list[h: pig, t <: List: pig]: pig[h :: t] = s"${pig[h]} :: ${pig[t]}"
  @inline implicit def istype[a: pig, b: pig]: pig[a <:< b] = s"${pig[a]} <:< ${pig[b]}"
  @inline implicit def disjunction[a: pig, b: pig]: pig[a || b] = s"${pig[a]} || ${pig[b]}"

}
