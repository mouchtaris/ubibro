package tag

import
  cross.lang._,
  common.StdImports._

trait Tagverse {
  type BaseType

  sealed abstract trait tag extends Any
  final type tagged = BaseType & tag

  def apply(t: BaseType): tagged = t.asInstanceOf[tagged]
  def unapply(t: tagged): BaseType = t
}

