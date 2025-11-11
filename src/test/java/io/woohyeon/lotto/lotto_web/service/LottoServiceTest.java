package io.woohyeon.lotto.lotto_web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import io.woohyeon.lotto.lotto_web.support.LottoRules;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class LottoServiceTest {

    LottoService lottoService = new LottoService();

    @Test
    void purchaseLottos() {
        //given
        int[] correctPurchaseAmounts = {
                1000,
                2000,
                10000,
                11000
        };

        int[] wrongPurchaseAmounts = {
                1001,   //1000의 배수 x
                0,      //0
                -1000   //음수
        };

        //when
        List<PurchaseResponse> purchaseResponses = Arrays.stream(correctPurchaseAmounts)
                .mapToObj(this.lottoService::purchaseLottos)
                .toList();

        //then
        for (int i = 0; i < correctPurchaseAmounts.length; i++) {
            //투입한 금액을 로또 가격으로 나눈 개수의 로또를 발행한다.
            assertThat(purchaseResponses.get(i).issuedCount()).isEqualTo(correctPurchaseAmounts[i] / LottoRules.LOTTO_PRICE);

            //PurchaseResponse의 issuedLottos 필드는 issuedLottos의 개수와 같다.
            assertThat(purchaseResponses.get(i).issuedLottos().size()).isEqualTo(purchaseResponses.get(i).issuedCount());
        }

        Arrays.stream(wrongPurchaseAmounts)
                .forEach(wrongPurchaseAmount ->
                        assertThatThrownBy(
                                () -> lottoService.purchaseLottos(wrongPurchaseAmount)
                        ).isInstanceOf(IllegalArgumentException.class)
                );
    }
}