package fun
package list
package typelevel
package concat

trait InterpretationProvider {

  /**
    * Implicit interpretation for deconstructing a list with Nil.
    * @tparam b a list type
    * @return an interpreter
    */
  implicit def concatInterpretationNil[b <: List: Concat.resultOf[Nil, b]#t]: ConcatInterpretation[Nil, b, b] =
  // Implicit construction
    identity[b] _

  /**
    * Implicit interpretation for any other list concatenation `h :: t :: b`.
    *
    * This requires implicit evidence for the interpretation of the
    * tail of the list,`t :: b`.
    *
    * @param tbconcat interpretation for the concatenation of `t` and `b`
    * @tparam h head type
    * @tparam t tail type of list `a`
    * @tparam b list `b`
    * @tparam tb lists `b` and `t` concatenated
    * @return an interpreter
    */
  implicit def concatInterpretationList[
  h,
  t <: List,
  b <: List,
  tb <: List
  ](
    implicit
    tbconcat: ConcatInterpretation[t, b, tb]
  ): ConcatInterpretation[h :: t, b, h :: tb] =
    (list: h :: tb) ⇒
      tbconcat(list.tail)

}
