# jpa-project1-summary

### 도메인 모델

<img width="600" alt="스크린샷 2022-10-06 오후 7 31 19" src="https://user-images.githubusercontent.com/97823928/194291270-c1c7dfde-9dc2-4110-992f-d91f3fa61b91.png">

### 변경 감지와 병합(merge)

```준영속 엔티티```
* 영속성 컨텍스트가 더는 관리하지 않는 엔티티
* 이미 데이터베이스에 한번 저장되어서 식별자가 존재

#### ItemRepository의 저장 메서드

```java
@Repository
public class ItemRepository {
    @PersistenceContext
    EntityManager em;
    
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
       }
    }
    //...
}
```
* save()에 식별자 값이 없으면 새로운 엔티티로 판단하여 영속화(=저장), 없으면 병합(merge)
* 준영속 상태인 상품 엔티티를 수정할 때는 Id값이 있기 떄문에 병합을 수행

#### 1. 변경 감지 기능

```java
@Transactional
void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
    Item findItem = em.find(Item.class, itemParam.getId());
    findItem.setPrice(itemParam.getPrice());
}
```
* 영속성 컨텍스트에서 엔티티 다시 조회 후 데이터를 수정하는 방식
* 트랜잭션 안에서 엔티티를 조회, 변경할 값 선택 후 트랜잭션 커밋 시점에 변경감지가 동작 → UPDATE SQL문 실행

#### 2. 병합 사용

```java
@Transactional
void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티 
    Item mergeItem = em.merge(item);
}
```

<img width="550" alt="스크린샷 2022-10-06 오후 9 25 10" src="https://user-images.githubusercontent.com/97823928/194311920-2e23d240-ca12-4bb2-8659-9b109313bf26.png">

1. merge()를 실행
2. 준영속 엔티티의 식별자 값으로 1차 캐시에서 영속 엔티티를 조회
* 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장  
3. 조회한 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체
* member 엔티티의 모든 값을 mergeMember에 밀어 넣음
* 이때 mergeMember의 “회원1”이라는 이름이 “회원명변경”으로 바뀜
4. 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터 베이스에 UPDATE SQL문이 실행
* 병합시 값이 없으면 null로 업데이트할 위험이 존재 (병합은 모든 필드를 교체함)
