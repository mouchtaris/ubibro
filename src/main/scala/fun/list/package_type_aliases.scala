package fun.list

trait package_type_aliases extends Any {

  /**
    * A type alias to specify generic lists. Usually useful
    * as a type parameter bound.
    *
    * {{{
    *   def acceptList[l <: List](l: l): l.type = l
    * }}}
    */
  type List = _ :: (_ <: (_ :: _))

  /**
    * A type alias for [[Cons]].
    * @tparam a see [[Cons]]
    * @tparam b see [[Cons]]
    */
  type ::[a, b <: List] = Cons[a, b]

}
