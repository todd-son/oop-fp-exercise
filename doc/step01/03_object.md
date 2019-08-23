# 객체

1. 객체란 상태, 행동, 식별자를 가진 실체

## 상태

1. 상태란 행동의 과정과 결과를 단순히 표현한 것. 과거에 발생한 행동의 이력을 표현하기는 어려움. 상태 자체도 객체로 표현될 수 있음
2. 객체의 프로퍼티란 객체의 상태를 구성하는 모든 특징(ex. 키 몸무게 이름)
3. 프로퍼티 값(ex. 180 70 토드)
4. 객체와 객체 사이의 의미 있는 연결을 링크라고 한다. 객체 사이의 링크가 존재해야만 요청을 보내고 받을 수 있다. 
5. 객체 스스로 본인의 상태를 통제해야 한다.

## 행동 

1. 객체의 행동에 의해 객체의 상태가 변경됨. 행동은 부수효과를 초래한다.
2. 객체의 행동은 상태에 영향을 받으며 상태를 변경시킨다.

## 협력과 행동

1. 객체가 다른 객체와 협력하는 유일한 방법은 다른 객체에게 요청을 보내는 것이다.

- Customer에 커피를 상태로 적용하고 커피를 마신다는 행동을 부여해보자.

```scala
case class Customer(cashier: Cashier, coffee: Option[Coffee] = None) {
  def drinkCoffee(quantity: Int): Try[Customer] = {
    coffee match {
      case None => Failure(new IllegalStateException("Don't have a cup of coffee."))
      case Some(c) =>
        if (c.quantity < quantity)
          Failure(new IllegalStateException("Not enough coffee."))
        else
          Success(
            this.copy(
              coffee = Some(
                c.copy(quantity = c.quantity - quantity))
            )
          )
    }
  }
}
```

## 상태 캡슐화 

1. 현실 세계에서의 커피는 수동적인 존재이지만 객체의 세상에서는 능동적이 되어야 한다.

2. 객체의 행동 유발은 외부 객체에 의한 것이지만 객체의 상태를 변경하는 것은 항상 객체 스스로 결정해야 된다.  

- 상태 캡슐화도 적용해 보자.

```scala
case class Customer(cashier: Cashier, coffee: Option[Coffee] = None) {
  def drinkCoffee(quantity: Int): Try[Customer] = {
    coffee match {
      case None => Failure(new IllegalStateException("Don't have a cup of coffee."))
      case Some(c) =>
        c.drunken(quantity) match {
          case Success(c2) => Success(this.copy(coffee = Some(c2)))
          case Failure(e) => Failure(e)
        }
    }
  }
}

case class Coffee(name: String, quantity: Int) {
  def drunken(quantity: Int): Try[Coffee] =
    if (this.quantity < quantity)
      Failure(new IllegalStateException("Not enough coffee."))
    else
      Success(this.copy(quantity = this.quantity - quantity))
}
```

## 식별자

1. 객체를 서로 구별할 수 있는 특정한 프로퍼티를 식별자라고 한다.
2. 값(value)은 금액, 시간의 경우 불변 상태를 가지므로 객체의 상태가 같다면 두 인스턴스를 같은 것으로 판단한다. 동등성으로 판단한다.(equality)
3. 참조 객체 또는 엔티티는 식별자를 가지며 식별자가 같으면 같은 객체로 판단한다. 이러한 성질을 동일성이라고 한다.

# 행동이 상태를 결정한다.

1. 객체 지향의 입문자들이 가장 쉽게 빠지는 함정은 상태를 중심으로 객체를 바라보는 것이다.
2. 상태를 먼저 결정할 경우 캡슐화가 저해된다. 객체의 재사용성이 저하된다.

# 은유와 객체

1. 현실 속에서는 수동적인 객체가 소프트웨어 객체로 구현될때는 능동적으로 변한다. 이를 의인화라고 한다