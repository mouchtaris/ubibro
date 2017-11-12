package incubate3
package test

import
  Console.{ println ⇒ cprintln },
  TypeInfo._,
  cross.reflect.Api._

class listopsTest {
  import
    list._,
    incubate3.hell0.listops._

  type L1 = Int :: String :: Unit :: Nil
  Map[Vector, Nil]()
  Map[Vector, Int :: Nil]()
  val lm = Map[Vector, L1]()

  def pt[t: TypeTag] = cprintln(typeinfo[t])

  Fold[scala.Tuple2, L1]()

  val lf = Fold[hell0.evidence.||, lm.Out]()
  pt[lf.Out]
}


