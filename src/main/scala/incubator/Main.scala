package incubator

/** Here Is The Thing 1 */
object hitt1 {

  //
  // Let's assume we are modelling commands as Data Types
  //
  trait Command

  //
  // And then let's assume we're modelling their runtime behaviour
  // as type-classes.
  //
  // Interpretations have one quirk: they apply a type transformation
  // to their input type.
  //
  // A real life reason for this is that we reuse a single object as an
  // argument to many Interpretations, by providing views to it.
  // Consider a (Haskel) list (Shapeless HList) example:
  //
  //    type in1[rest <: List] = String :: Int :: rest
  //    type in2[rest <: List] = Float :: Unit :: rest
  //    type in12[rest <: List] = in1[in2[rest]]
  //        // ⇒ String :: Int :: Float :: Unit :: rest
  //
  trait Interpretation[comm <: Command] {
    type In[a]
    type Out
    def apply[a](in: In[a]): Out
  }

  //
  // At some point, we will naturally need to combine our interpretations.
  // In keeping this example as generic as possible, we define this combination
  // in terms of combining the inputs and the outputs of two interpretations.
  //

  // Abstract combining inputs
  trait CombinedInput[in1[_], in2[_]] {
    type in[_]
    def to1[a](in: in[a]): in1[_]
    def to2[a](in: in[a]): in2[_]
  }

  // Abstract combining outputs
  trait CombineOutput[out1, out2] {
    type out
    def apply(out1: out1, out2: out2): out
  }

  // Define combination command transformation
  trait Combine[comm1 <: Command, comm2 <: Command] extends Command
  type ++[comm1 <: Command, comm2 <: Command] = Combine[comm1, comm2]

  // Define a refining type alias for Interpretation
  type InterpInOut[comm <: Command, in[_], out] = Interpretation[comm] {
    type In[a] = in[a]
    type Out = out
  }

  // Define an interpretation for combined commands
  //
  // In order to express the required inputs (such as input/output
  // combination evidence), we need (among others) the `In`
  // type-constructors from each interpretation to be explicitly
  // in the type parameters list.
  //
  // For the same reason we need the `Out` abstract types.
  //
  final case class CombineInterpretations[
    comm1 <: Command,
    comm2 <: Command,
    in1[_], out1,
    in2[_], out2,
  ](
    interp1: InterpInOut[comm1, in1, out1],
    interp2: InterpInOut[comm2, in2, out2],
    combineInputs: CombinedInput[in1, in2],
    combineOutputs: CombineOutput[out1, out2],
  ) extends Interpretation[ comm1 ++ comm2 ] {
    type In[a] = combineInputs.in[a]
    type Out = combineOutputs.out
    def apply[a](in: In[a]): Out = {
      val in1: in1[_] = combineInputs to1 in
      val in2: in2[_] = combineInputs to2 in
      val out1: out1 = interp1(in1)
      val out2: out2 = interp2(in2)
      val out: Out = combineOutputs(out1, out2)
      out
    }
  }

  //
  // A sample use case for all of the above, as "addition"
  //
  object sample {
    final abstract class Add extends Command

    // Input type transformers
    final case class HasInt[a](int: Int, other: a)

    case object AddI extends Interpretation[Add] {
      type In[a] = HasInt[a]
      type Out = Int
      def apply[a](in: In[a]): Int = in.int + 1
    }

    case object CombineHasInt
      extends CombinedInput[HasInt, HasInt]
    {
      type in[a] = HasInt[HasInt[a]]
      def to1[a](in: in[a]): HasInt[a] = HasInt(in.int, in.other.other)
      def to2[a](in: in[a]): HasInt[a] = in.other
    }

    case object CombineInt
      extends CombineOutput[AddI.Out, AddI.Out]
    {
      type out = Int
      def apply(a: Int, b: Int): Int = a + b
    }

    val combin: CombinedInput[AddI.In, AddI.In] =
      CombineHasInt
    val combout: CombineOutput[AddI.Out, AddI.Out] =
      CombineInt
    val interp1: InterpInOut[Add, AddI.In, AddI.Out] =
      AddI
    val interp2: InterpInOut[Add, AddI.In, AddI.Out] =
      AddI

    final case class combo[
      f1,
      f2,
      combin <: CombinedInput[AddI.In, AddI.In],
      combout,
    ](
      interp1: f1,
      interp2: f2,
      combin: combin,
      combout: combout,
    ) {
      type Wat = (f1, f2)
    }

    val combined = combo(
      interp1 = AddI,
      interp2 = AddI,
      combin = combin,
      combout = combout,
    )

    import pig._
    implicit val AddIPig: Pig[AddI.type] = "AddI"
    implicit val AddPig: Pig[Add] = "Add"
    implicit val IntPig: Pig[Int] = "Int"
    implicit val UnitPig: Pig[Unit] = "Unit"
    implicit def CombineOutputPig[a: Pig, b: Pig]: Pig[CombineOutput[a, b]] =
      s"CombineOutput[${pig[a]}, ${pig[b]}]"
    implicit def HasIntPig[a: Pig]: Pig[HasInt[a]] =
      s"HasInt[${pig[a]}]"
    implicit def CombinedInputPig[ain[_], bin[_]](
      implicit
      pigain: Pig[ain[Unit]],
      pigbin: Pig[bin[Unit]]
    ): Pig[CombinedInput[ain, bin]] =
      s"CombinedInput[${pigain}, ${pigbin}]"
  }

  object pig {
    def apply[t: Pig]: Pig[t] = implicitly
    final implicit class Pig[+t](val name: String) extends AnyVal {
      override def toString: String = name
    }
    object Pig {
      val xxx: Pig[Any] = "XXX"
      implicit def t2pig[a: Pig, b: Pig]: Pig[(a, b)] = s"(${pig[a]}, ${pig[b]})"
    }
  }

  def main(args: Array[String]): Unit = {
    import sample._
    println {
      pig[combined.Wat]
    }
  }

}

object Main {

  def main(args: Array[String]): Unit = {
    hitt1.main(args)
  }

}

