import scala.annotation.implicitNotFound


//
//object interpretation {
//
//  trait ConstantOut[out, T] {
//    type Out = out
//  }
//
//  object ConstantOut {
//
//    private[this] object instance extends ConstantOut[Nothing, Nothing]
//
//    @inline def apply[out, T](): ConstantOut[out, T] =
//      instance.asInstanceOf[ConstantOut[out, T]]
//
//    @inline implicit def any[out, T]: ConstantOut[out, T] =
//      apply()
//
//  }
//
//  trait Interpretation[T] {
//    final type E = T
//    type In[_]
//    type Out[_ <: In[_]] = { type Out }
//    def apply[a](a: a)(implicit ev: E, in: In[a], out: Out[In[a]]): out.Out
//  }
//
//}
//
//
//object stdinterp {
//
//  final implicit class Name[t](val name: String) extends AnyVal {
//    @inline override def toString: String = name
//  }
//
//  object Name {
//    final implicit val Int: Name[Int] = "Int"
//
//    final implicit val String: Name[String] = "String"
//  }
//
//  import
//    Known._,
//    interpretation._,
//    disjunction._,
//    stdinterp._
//
//  final case class StdInterpretation[T: Name]() extends Interpretation[T] {
//    type In[a] = a <:< T
//
//    type Out[_ <: In[_]] = ConstantOut[T, _]
//
//    @inline def apply[a](a: a)(implicit ev: T, in: In[a], out: Out[In[a]]): out.Out = a
//
//    override def toString: String = s"StdInterpretation[${known[Name[T]].name}]"
//  }
//
//  implicit def stdInterpretation[T: Name](
//    implicit
//    ev: (T <:< Int) || (T <:< String)
//  ): StdInterpretation[T] =
//    StdInterpretation()
//
//}
//
//object orinterp {
//  import
//    Known._,
//    interpretation._,
//    stdinterp._,
//    disjunction._,
//    conjunction._
//
//  trait OrIEvidence[a, b] {
//    type Out
//  }
//
//  object OrIEvidence {
//
//    final case class Evidence[a, b, out]() extends orinterp.this.OrIEvidence[a, b] { type Out = out }
//
//    implicit def evidenceA[a, b](implicit ev: t.Out[a]#t[a || b]): Evidence[a, b, a] =
//      Evidence()
//
//    implicit def evidenceB[a, b](implicit ev: t.Out[b]#t[a || b]): Evidence[a, b, b] =
//      Evidence()
//
//  }
//
//  final case class OrInterpretation[
//    ca,
//    cb,
//    ia <: Interpretation[ca],
//    ib <: Interpretation[cb],
//    ain[_],
//    bin[_]
//  ](
//    interpa: ia,
//    interpb: ib,
//    evidence: OrIEvidence[ca, cb]
//  ) extends Interpretation[ca || cb] {
//
//    type In[a] = ain[a] && bin[a]
//
//    type Out[_ <: In[_]] = ConstantOut[evidence.Out, _]
//
//    def apply[a](a: a)(implicit ev: E, in: In[a], out: Out[In[a]]): out.Out = ???
//
//  }
//
//  implicit def orInterpretation[ca, cb](
//    implicit
//    interpa: Interpretation[ca],
//    interpb: Interpretation[cb]
//  ): OrInterpretation[
//    ca, cb,
//    interpa.type,
//    interpb.type,
//    interpa.In,
//    interpb.In
//  ] =
//    OrInterpretation(interpa, interpb, ???)
//
//}
//
//object istype {
//  import
//    interpretation._,
//    Known._
//
//  @implicitNotFound("Cannot prove ${A} <<: ${B}")
//  final case class IsType[A, B](
//    a2b: A ⇒ B
//  )
//
//  type <<:[a, b] = a `IsType` b
//
//  object IsType {
//
//    @inline implicit def isType[a, b](implicit ev: a =:= b): IsType[a, b] =
//      IsType(ev)
//
//  }
//
//}
//
//object istypeinterp {
//  import
//    interpretation._,
//    istype._,
//    conjunction._
//
//  final case class IsTypeInterpretation[A, B](
//  ) extends Interpretation[IsType[A, B]] {
//
//    type In[a] = a <:< A
//
//    type Out[_ <: In[_]] = ConstantOut[B, _]
//
//    def apply[a](a: a)(implicit ev: E, in: In[a], out: Out[In[a]]): out.Out =
//      ev.a2b(in(a))
//
//  }
//
//  implicit def isTypeInterpretation[a, b]: IsTypeInterpretation[a, b] =
//    IsTypeInterpretation()
//
//}
//
//
//
//object incubate {
//
//  final implicit class Tappin[a](val self: a) extends AnyVal {
//    def tap[b](f: a ⇒ b): b = f(self)
//
//    def stap(f: a ⇒ Unit): a = {
//      f(self);
//      self
//    }
//
//    def sp: a = this stap println
//  }
//
//  import
//    istype._,
//    Known._,
//    interpretation._
//
//  trait Typ[t]
//
//  def uri(s: String): Uri = Uri create s
//
//  final implicit case object TInt extends Typ[Int]
//
//  final implicit case object TString extends Typ[String]
//
//  final implicit case object TUri extends Typ[Uri]
//
//}
//
//object foldinterp {
//  import
//    interpretation._,
//    list._,
//    typeops._
//}
//
//object probe
//
object Main {
//  import
//    istype._,
//    list._,
//    typeops._,
//    Known._,
//    Pig._,
//    incubate._,
//    disjunction._,
//    interpretation._,
//    orinterp._,
//    foldinterp._,
//    istypeinterp._
//
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println(msg)
//    type isint[t] = IsType[t, Int]
//    type l0 = String :: Uri :: Int :: Unit :: Nil
//    val l0 = "Hello" :: uri("http://patron.gallery") :: 12 :: () :: Nil
//    val lm = known[tmap[isint, l0]]
//    val lf = known[tfold[||, lm.Out]]
//    println("****************************")
//    print  ("* "); pig[lm.Out].sp
//    print  ("* "); pig[lf.Out].sp
//    println("****************************")
//
//    known[Interpretation[IsType[Int, Int]]]
//    known[Interpretation[IsType[String, Int]]]
//    known[Interpretation[Int <<: Int]].apply(12).sp
//    //known[Interpretation[Int <<: Int]].sp.apply(12)
//    //known[Interpretation[(Int <<: Int) || (Int <<: Int)]].sp.apply(12)
  }
//
  def msg = "I was compiled by dotty :)"

}
