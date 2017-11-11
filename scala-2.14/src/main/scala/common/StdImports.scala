package common

trait StdImports {

  type AnyRef = scala.AnyRef
  type Any = scala.Any
  type AnyVal = scala.AnyVal
  type Array[a] = scala.Array[a]
  type Boolean = scala.Boolean
  type Double = scala.Double
  type Float = scala.Float
  type Int = scala.Int
  type Nothing = scala.Nothing
  type Option[a] = scala.Option[a]
  type StringBuilder = scala.StringBuilder
  type Unit = scala.Unit
  type Vector[a] = scala.Vector[a]
  type Seq[a] = scala.Seq[a]
  type uList[a] = scala.List[a]
  val uList = scala.List
  val Console = scala.Console
  val Range = scala.Range
  val StringContext = scala.StringContext
  val Vector = scala.Vector
  val Some = scala.Some
  val None = scala.None

  import scala.{Predef ⇒ p}
  type <:<[a, b] = p.<:<[a, b]
  type =:=[a, b] = p.=:=[a, b]
  type DummyImplicit = p.DummyImplicit
  type String = p.String

  def implicitly[t](implicit t: t): t = t
  def ??? = p.???

}
