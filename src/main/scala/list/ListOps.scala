package list

trait ListOps[l <: List] extends Any {

  def self: l

  @inline final def ::[a](a: a): a :: l = list.::(a, self)

}

