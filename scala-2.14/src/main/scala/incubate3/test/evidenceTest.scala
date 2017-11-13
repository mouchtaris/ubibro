package incubate3
package test

import
  Console.{ println ⇒ cprintln }

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


  import list._
  implicitly[ForAll[Clue, A :: C :: Nil, _]]

  object D { def f = 23 }
  object E { def f = 48 }
  implicit val ev = D
  cprintln( Or[D.type, E.type]().evidence.f )
  cprintln( Or[D.type, E.type]() apply ("you", 12) )
}

