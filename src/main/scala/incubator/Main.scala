package incubator

object Main extends Samples {

  def main(args: Array[String]): Unit = {
    { Known[Pig[ca]]; pig[ca]; }.asInstanceOf[Unit]
    Known[ Interpretation[ca] ]
    val interp = Known[ Interpretation[ Or[ca, cb]] ]

    println {
      interp(ia() :: ib() :: "hello" :: 12 :: Nil)
    }
  }

}
