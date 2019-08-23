package todd.step04

// 객체 참조를 끊고 느슨하게 만들기
case class Customer(id: Long, coffee: Option[Coffee], menu: Menu, baristaId: Long) {
  def order(barista: Barista, menuName: String): Customer = {
    menu.choose(menuName) match {
      case None => this.copy()
      case Some(item) =>
        this.copy(coffee = Some(barista.makeCoffee(item)))
    }
  }
}

case class Menu(items: List[MenuItem]) {
  def choose(name: String): Option[MenuItem] = items.find(item => name == item.name)
}

case class MenuItem(name: String)

class Barista(id: Long, coffeeFactory: CoffeeFactory) {
  def makeCoffee(menuItem: MenuItem): Coffee = coffeeFactory.create(menuItem)
}

case class Coffee(id: Long, name: String)

class CoffeeFactory(idGenerator: IdGenerator) {
  def create(menuItem: MenuItem) = Coffee(idGenerator.generate(), menuItem.name)
}

trait IdGenerator {
  def generate(): Long
}

trait CustomerRepository {
  def findById(id: Long): Customer
}

trait BaristaRepository {
  def findById(id: Long): Barista
}

class CoffeeOrderService(menu: Menu, customerRepository: CustomerRepository, baristaRepository: BaristaRepository) {
  def order(customerId: Long, menuName: String): Customer = {
    val customer = customerRepository.findById(customerId)
    val barista = baristaRepository.findById(customer.baristaId)

    customer.order(barista, menuName)

//    val menuItem = menu.choose(menuName)
//
//    val coffee = menuItem match {
//      case None => None
//      case Some(item) =>
//        Some(barista.makeCoffee(item))
//    }
//
//    customer.copy(coffee = coffee)
  }
}


