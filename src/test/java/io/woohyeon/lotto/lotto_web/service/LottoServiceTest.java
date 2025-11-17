package io.woohyeon.lotto.lotto_web.service;

import static io.woohyeon.lotto.lotto_web.support.LottoRules.LOTTO_PRICE;
import static io.woohyeon.lotto.lotto_web.support.LottoRules.ROUNDING_SCALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import io.woohyeon.lotto.lotto_web.model.Lotto;
import io.woohyeon.lotto.lotto_web.model.Rank;
import io.woohyeon.lotto.lotto_web.model.RankCount;
import io.woohyeon.lotto.lotto_web.model.WinningNumbers;
import io.woohyeon.lotto.lotto_web.service.dto.request.LottoPurchaseRequest;
import io.woohyeon.lotto.lotto_web.service.dto.request.LottoResultRequest;
import io.woohyeon.lotto.lotto_web.service.dto.response.ExpectedStatistics;
import io.woohyeon.lotto.lotto_web.service.dto.response.LottoPurchaseResponse;
import io.woohyeon.lotto.lotto_web.repository.ResultStore;
import io.woohyeon.lotto.lotto_web.service.dto.response.LottoResultResponse;
import io.woohyeon.lotto.lotto_web.service.dto.response.PurchaseDetailResponse;
import io.woohyeon.lotto.lotto_web.service.dto.response.PurchaseSummaryResponse;
import io.woohyeon.lotto.lotto_web.service.dto.response.PurchasesResponse;
import io.woohyeon.lotto.lotto_web.repository.PurchaseStore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LottoServiceTest {

    PurchaseStore purchaseStore;
    ResultStore resultStore;
    LottoService lottoService;

    @BeforeEach
    void beforeEach() {
        purchaseStore = new PurchaseStore();
        resultStore = new ResultStore();
        lottoService = new LottoService(purchaseStore, resultStore);
    }

    @Test
    void purchaseLottosWith_주어진_금액으로_로또를_구매한다() {
        //given
        List<LottoPurchaseRequest> corrects = List.of(
                new LottoPurchaseRequest(1000),
                new LottoPurchaseRequest(2000),
                new LottoPurchaseRequest(10000),
                new LottoPurchaseRequest(11000)
        );

        List<LottoPurchaseRequest> wrongs = List.of(
                new LottoPurchaseRequest(1001),     //1000의 배수 x
                new LottoPurchaseRequest(0),        //0
                new LottoPurchaseRequest(-1000)    //음수
        );

        //when
        List<LottoPurchaseResponse> purchaseResponses = corrects.stream()
                .map(lottoService::purchaseLottosWith)
                .toList();

        //then

        //투입한 금액을 로또 가격으로 나눈 개수의 로또를 발행한다.
        for (int i = 0; i < purchaseResponses.size(); i++) {
            assertThat(purchaseResponses.get(i).lottoCount())
                    .isEqualTo(corrects.get(i).purchaseAmount() / LOTTO_PRICE);
        }

        //1000의 배수인 자연수가 입력되지 않으면 예외가 발생한다.
        wrongs.forEach(wrong ->
                assertThatThrownBy(
                                () -> lottoService.purchaseLottosWith(wrong)
                        ).isInstanceOf(IllegalArgumentException.class)
        );
    }
    @Test
    void getPurchaseSummaries_구매_목록을_반환한다() {
        //given
        List<LottoPurchaseRequest> purchaseRequests = List.of(
                new LottoPurchaseRequest(1000),
                new LottoPurchaseRequest(2000),
                new LottoPurchaseRequest(10000),
                new LottoPurchaseRequest(11000)
        );
        purchaseRequests.forEach(request -> lottoService.purchaseLottosWith(request));

        //when
        PurchasesResponse result = lottoService.getPurchaseSummaries();

        //then
        assertThat(result.count()).isEqualTo(purchaseRequests.size());

        //구매 내역은 최신순으로 정렬되야 한다.
        List<Integer> expectedAmountsDesc = purchaseRequests.stream()
                .map(LottoPurchaseRequest::purchaseAmount)
                .sorted(Comparator.reverseOrder())
                .toList();

        // 응답 검사
        for (int i = 0; i < result.count(); i++) {
            PurchaseSummaryResponse summary = result.purchases().get(i);

            // ticketCount 검증
            assertThat(summary.lottoCount())
                    .isEqualTo(expectedAmountsDesc.get(i) / LOTTO_PRICE);

            // 아직 결과 생성 전이므로
            assertThat(summary.hasResult()).isFalse();
            assertThat(summary.returnRate()).isNull();
        }
    }

    @Test
    void getPurchaseDetail_구매_상세_조회를_한다() {
        //given
        LottoPurchaseRequest purchaseRequest = new LottoPurchaseRequest(10000);
        LottoPurchaseResponse lottoPurchaseResponse = lottoService.purchaseLottosWith(purchaseRequest);

        //when
        PurchaseDetailResponse saved = lottoService.getPurchaseDetail(lottoPurchaseResponse.id());

        //then
        assertThat(saved.purchaseAmount()).isEqualTo(purchaseRequest.purchaseAmount());
        assertThat(saved.lottoCount()).isEqualTo(purchaseRequest.purchaseAmount() / LOTTO_PRICE);
    }

    @Test
    void updateResult_당첨_내역을_업데이트한다() {
        //given

        //로또 3 개의 번호를 임의로 지정한다.
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(11, 12, 13, 14, 15, 16)),
                new Lotto(List.of(20, 21, 22, 23, 24, 25))
        );
        Long savedPurchaseId = purchaseStore.save(lottos);

        LottoResultRequest resultRequest = new LottoResultRequest(List.of(1, 2, 3, 4, 5, 6), 11);

        //when
        LottoResultResponse result = lottoService.updateResult(savedPurchaseId, resultRequest);

        //then
        assertThat(result.purchaseAmount()).isEqualTo(3000);
        assertThat(result.purchaseId()).isEqualTo(savedPurchaseId);
    }

    @Test
    void updateResult_수익률은_소수점_둘째자리까지_반올림해서_반환된다() {
        // given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(11, 12, 13, 14, 15, 16)),
                new Lotto(List.of(20, 21, 22, 23, 24, 25))
        );
        Long savedPurchaseId = purchaseStore.save(lottos);

        LottoResultRequest resultRequest =
                new LottoResultRequest(List.of(1, 2, 3, 4, 5, 6), 11);

        int purchaseAmount = lottos.size() * LOTTO_PRICE;

        // when
        LottoResultResponse result = lottoService.updateResult(savedPurchaseId, resultRequest);

        // then
        double rawReturnRate = (double) Rank.FIRST.getPrize() / purchaseAmount * 100;
        double expectedRounded = BigDecimal.valueOf(rawReturnRate)
                .setScale(ROUNDING_SCALE, RoundingMode.HALF_UP)
                .doubleValue();

        //부동 소수점 문제로 인해, 0.1까지는 오차 허용
        assertThat(result.returnRate())
                .isCloseTo(expectedRounded, within(0.1));
    }

    @Test
    void getResult_당첨_내역을_조회한다() {
        //given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(11, 12, 13, 14, 15, 16)),
                new Lotto(List.of(20, 21, 22, 23, 24, 25))
        );
        Long savedPurchaseId = purchaseStore.save(lottos);

        LottoResultRequest resultRequest =
                new LottoResultRequest(List.of(1, 2, 3, 4, 5, 6), 11);

        LottoResultResponse resultResponse = lottoService.updateResult(savedPurchaseId, resultRequest);

        // when
        LottoResultResponse result = lottoService.getResult(savedPurchaseId);

        //then
        assertThat(result.purchaseId()).isEqualTo(resultResponse.purchaseId());
        assertThat(result.purchaseAmount()).isEqualTo(resultResponse.purchaseAmount());
        assertThat(result.returnRate()).isEqualTo(resultResponse.returnRate());
    }

    @Test
    void getStatistics_결과가_없으면_빈_통계를_반환한다() {
        // given && when
        ExpectedStatistics stats = lottoService.getStatistics();

        // then
        assertThat(stats.totalSamples()).isEqualTo(0);
        assertThat(stats.averageReturnRate()).isEqualTo(0.0);
        assertThat(stats.accumulatedRankCounts()).isEmpty();
    }

    @Test
    void getStatistics_결과가_있으면_누적된_통계를_반환한다() {
        // given

        // 첫 번째 결과 (1등 1개, NONE 2개)
        resultStore.save(
                1L,
                new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7),
                2_000_000_000,
                150.0,
                List.of(
                        new RankCount(Rank.FIRST, 1),
                        new RankCount(Rank.NONE, 2)
                )
        );

        // 두 번째 결과 (3등 1개, NONE 5개)
        resultStore.save(
                2L,
                new WinningNumbers(List.of(1, 2, 3, 10, 11, 12), 42),
                50_000_000,
                80.0,
                List.of(
                        new RankCount(Rank.THIRD, 1),
                        new RankCount(Rank.NONE, 5)
                )
        );

        // 세 번째 결과 (5등 1개, NONE 20개)
        resultStore.save(
                3L,
                new WinningNumbers(List.of(3, 4, 5, 6, 7, 8), 44),
                5_000,
                10.0,
                List.of(
                        new RankCount(Rank.FIFTH, 1),
                        new RankCount(Rank.NONE, 20)
                )
        );

        // when
        ExpectedStatistics stats = lottoService.getStatistics();

        // then
        assertThat(stats.totalSamples()).isEqualTo(3);

        // 평균 수익률: (150 + 80 + 10) / 3 = 80.0
        assertThat(stats.averageReturnRate()).isEqualTo(80.0);

        List<RankCount> accumulated = stats.accumulatedRankCounts();

        assertThat(accumulated).containsExactlyInAnyOrder(
                new RankCount(Rank.FIRST, 1),
                new RankCount(Rank.THIRD, 1),
                new RankCount(Rank.FIFTH, 1),
                new RankCount(Rank.NONE, 27)
        );
    }
}