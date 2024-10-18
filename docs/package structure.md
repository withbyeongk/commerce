# 패키지 구조
```
└─commerce
    │  CommerceApplication.java
    │
    ├─application
    │  └─service
    │          CartService.java
    │          MemberService.java
    │          OrderService.java
    │          PayService.java
    │          ProductService.java
    │
    ├─domain
    │  ├─entity
    │  │      Cart.java
    │  │      Member.java
    │  │      Order.java
    │  │      OrderItem.java
    │  │      Pay.java
    │  │      Product.java
    │  │
    │  └─policy
    │          Const.java
    │
    ├─infra
    │  ├─entity
    │  │      CartEntity.java
    │  │      MemberEntity.java
    │  │      OrderEntity.java
    │  │      OrderItemEntity.java
    │  │      PayEntity.java
    │  │      ProductEntity.java
    │  │
    │  └─repository
    │          CartRepository.java
    │          MemberRepository.java
    │          OrderItemRepository.java
    │          OrderRepository.java
    │          PayRepository.java
    │          ProductRepository.java
    │
    └─presentation
        ├─controller
        │      CartController.java
        │      MemberController.java
        │      OrderController.java
        │      PayController.java
        │      ProductController.java
        │
        └─dto
                CartPutInDto.java
                ChangeQuantityDto.java
                ChargePointDto.java
                OrderRequestDto.java
                OrderResponseDto.java
                PayRequestDto.java
                PayResponseDto.java
                PointResponseDto.java
                ProductResponseDto.java
```
클린 아키텍처를 적용하여 위와 같은 구조로 패키지 구조를 설계했습니다.