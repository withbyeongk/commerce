package io.hhplus.commerce.application.facade.usecase;

import io.hhplus.commerce.presentation.controller.member.dto.CartListDto;
import io.hhplus.commerce.presentation.controller.member.dto.CartPutInDto;
import io.hhplus.commerce.presentation.controller.member.dto.ChangeQuantityDto;

import java.util.List;

public interface CartUsecase {
    public void putIn(CartPutInDto dto);
    public void putOff(Long cartId);
    public void changeQuantity(ChangeQuantityDto dto);
    public List<CartListDto> getProductsInCart(Long memberId);
}
