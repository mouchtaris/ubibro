package tag

import
  cross.lang._,
  common.StdImports._

trait Tagverse {
  type T

  sealed abstract trait tag extends Any
  final type t = T & tag

  def apply(t: T): t = t.asInstanceOf[t]
  def unapply(t: t): T = t
}

