package example

import fun._,
  list._,
  fun.typelevel._,
  list.typelevel._,
  interpreter._

object poo {

  object ca
  implicit object cb
  object cc
  type ca = ca.type
  type cb = cb.type
  type cc = cc.type

  import interpretation._
  implicit val interpca = Interpretation[ca, Int :: Nil, Int] { i ⇒ i.head }
  implicit val interpcb = Interpretation[cb, String :: Nil, String] { str ⇒ str.head }
  implicit val interpcc = Interpretation[cc, Unit :: Nil, Unit] { _.head }

  type or =
    Or[ ca,
      Or[ cb, cc ]
      ]
  Known[ or ]
  val intr = Known[ Interpretation[or] ]

  Known[ Rest[Int:: Nil] ]
  Known[ Rest[Nil] ]

  def main(args: Array[String]): Unit = {
    println {
      intr.apply( (12 :: Nil) :: (("Fng" :: Nil) :: (() :: Nil) :: Nil) :: Nil)
    }
  }

//  Error:(133, 27) type mismatch;
//  found   : Int :: fun.list.Nil.type :: (String :: fun.list.Nil.type :: (Unit :: fun.list.Nil.type))
//  (which expands to)  fun.list.Cons[fun.list.Cons[Int,fun.list.Nil.type],fun.list.Cons[fun.list.Cons[String,fun.list.Nil.type],fun.list.Cons[Unit,fun.list.Nil.type]]]
//  required: example.poo2.intr.In
//  (which expands to)  fun.list.Cons[ain,fun.list.Cons[bin,Nothing]]
//  intr.apply( (12 :: Nil) :: ( ("Hello") :: Nil) :: ( () :: Nil) )
}
