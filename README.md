# Safely-handle-null-in-Java
자바에서 null을 안전히 다루는 방법
</br></br>
# 자바 null 참조의 문제점

- 의미가 모호함
- 다음 메서드 네이밍을 통해 사용자는 id에 해당하는 Member가 나올 것이라 기대하겠지만 해당 id에 해당하는 Member가 없는 경우 null이 응답될 수 있습니다.
- 이러한 상황에서 메서드의 네이밍을 명확하게 하려면 getByIdMemberOrNullIfNotFound()와 같은 네이밍으로 작명해야 합니다.
- 이 문제점은 Null이 가지고있는 의미가 모호함 에서 발생하게 됩니다.

```java
public Member getById(UUID id) {
if (repository.exists(id)) {
return null;
}

return repository.find(id);
}
```

- 초기화 되지 않음, 정의되지 않음, 값이 없음, null을 값으로 가짐?
- 모든 참조의 기본 상태(값?)
- 모든 참조는 null이 가능하기에 개발자는 항상 null인지 값인지 체크해야 하는 상황에 놓인다.
    - 전달 받은 객체에 null이 들어있을지 실제 참조 값이 들어있을지 알 수 없기에 분기 처리를 통해 null을 체크하고 처리해야 합니다.

```java
public void printName(Member member) {
Name name = member.getName();

if(name == null) {
System.out.println("can't find name");
} else {
System.out.println(name);
}
}
```
</br></br>

# 자바에서 null을 안전하게 다루는 방법

## 자바 제공 기본 장치

- 단정문
- java.util.Objects
- java.util.Optional

</br>

## 단정문 assertion

- assert 식 1;
- assert 식 1 : 식 2;

```java
private void serRefreshInterval(int interval) {
assert interval > 0 && interval < 1000 : interval;
}
```

- 진리 값 판별 식인 식 1이 거짓이라면 AssertionError 발생
- 식 2는 AssertionError에 포함될 상세 정보를 만드는 생성식
- 공개 메서드에서는 사용하지 말아야 함
    - 객체 스스로가 생상자이면서 소비자인 경우에만 사용해야 함.
    - 객체의 api를 누가 사용하는지 모르는 경우 사용하면 안 됨
    - Runtime시에 비활성화됨
    
</br>
   
## Object

### Java 8

- isNull(Object obj)
- nonNull(Object obj)
- requireNonNull(T obj)

### Java 9

- requeireNonNullElse(T obj, T defaultObj)

</br>

## Optional 

Optional 활용 팁

- 절대로 Optional 변수와 반환 값에 null을 사용하지 말아라
- Optional에 값이 들어있다는 걸 확신하지 않는 한 Optional.get()을 쓰지 말아라
- Optional.isPresent() Optional.get()을 가능한 쓰지 말자
- Optional로 값을 처리하는 중에 그 안에 중간값을 처리하기 위한 Optional 이 사용되면 매우 복잡해진다.
- Optional을 필드, 메서드의 매개변수, 집합 자료형에 쓰지 말자
- 집합 자료형(List, Map, Set)을 감싸려면 Optional이 아닌 빈 집합을 사용한다

</br>
</br>

# null 잘 쓰는 법

## API (매개변수, 반환 값)에 null을 최대한 쓰지 말아라

- null로 지나치게 유연한 메서드를 만들지 말고 명시적인 메서드를 만들어라
- null을 반환하지 말아라
    - 반환 값이 있어야 한다면 차라리 예외를 던지자
    - 빈 반환 값은 빈 컬렉션이나 null 객체를 활용한다
    - 반환이 값이 없을 수도 있다면 Oprional을 반환한다
- 선택적 매개변수는 null 대신 다형성(메서드 추가 정의; overload)을 사용해 표현하라

</br>

## 사전 조건과 사후 조건을 확인하라(계약에 의한 설계)

- 개방 폐쇄 원칙 OCP의 상위 개념
- api 소비자와 제공자 사이의 지켜져야 할 규약을 지켜야 할 계약으로 여기는 설계 방법
- 형식적 규약 외에 사전 조건과 사후 조건과 유지 조건을 포함

### + 자바의 계약에 의한 설계 (Design by Contract)

- interfate + java doc
- 사전 조건 = 보호 절
    - 단정문
    - Objects의 메서드
    - illegalArgumentException
    - NullPointerException
- Spring의 Assert 클래스
- AssertJ Preconditions 클래스
- Bean Validation

</br>

## null의 범위를 지역(클래스, 메서드)에 제한하라

객체지향 프로그래밍은 메세징이며, 지역적인 보호, 보강, 상태가 변화하는 과정을 지엽적으로 숨기는 것이다.

- 기본 문제 해결 원칙: 큰 문제는 제어가 가능한 작은 문제로 나누어 정복하고 다시 통합한다.
- 상태와 비슷하게 null 또한 지역적으로 제한할 경우 큰 문제로 번지지 않는다.
- 클래스와 메서드를 작게 만들어라

</br>

## 초기화를 명확히 하라

- 초기화 시점과 실행 시점이 겹치지 않아야 한다.
- 실행 시점엔 초기화되지 않은 필드가 없어야 한다.
- 실행 시점에 null인 필드는 초기화되지 않았다는 의미가 아닌, 값이 없다는 의미여야 한다.
- 객체 필드의 생명주기는 모두 객체의 생명주기와 같아야 한다.
- 지연 초기화 필드의 경우 팩토리 메서드로 null 처리를 캡슐화하라
