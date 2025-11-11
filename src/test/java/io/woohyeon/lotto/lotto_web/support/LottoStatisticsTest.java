package lotto.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map.Entry;
import lotto.model.Lotto;
import lotto.model.Rank;
import lotto.model.WinningNumbers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LottoStatisticsTest {

    private LottoStatistics lottoStatistics;

    @BeforeEach
    void beforeEach() {
        WinningNumbers winningNumbers =
                new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);

        //1등: 1 개, 2등 1 개, 3등: 2 개, 4등: 3 개, 5등: 4 개
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),       // 1등
                new Lotto(List.of(1, 2, 3, 4, 5, 7)),       // 2등 (보너스 포함)
                new Lotto(List.of(1, 2, 3, 4, 5, 10)),      // 3등 (1)
                new Lotto(List.of(1, 2, 3, 4, 5, 10)),      // 3등 (2)
                new Lotto(List.of(1, 2, 3, 4, 10, 11)),     // 4등 (1)
                new Lotto(List.of(1, 2, 3, 4, 10, 11)),     // 4등 (2)
                new Lotto(List.of(1, 2, 3, 4, 10, 11)),     // 4등 (3)
                new Lotto(List.of(1, 2, 3, 10, 11, 12)),    // 5등 (1)
                new Lotto(List.of(1, 2, 3, 10, 11, 12)),    // 5등 (2)
                new Lotto(List.of(1, 2, 3, 10, 11, 12)),    // 5등 (3)
                new Lotto(List.of(1, 2, 3, 10, 11, 12)),    // 5등 (4)
                new Lotto(List.of(1, 2, 10, 11, 12, 13))   // 낙첨
        );

        lottoStatistics = new LottoStatistics(winningNumbers, lottos, 12);
    }

    @Test
    void calculateRankCounts_당첨된_개수가_많은_순서대로_정렬하여_등수_별_당첨_횟수를_출력한다() {
        //when
        lottoStatistics.calculateRankCounts();
        List<Entry<Rank, Long>> sorted = lottoStatistics.getRankCounts();

        //then
        // 당첨된 개수가 많은 순서대로 정렬한다.
        assertThat(sorted.get(0).getKey()).isSameAs(Rank.FIFTH);
        assertThat(sorted.get(1).getKey()).isSameAs(Rank.FOURTH);
        assertThat(sorted.get(2).getKey()).isSameAs(Rank.THIRD);

        //당첨된 개수가 동일하면, 등수에 따라 내림차순으로 정렬한다.
        assertThat(sorted.get(3).getKey()).isSameAs(Rank.NONE);
        assertThat(sorted.get(4).getKey()).isSameAs(Rank.SECOND);
        assertThat(sorted.get(5).getKey()).isSameAs(Rank.FIRST);
    }


    @Test
    void calculateRateOfReturn_로또의_수익률을_계산한다() {
        //given
        lottoStatistics.calculateRankCounts();

        //when
        lottoStatistics.calculateRateOfReturn();
        double rateOfReturn = lottoStatistics.getRateOfReturn();

        //then
        assertThat(rateOfReturn).isEqualTo(1.69430833333E10);
    }
}