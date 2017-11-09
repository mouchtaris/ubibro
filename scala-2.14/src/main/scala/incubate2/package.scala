package object incubate2 {

  type Any = scala.Any
  type AnyVal = scala.AnyVal
  type AnyRef = scala.AnyRef
  type Int = scala.Int
  type Float = scala.Float
  type Double = scala.Double
  type Unit = scala.Unit
  type Nothing = scala.Nothing
  type Array[a] = scala.Array[a]
  type Vector[a] = scala.Vector[a]
  val Vector = scala.Vector
  val StringContext = scala.StringContext
  type StringBuilder = scala.StringBuilder
  val Range = scala.Range

  import scala.{ Predef => p }
  type String = p.String
  type =:=[a, b] = p.=:=[a, b]
  type <:<[a, b] = p.<:<[a, b]
  type DummyImplicit = p.DummyImplicit

  def implicitly[t](implicit t: t): t = t
  def ??? = p.???

  val Console = scala.Console
}
