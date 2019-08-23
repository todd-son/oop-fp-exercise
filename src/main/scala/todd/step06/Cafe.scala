package todd.step06

// 스칼라는 ad-hoc 디펜던시를 사용하면 좀더 문맥이 좋아질 수 있다.
// 상태도 좀 고려해보자.
// 고객의 최종 상태에 order도 포함되지 않을까?
// 상태란 일반적으로 최종 상태를 의미하므로 고객의 최종 주문을 포함할 수 있다.
// 고객이란 어떤 주문을 했고, 최종적으로 커피를 가지고 있나 없나
case class Customer(id: Long, order: Order, coffee: Option[Coffee]) {
  def order()(implicit menu: Menu, barista: Barista): Customer  = {
    menu.choose(order) match {
      case None => this.copy()
      case Some(item) =>
        this.copy(coffee = Some(barista.makeCoffee(item)))
    }
  }
}

case class Menu(items: List[MenuItem], validator: MenuValidator) {
  def choose(order: Order): Option[MenuItem] = {
    val item = items.find(item => order.menuName == item.name)

    item.flatMap(i => {
      if (validator.validate(i))
        item
      else
        None
    })
  }
}

case class MenuItem(name: String)

trait MenuValidator {
  def validate(menuItem: MenuItem): Boolean
}

trait MenuFactory {
  def create(): Menu
}

class Barista(id: Long, coffeeFactory: CoffeeFactory) {
  def makeCoffee(menuItem: MenuItem): Coffee = coffeeFactory.create(menuItem)
}

case class Coffee(id: Long, name: String)

trait CoffeeFactory {
  def create(menuItem: MenuItem): Coffee
}

class HandDripCoffeeFactory(idGenerator: IdGenerator) extends CoffeeFactory {
  def create(menuItem: MenuItem): Coffee = Coffee(idGenerator.generate(), menuItem.name)
}

class CoffeeMachineCoffeeFactory(idGenerator: IdGenerator) extends CoffeeFactory {
  def create(menuItem: MenuItem): Coffee = Coffee(idGenerator.generate(), menuItem.name)
}

trait IdGenerator {
  def generate(): Long
}

trait CustomerRepository {
  def findById(id: Long): Customer
}

trait BaristaRepository {
  def findAvailable(): Barista
}

// primitive 한 데이터가 넘어가는 것은 지저분하니 얘도 도메인 객체로
case class Order(customerId: Long, menuName: String)

class CoffeeOrderService(menuFactory: MenuFactory, customerRepository: CustomerRepository, baristaRepository: BaristaRepository) {
  def order(order: Order): Customer = {
    val customer = customerRepository
      .findById(order.customerId)
      .copy(order = order)

    implicit val menu = menuFactory.create()
    implicit val barista = baristaRepository.findAvailable()

    customer.order()
  }
}



