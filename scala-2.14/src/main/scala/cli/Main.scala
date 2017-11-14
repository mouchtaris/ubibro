package cli

object Main {

  val runnies = scala.Vector(
    incubate3.Incubator,
    incubate4.Incubator
  )
  val pickRunny = 1
  def runny = runnies(pickRunny)

  def main(args: scala.Array[scala.Predef.String]): scala.Unit = {
    runny.run()
  }

}
