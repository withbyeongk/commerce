# API Specification 

## 상품

### 상품 목록 조회

- HTTP Method : GET
- Path : localhost:8080/api/products/all
- Request : No Parameters
- Response : 200
    - 예시 :
```html
[
  {
    "productId": 1,
    "name": "Product 1",
    "price": 100,
    "stock": 50,
    "description": "Description 1",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  },
  {
    "productId": 2,
    "name": "Product 2",
    "price": 200,
    "stock": 100,
    "description": "Description 2",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  },
  {
    "productId": 3,
    "name": "Product 3",
    "price": 300,
    "stock": 150,
    "description": "Description 3",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  },...
]
```
- Error : X
- Authorization : X

### 인기 상품 목록 조회

- HTTP Method : GET
- Path : localhost:8080/api/products/bestsellers
- Request : No Parameters
- Response : 200
    - 예시 :
```html
[
  {
    "productId": 1,
    "name": "Product 1",
    "price": 100,
    "stock": 50,
    "description": "Description 1",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  },
  {
    "productId": 2,
    "name": "Product 2",
    "price": 200,
    "stock": 100,
    "description": "Description 2",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  },
  {
    "productId": 3,
    "name": "Product 3",
    "price": 300,
    "stock": 150,
    "description": "Description 3",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  },...
]
```
- Error : X
- Authorization : X

### 상품 상세 조회

- HTTP Method : GET
- Path : localhost:8080/api/products/{productId}
- Request : product_id
- Response : 200
    - 예시 :
```html
[
  {
    "productId": 1,
    "name": "Product 1",
    "price": 100,
    "stock": 50,
    "description": "Description 1",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:19:34.9733854",
    "createdAt": "2024-10-11T03:19:34.9733854"
  }
]
```
- Error : X
- Authorization : X

## 회원 포인트

### 회원 포인트 충전

- HTTP Method : POST
- Path : localhost:8080/api/member/points
- Request :
    - 예시 :
```html
{
    "memberId":"2",
    "points":"300"
}
```
- Response : 200
- Error : X
- Authorization : X

### 회원 포인트 조회

- HTTP Method : GET
- Path : localhost:8080/api/member/{memberId}/points
- Request :
- Response : 200
    - 예시 :
```html
{
    "memberId": 1,
    "point": 100
}
```
- Error : X
- Authorization : X

## 주문

### 상품 주문

- HTTP Method : POST
- Path : localhost:8080/api/order
- Request :
    - 예시 :
```html
{
  "memberId":2,
  "products": [1, 2, 3, 4, 5]
}
```
- Response : 200
    - 예시 :
```html
{
  "orderId": 1,
  "memberId": 2,
  "productId": 3,
  "totalPrice": 1000
}
```
- Error : X
- Authorization : X

## 결제

- HTTP Method : POST
- Path : localhost:8080/api/pay
- Request :
    - 예시 :
```html
{
  "memberId":2,
  "orderId": 4
}
```
- Response : 200
    - 예시 :
```html
{
  "payId": 1
}
```
- Error : X
- Authorization : X

## 장바구니

### 장바구니에 상품 담기

- HTTP Method : POST
- Path : localhost:8080/api/member/cart/products
- Request :
    - 예시 :
```
{
  "memberId":2,
  "productId": 4
}
```
- Response : 200
- Error : X
- Authorization : X

### 장바구니에서 상품 삭제

- HTTP Method : DELETE
- Path : localhost:8080/api/member/cart/products
- Request : No Parameters
- Response : 200
- Error : X
- Authorization : X

### 장바구니에 담긴 상품 목록 조회

- HTTP Method : GET
- Path : localhost:8080/api/member/{memberId}/cart/products
- Request : No Parameters
- Response : 200
    - 예시 :
```html
[
  {
    "productId": 1,
    "name": "Product 1",
    "price": 100,
    "stock": 50,
    "description": "Description 1",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:54:28.938402",
    "createdAt": "2024-10-11T03:54:28.938402"
  },
  {
    "productId": 2,
    "name": "Product 2",
    "price": 200,
    "stock": 49,
    "description": "Description 2",
    "deletedAt": null,
    "updatedAt": "2024-10-11T03:54:28.938402",
    "createdAt": "2024-10-11T03:54:28.938402"
  }
]
```
- Error : X
- Authorization : X

### 장바구니에 있는 상품의 수량 변경

- HTTP Method : PATCH
- Path : localhost:8080/api/member/products
- Request :
    - 예시 :
```html
{
  "memberId":2,
  "productId": 4,
  "quantity": 5
}
```
- Response : 200
- Error : X
- Authorization : X
