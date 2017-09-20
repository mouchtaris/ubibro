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
  type List = (a :: b) forSome {

    type a
    type b <: (c :: d) forSome {

      type c
      type d <: (e :: f) forSome {

        type e
        type f // probably no use to go deeper than this

      }

    }

  }

  /**
    * A type alias for [[Cons]].
    * @tparam a see [[Cons]]
    * @tparam b see [[Cons]]
    */
  type ::[a, b <: List] = Cons[a, b]

}
