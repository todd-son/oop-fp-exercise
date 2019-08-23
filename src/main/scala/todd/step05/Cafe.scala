package todd.step05

// 유스케이스 다시 생각하기:
// 사실 고객은 어떤 바리스타이든 상관없다.
// 사실 메뉴도 가게에 따라 변경된다.
case class Customer(id: Long, coffee: Option[Coffee]) {
  def order(barista: Barista, menu: Menu, order: Order): Customer = {
    menu.choose(order) match {
      case None => this.copy()
      case Some(item) =>
        this.copy(coffee = Some(barista.makeCoffee(item)))
    }
  }
}

// 메뉴가 현재 주문 가능한지 로직을 복잡하다면 과감하게 넣자.
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

// 바리스타마다 자기만의 제조법을 가진다.
// 데이터를 먼저 생각했다면
// class Barista(id: Long, makingType: MakingType)
// 데이터 저장을 미리 생각하지 말자
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

case class Order(customerId: Long, menuName: String)

class CoffeeOrderService(menuFactory: MenuFactory, customerRepository: CustomerRepository, baristaRepository: BaristaRepository) {
  def order(order: Order): Customer = {
    val customer = customerRepository.findById(order.customerId)
    val menu = menuFactory.create()
    val barista = baristaRepository.findAvailable()

    customer.order(barista, menu, order)
  }
}

// 이제 구조를 잡았으니 TDD를 합시다. 그리고 TDD를 하면서 구조를 계속 개선 합시다.
// 오히려 코드가 유스케이스를 더 명확하게도 할 수 있다.



