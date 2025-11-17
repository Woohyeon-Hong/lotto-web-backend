package io.woohyeon.lotto.lotto_web.repository;

import static io.woohyeon.lotto.lotto_web.support.LottoRules.LOTTO_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import io.woohyeon.lotto.lotto_web.model.Lotto;
import io.woohyeon.lotto.lotto_web.model.RankCount;
import io.woohyeon.lotto.lotto_web.model.ResultRecord;
import io.woohyeon.lotto.lotto_web.model.WinningNumbers;
import io.woohyeon.lotto.lotto_web.support.LottoStatistics;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultStoreTest {

    ResultStore resultStore;
    PurchaseStore purchaseStore;

    @BeforeEach
    void beforeEach() {
        resultStore = new ResultStore();
        purchaseStore = new PurchaseStore();
    }

    @Test
    void save_purchaseId를_id로_해서_저장한다() {
        //given
        List<Lotto> purchasedLottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(7, 8, 9, 10, 11, 12)),
                new Lotto(List.of(1, 3, 5, 7, 9, 11))
        );
        Long purchaseId = purchaseStore.save(purchasedLottos);

        WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 12);

        LottoStatistics lottoStatistics = new LottoStatistics(winningNumbers, purchasedLottos,
                purchasedLottos.size() * LOTTO_PRICE);
        lottoStatistics.compute();

        List<RankCount> rankCounts = RankCount.fromEntries(lottoStatistics.getRankCounts());

        int totalPrize = lottoStatistics.getRankCounts().stream()
                .mapToInt(entry -> entry.getKey().getPrize() * entry.getValue().intValue())
                .sum();

        double returnRate = lottoStatistics.getRateOfReturn();

        //when
        Long savedId = resultStore.save(purchaseId, winningNumbers, totalPrize, lottoStatistics.getRateOfReturn(), rankCounts);

        //then
        ResultRecord resultRecord = resultStore.findByPurchaseId(savedId).get();

        assertThat(resultRecord.getTotalPrize()).isEqualTo(totalPrize);
        assertThat(resultRecord.getReturnRate()).isEqualTo(returnRate);
        assertThat(resultRecord.getWinningNumbers()).isEqualTo(winningNumbers);
        assertThat(resultRecord.getRankCounts()).isEqualTo(rankCounts);
    }

    @Test
    void findAll_모든_당첨_내역을_반환한다() {
        //given

        // 구매 1
        List<Lotto> purchasedLottos1 = List.of(new Lotto(List.of(1, 2, 3, 4, 5, 6)));
        Long purchaseId1 = purchaseStore.save(purchasedLottos1);

        WinningNumbers winningNumbers1 = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 12);
        LottoStatistics statistics1 = new LottoStatistics(
                winningNumbers1,
                purchasedLottos1,
                purchasedLottos1.size() * LOTTO_PRICE
        );
        statistics1.compute();

        List<RankCount> rankCounts1 = RankCount.fromEntries(statistics1.getRankCounts());

        int totalPrize1 = statistics1.getRankCounts().stream()
                .mapToInt(entry -> entry.getKey().getPrize() * entry.getValue().intValue())
                .sum();

        Long savedId1 = resultStore.save(
                purchaseId1,
                winningNumbers1,
                totalPrize1,
                statistics1.getRateOfReturn(),
                rankCounts1
        );

        // 구매 2
        List<Lotto> purchasedLottos2 = List.of(new Lotto(List.of(1, 3, 5, 7, 9, 11)));
        Long purchaseId2 = purchaseStore.save(purchasedLottos2);

        WinningNumbers winningNumbers2 = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 12);
        LottoStatistics statistics2 = new LottoStatistics(
                winningNumbers2,
                purchasedLottos2,
                purchasedLottos2.size() * LOTTO_PRICE
        );
        statistics2.compute();

        List<RankCount> rankCounts2 = RankCount.fromEntries(statistics2.getRankCounts());

        int totalPrize2 = statistics2.getRankCounts().stream()
                .mapToInt(entry -> entry.getKey().getPrize() * entry.getValue().intValue())
                .sum();

        Long savedId2 = resultStore.save(
                purchaseId2,
                winningNumbers2,
                totalPrize2,
                statistics2.getRateOfReturn(),
                rankCounts2
        );

        // when
        List<ResultRecord> result = resultStore.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(savedId1).isEqualTo(purchaseId1);
        assertThat(savedId2).isEqualTo(purchaseId2);

        assertThat(result.get(0).getCreatedAt()).isBefore(result.get(1).getCreatedAt());

        assertThat(result.get(0).getWinningNumbers()).isEqualTo(winningNumbers1);
        assertThat(result.get(1).getWinningNumbers()).isEqualTo(winningNumbers2);

        assertThat(result.get(0).getTotalPrize()).isEqualTo(totalPrize1);
        assertThat(result.get(1).getTotalPrize()).isEqualTo(totalPrize2);
    }
}