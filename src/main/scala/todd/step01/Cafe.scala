package todd.step01

// 협력을 인터페이스로 나타내보자
trait Customer {
  def order(menuName: String)
}

trait Menu {
  def choose(name: String): MenuItem
}

trait MenuItem {

}

trait Barista {
  def makeCoffee(menuItem: MenuItem): Coffee
}

trait Coffee {
  def construct(menuItem: MenuItem)
}
