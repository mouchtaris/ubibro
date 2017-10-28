package conjunction

trait AndDeductions {

  @inline final implicit def aFromABConjunction[a, b](implicit and: a && b): a = and.a

  @inline final implicit def bFromABConjunction[a, b](implicit and: a && b): b = and.b

}

