package incubate3
package test

class evidenceTest {
  import hell0.evidence._

  final case class Clue[a]()

  trait HasClue {
    type This
    implicit val clue: Clue[This] = Clue()
  }

  /*    */ object A extends HasClue { type This = this.type }
  implicit object B
  implicit object C extends HasClue { type This = this.type }

  final type A = A.type
  final type B = B.type
  final type C = C.type

  val bnc = implicitly[ B && C ]
  val boc = implicitly[ B || C ]
  val aob = implicitly[ A || B ]
  val coa = implicitly[ C || A ]
}

