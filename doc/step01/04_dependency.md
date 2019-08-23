# 연관관계

연관관계란 한 객체에서 객체를 찾아갈 수 있는 것

## 연관관계의 종류 
1. Association - 객체참조

```scala
case class Customer(menu: Menu)
```

2. Dependency - 협력의 시점에 의존을 맺고 헤어짐

```scala
case class Customer() {
  def choose(menu: Menu, menuName: String): MenuItem = menu.choose(menuName)
}
```

3. Inheritance - 상속
4. Realization - 인터페이스 구현

## 객체 참조 끊기

1. 이 경우 Repository가 등장 이때 참조ID를 사용하여 연관관계를 느슨하게 한다.





 

