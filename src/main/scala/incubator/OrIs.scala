package incubator

trait OrIs {

  implicit def ori[
    a,
    b
  ](
    implicit
    interpa: Interpretation[a],
    interpb: Interpretation[b],
  ): OrI.aux[
    a,
    b,
    interpa.In, interpa.Out,
    interpb.In, interpb.Out
  ] =
    OrI[
      a,
      b,
      interpa.In,
      interpa.Out,
      interpb.In,
      interpb.Out
    ](interpa, interpb)

}
