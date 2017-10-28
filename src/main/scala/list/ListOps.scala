package list

trait ListOps[l <: List] extends Any {

  def self: l

  def ::[a](a: a): a :: l = list.::(a, self)

}

