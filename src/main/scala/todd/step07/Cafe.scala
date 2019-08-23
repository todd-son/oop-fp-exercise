package todd.step07

// msa
// 결국 중요한 것은 도메인이고 객체이다!!!!!!!!!
case class Customer(id: Long, order: Order, coffee: Option[Coffee]) {
  def order()(implicit menuService: MenuService, baristaService: BristaService): Customer  = {
    menuService.choose(order) match {
      case None => this.copy()
      case Some(item) =>
        this.copy(coffee = Some(baristaService.makeCoffee(item)))
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

trait MenuService {
  def choose(order: Order): Option[MenuItem]
}

case class MenuItem(name: String)

trait MenuValidator {
  def validate(menuItem: MenuItem): Boolean
}

trait MenuFactory {
  def create(): Menu
}

trait BristaService {
  def makeCoffee(menuItem: MenuItem): Coffee
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

class CoffeeOrderService(customerRepository: CustomerRepository) {
  def order(order: Order)(implicit menuService: MenuService, baristaService: BristaService) : Customer = {
    val customer = customerRepository
      .findById(order.customerId)
      .copy(order = order)

    customer.order()
  }
}



