package todd.step02

// 가볍게 구현하자.
case class Customer(var coffee: Option[Coffee], menu: Menu, barista: Barista) {
  def order(menuName: String){
    menu.choose(menuName) match {
      case None => Unit
      case Some(item) =>
        coffee = Some(barista.makeCoffee(item))
    }
  }
}

case class Menu(items: List[MenuItem]) {
  def choose(name: String): Option[MenuItem] = items.find(item => name == item.name)
}

case class MenuItem(name: String)

class Barista {
  def makeCoffee(menuItem: MenuItem): Coffee = Coffee(menuItem)
}

case class Coffee(name: String)

object Coffee {
  def apply(menuItem: MenuItem): Coffee = Coffee(menuItem.name)
}
