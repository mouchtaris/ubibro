package typeops

trait TFoldInferences extends Any {
  this: Any
    with TFoldTypeAliases
    with TFoldConstructor
  ⇒

  @inline final implicit def list2[f[_, _], a, b]: fullT[f, a :: b :: Nil, f[a, b]] =
    apply[f, a :: b :: Nil, f[a, b]]()

  @inline final implicit def list[f[_, _], h, t <: List](
    implicit
    tf: tfold[f, t]
  ): fullT[f, h :: t, f[h, tf.Out]] =
    apply[f, h :: t, f[h, tf.Out]]()

}
