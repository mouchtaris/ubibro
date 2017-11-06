package incubate2
package list_pigs

import
  list._


trait ListPigs {
  this: Any
    with pig.PigContext
  =>

  @`inline` final implicit val nil: pig[Nil] = "Nil"
  @`inline` final implicit def htlist[h: pig, t <: List: pig]: pig[h :: t] =
    s"${pig[h]} :: ${pig[t]}"
  @`inline` final implicit def list: pig[List] = "List"
}

