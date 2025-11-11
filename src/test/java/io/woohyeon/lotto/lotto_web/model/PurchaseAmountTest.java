package io.woohyeon.lotto.lotto_web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PurchaseAmountTest {

    @Test
    void validate_1000의_배수가_입력되면_예외가_발생하지_않는다() {
        //given
        int[] correctPurchaseAmounts = {
                1000,
                2000,
                10000,
                11000
        };

        //when && then
        Arrays.stream(correctPurchaseAmounts)
                .forEach(correctPurchaseAmount ->
                        assertThat(
                                new PurchaseAmount(correctPurchaseAmount)
                        ).isInstanceOf(PurchaseAmount.class)

                );
    }

    @Test
    void validate_1000의_배수가_아니거나_자연수가_아닌_값이_입력되면_예외가_발생한다() {
        //given
        int[] wrongPurchaseAmounts = {
                1001,   //1000의 배수 x
                0,      //0
                -1000   //음수
        };

        //when && then
        Arrays.stream(wrongPurchaseAmounts)
                .forEach(wrongPurchaseAmount ->
                        assertThatThrownBy(
                                () -> new PurchaseAmount(wrongPurchaseAmount)
                        ).isInstanceOf(IllegalArgumentException.class)
                );
    }

}