package io.woohyeon.lotto.lotto_web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.woohyeon.lotto.lotto_web.dto.request.LottoResultRequest;
import io.woohyeon.lotto.lotto_web.dto.response.IssuedLotto;
import io.woohyeon.lotto.lotto_web.dto.response.LottoResultResponse;
import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import io.woohyeon.lotto.lotto_web.support.LottoRules;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class LottoServiceTest {

    LottoService lottoService = new LottoService();

    @Test
    void purchaseLottosWith() {
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
                .mapToObj(this.lottoService::purchaseLottosWith)
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
                                () -> lottoService.purchaseLottosWith(wrongPurchaseAmount)
                        ).isInstanceOf(IllegalArgumentException.class)
                );
    }

    @Test
    void calculateStatisticsOf() {
        // given
        List<Integer> lottoNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        List<IssuedLotto> issuedLottos = List.of(
                new IssuedLotto(List.of(1, 2, 3, 4, 5, 6), null), // 1등
                new IssuedLotto(List.of(1, 2, 3, 4, 5, 7), null), // 2등
                new IssuedLotto(List.of(1, 2, 3, 4, 5, 8), null), // 3등
                new IssuedLotto(List.of(1, 2, 3, 4, 8, 9), null)  // 4등
        );

        LottoResultRequest request = new LottoResultRequest(
                issuedLottos,
                lottoNumbers,
                bonusNumber
        );

        // when
        LottoResultResponse result = lottoService.calculateStatisticsOf(request);

        // then
        // 총 로또 개수는 요청한 issuedLottos의 개수와 같아야 한다.
        assertThat(result.rankCounts().stream()
                .mapToLong(entry -> entry.getValue())
                .sum()).isEqualTo(issuedLottos.size());

        // 수익률은 0 이상이어야 한다.
        assertThat(result.returnRate()).isGreaterThanOrEqualTo(0);

        // 로또 개수 × LOTTO_PRICE 만큼의 금액을 기준으로 계산된다.
        int purchaseAmount = issuedLottos.size() * LottoRules.LOTTO_PRICE;
        assertThat(purchaseAmount).isEqualTo(4000);

        // 1등 로또(1~6 맞춤)가 1장 존재해야 함
        boolean hasFirstPrize = result.rankCounts().stream()
                .anyMatch(entry -> entry.getKey().name().equals("FIRST") && entry.getValue() == 1);
        assertThat(hasFirstPrize).isTrue();
    }
}