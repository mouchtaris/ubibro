package incubator

import
  fun.typelevel._,
  fun.list._,
  fun.interpretation._

object Main {
  import sample._

  object I {

    type fullT[t, in[_], out] =
      Interpretation[t] {
        type In[a] = in[a]
        type Out = out
      }

    type In1[in[_]] = {
      type t[T] = T {
        type In[a] = in[a]
      }
    }

  }

  implicit def orInterpretationA[
    ca: Or.resultOf[ca, cb]#t,
    cb,
  ](
    interpa: Interpretation[ca],
  ): I.fullT[Or[ca, cb], interpa.In, interpa.Out] =
    new Interpretation[ca `Or` cb] {
      type In[a] = interpa.In[a]
      type Out = interpa.Out
      def apply[a: In](a: a): Out =
        interpa(a)
    }

  implicit def orInterpretationB[
    ca,
    cb: Or.resultOf[ca, cb]#t,
  ](
    interpb: Interpretation[cb],
  ): I.fullT[Or[ca, cb], interpb.In, interpb.Out] =
    new Interpretation[ca `Or` cb] {
      type In[a] = interpb.In[a]
      type Out = interpb.Out
      def apply[a: In](a: a): Out = interpb(a)
    }

  def main(args: Array[String]): Unit = println {
    val l: Int :: List = 12 :: "Hi" :: Nil
    def isint[a: IsType.is[Int]#t](a: a): Int =
      Known[IsType[a, Int]].evidence(a)
    isint(l.head)
  }

}

