package io.hhplus.commerce.common.exception;

import org.springframework.http.HttpStatus;


public enum CommerceErrorCodes implements ErrorCode {
    /**
     *  현재 운영하는 시스템에서 사용하는 예외가 많지 않아 하나의 enum클래스에서 관리했습니다.
     *  만약 이 시스템이 좀 더 커질 것을 생각하여더 많은 예외가 필요하다면,
     *  여러 클래스로 분리하면 좋을 것 같습니다.
     *
     *  그리고 분리한다면, HttpStatus 종류별로 혹은 도메인별로 구분하는 방법이 있을 것 같은데
     *  도메인별로 구분하는 방법이 좀 더 좋을것 같습니다.
     *  가능하시면 제 판단에 대한 피드백 부탁드립니다.
      */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "카트 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    INVALID_EXCEED_QUANTITY(HttpStatus.BAD_REQUEST, "현재 장바구니에 담긴 상품 수보다 더 많거나 같은 양을 꺼낼 수 없습니다."),
    INVALID_ARGUMENTS_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 양수로 입력해야 합니다."),
    INVALID_ARGUMENTS_POINT(HttpStatus.BAD_REQUEST, "충전 포인트는 양수로 입력해야 합니다."),
    PRODUCT_STOCK_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품 재고정보를 찾을 수 없습니다."),
    OPTIMISTIC_LOCKING_FAILURE(HttpStatus.CONFLICT, "충전에 실패했습니다. 다시 시도해 주세요.")
    ;


    private final HttpStatus httpStatus;
    private final String message;

    CommerceErrorCodes(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}