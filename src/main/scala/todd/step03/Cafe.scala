package todd.step03

// 객체를 개선해보자
// 식별자 부여

case class Customer(id: Long, coffee: Option[Coffee], menu: Menu, barista: Barista) {
  def order(menuName: String): Customer = {
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

class Barista(coffeeFactory: CoffeeFactory) {
  def makeCoffee(menuItem: MenuItem): Coffee = coffeeFactory.create(menuItem)
}

case class Coffee(id: Long, name: String)

class CoffeeFactory(idGenerator: IdGenerator) {
  def create(menuItem: MenuItem) = Coffee(idGenerator.generate(), menuItem.name)
}

trait IdGenerator {
  def generate(): Long
}


