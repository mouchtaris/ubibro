package incubator

import fun.typelevel._, fun.list._, fun.interpretation._


object incubation {

  type FC1[f[_, _], a[_], b[_]] = {
    type t[T] = f[ a[T], b[T] ]
  }

  type I[T, in[_], out] = Interpretation[T] {
    type In[a] = in[a]
    type Out = out
  }

  abstract class abi[
    c,
    in[_],
    out
  ] extends Interpretation[c] {
    final type In[a] = in[a]
    final type Out = out
  }

  final case class OrI[
    ca,
    cb,
    ain[_], aout,
    bin[_], bout,
  ](
    interpa: I[ca, ain, aout],
    interpb: I[cb, bin, bout],
  ) extends abi[
    (ca `Or` cb),
    FC1[And, ain, bin]#t,
    (aout, bout),
  ] {
    implicit def ina[a: In]: interpa.In[a] = implicitly[In[a]].a
    implicit def inb[a: In]: interpb.In[a] = implicitly[In[a]].b
    def apply[a: In](a: a): Out = 
      (interpa(a), interpb(a))
    override def toString: String = "I OR(" + interpa + ", " + interpb + ")"
  }

  implicit def orI[
    ca,
    cb,
  ](
    implicit
    interpa: Interpretation[ca],
    interpb: Interpretation[cb],
  ):
    OrI[
      ca, cb,
      interpa.In, interpa.Out,
      interpb.In, interpb.Out,
    ]
  =
    new OrI[
      ca, cb,
      interpa.In, interpa.Out,
      interpb.In, interpb.Out,
    ](interpa, interpb)

}

object Main {
  import incubation._
  import sample._

  def main(args: Array[String]): Unit = println {
    Vector(
      Known[Interpretation[OA]].apply(OA),
      Known[Interpretation[OA]].apply(OB),
      Known[Interpretation[OB]].apply(OA),
      Known[Interpretation[OB]].apply(OB),
      Known[Clue[OA]],
      Known[Clue[OB]],
      Known[Clue[OA] `And` Clue[OB]],
      Known[Clue[OA] `Or` Clue[OB]],
      Known[Interpretation[Or[OA, OB]]],
      Known[Interpretation[Or[OA, OB]]].apply(OA),
    )
      .zip(1 to 1000).map { case (s, i) => f"$i%03d: $s" }.mkString("\n")
  }

}
