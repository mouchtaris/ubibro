package object typeops {

  type List = list.List
  type Nil = list.Nil
  type ::[a, b <: list.List] = list.::[a, b]

}
