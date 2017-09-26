package fun
package list
package typelevel

import
  fun.typelevel._

/**
  * Evidence type,, signifying that a type `a` is contained
  * in a list of type `list`.,,
  * @tparam list type of a list
  * @tparam a type that is contained in the list
  */
sealed trait Contains[list <: List, a]

/**
  * Provides constructors and implicit evidence for
  * [[Contains]] evidence.
  */
object Contains {

  /**
    * Convenience type alis for a conceptual type [[Contains]][Any, Any].
    */
  type any = Contains[_ <: List, _]

  /**
    * Convenience type, which can be used in implicit [[Contains]]
    * resolutions.
    *
    * Ex.
    * {{{
    *   def listWithInt[list <: List: Contain.typ[Int]#t](list: list): list = list
    * }}}
    * @tparam a type contained in list
    * @tparam list a list that should contain `a`
    */
  type typ[a] = {
    type t[list <: List] = Contains[list, a]
  }

  /**
    * Convenience type which can be used in implicit [[Contains]]
    * resolutions.
    *
    * Ex.
    * {{{
    *   def typeContainedInList[t: Contains.in[Int :: string :: Nil]#t](t: t): t = t
    * }}}
    * @tparam list a list in which a type is contained
    * @tparam a type that should be contained in list `list`
    */
  type in[list <: List] = {
    type t[a] = Contains.typ[a]#t[list]
  }

  /**
    * The sole instance of [[Contains]], since it's a type-level type.
    */
  private[this] object instance extends Contains[Nil, Nothing]

  /**
    * Construct an instance of [[Contains]] with the given types.
    * @tparam list a list type
    * @tparam a a type
    * @return an instance of evidence [[Contains]]
    */
  @inline def apply[list <: List, a](): Contains[list, a] =
    instance.asInstanceOf[Contains[list, a]]

  /**
    * Implicitly provide evidence of [[Contains]].
    * @tparam list a list that contains type `a`
    * @tparam a a type that is contained in list of type `list`
    * @return evidence for [[Contains]]
    */
  @inline implicit def listContains[a, list <: List: ForAny.pred[IsType.is[a]#t]#t]: Contains[list, a] =
    apply()

}
