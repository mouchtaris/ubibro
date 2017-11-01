import scala.language.strictEquality
import typeops._
import pig._
import core._
import listarguments.{ ListArgumentHandling }

object evaluators {

  final implicit class SimpleEvaluator[op, out](
    val eval: Read[op, out]
  ) extends AnyVal with Evaluator {
    type Out = out
    type Operation = op
  }
  object SimpleEvaluator {
    implicit def anyToSE[Op, T](t: => T): SimpleEvaluator[Op, T] =
      new SimpleEvaluator[Op, T](t)
  }

}
