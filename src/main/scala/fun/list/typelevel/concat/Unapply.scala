package fun
package list
package typelevel
package concat

trait Unapply {

  def unapply[a <: List, b <: List, ab <: List](list: ab)(implicit concat: ConcatInterpretation[a, b, ab]): Option[b] =
    Some(concat(list))

}
