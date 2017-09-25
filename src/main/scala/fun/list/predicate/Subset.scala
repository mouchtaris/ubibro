package fun
package list
package predicate

/**
  * Evidence type, signifying that a list type `a`
  * is a subset of list type `b`, in terms of type
  * containment.
  * @tparam a list type contained in list type `b`
  * @tparam b list type that contains list type `a`
  */
trait Subset[a <: List, b <: List]

/**
  * Provides construction and implicit resolution of evidence
  * of type [[Subset]].
  */
object Subset {

  /**
    * Conenience type alis for a conceptual type [[Subset]]`[Any, Any]`.
    */
  type any = Subset[_ <: List, _ <: List]

  /**
    * The sole instance of type [[Subset]], since it's a type-level type.
    */
  private[this] object instance extends Subset[Nil, Nil]

  /**
    * Construct an instance of type [[Subset]] with the given types.
    * @tparam a list type contained in list type `b`
    * @tparam b list type that contains list type `a`
    * @return a new instance of evidence of type [[Subset]]
    */
  @inline def apply[a <: List, b <: List](): Subset[a, b] =
    instance.asInstanceOf[Subset[a, b]]

  /**
    * Implicit evidence that [[Nil]] is a subset of any
    * list type
    * @tparam list any list type
    * @return evidence [[Subset[Nil, list]]]
    */
  @inline implicit def nilSubset[list <: List]: Subset[Nil, list] =
    apply()

  /**
    * Implicitly provide evidence that list type `a` is a subset
    * of list type `b`.
    * @param evidence
    * @tparam a list type contained in list type `b`
    * @tparam b list type that contains list type `a`
    * @return evidence that `a` is a [[Subset]] of type `b`
    */
  @inline implicit def listSubset[
    a <: List: ForAll.pred[Contains.in[b]#t]#t,
    b <: List
  ]: Subset[a, b] =
    apply()

}
