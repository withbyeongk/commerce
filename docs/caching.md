# Caching

## 캐싱을 사용하여 부하를 최소화할 수 있는 비즈니스 로직

### 캐싱 사용에 적합한 조건
1. 자주 사용되는 동일한 데이터
2. 빠른 검색 필요
3. 불필요한 반복
4. 오래 걸리는 쿼리
5. DB의 부하

### 현 시스템 내에서 적용하면 좋을 API

: **인기 상품 조회 API**<br>

- 이유 :

> 인기 상품 API는 일반적으로 많은 사람들의 요청이 예상된다.<br>
> 아래의 이유들로 인기 상품 조회 API는 캐싱에 적절한 경우라고 생각된다.

1. 성능 향상 : <br>
> 실시간으로 판매되는 인기 상품들을 원활하게 볼 수 있어야 한다.<br>
> 캐싱을 하여 DB를 거치지 않고 바로 결과를 반환하게 된다면 훨씬 빠른 응답시간을 보장하여 사용성을 높여줄 수 있다.

2. 부하 감소 :
> 인기 상품 API는 집계쿼리를 수행하게 되어 매 실행마다 DB에 부담을 줍니다.<br>
> 쿼리가 오래걸릴 수 있으며, 캐싱을 하면 DB의 부담을 줄일 수 있다.<br>
> 이로 인해 성능과 안정성이 향상될 수 있다.

3. 데이터 일관성 :
> 인기 상품 조회는 3일간 가장 많이 팔린 다섯가지의 상품을 조회한다.<br>
> 일별로 변하는 데이터이므로, 하루동안은 변경되지 않는다.<br>
> 하루동안 캐싱하여 사용하면 데이터의 일관성을 유지하면서 성능을 향상시킬 수 있다.<br>

4. 사용자 경험 개선 :
> 결국 요청에 대한 처리시간을 줄일 수 있다.<br>
> DB의 부하를 줄여 다른 요청을 원활하게 처리할 수도 있다.
