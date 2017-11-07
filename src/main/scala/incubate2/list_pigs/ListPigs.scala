package incubate2
package list_pigs

import
  list._


trait ListPigs {
  this: Any
    with pigs.PigContext
  =>

  final implicit val nil: pig[Nil] = "Nil"
  final implicit def htlist[h: pig, t <: List: pig]: pig[h :: t] =
    s"${pig[h]} :: ${pig[t]}"
  final implicit def list: pig[List] = "List"

}
