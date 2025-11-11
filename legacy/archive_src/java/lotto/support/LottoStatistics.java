package lotto.support;

import static lotto.support.LottoRules.ROUNDING_SCALE;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lotto.model.Lotto;
import lotto.model.Rank;
import lotto.model.WinningNumbers;

public class LottoStatistics {

    private static final String UNCOMPUTED_STATISTICS_ERROR_MESSAGE =
            "[ERROR] 당첨 통계가 아직 계산되지 않았습니다. compute() 호출이 필요합니다.";

    private static final String UNCOMPUTED_RATE_OF_RETURN_ERROR_MESSAGE =
            "[ERROR] 수익률이 아직 계산되지 않았습니다. compute() 호출이 필요합니다.";

    private final WinningNumbers winningNumbers;
    private final List<Lotto> lottos;
    private final int purchaseAmount;

    private List<Entry<Rank, Long>> rankCounts;
    private double rateOfReturn;

    public LottoStatistics(WinningNumbers winningNumbers, List<Lotto> lottos, int purchaseAmount) {
        this.winningNumbers = winningNumbers;
        this.lottos = List.copyOf(lottos);
        this.purchaseAmount = purchaseAmount;
        this.rateOfReturn = -1;
    }

    public List<Entry<Rank, Long>> getRankCounts() {
        if (rankCounts == null) {
            throw new IllegalStateException(UNCOMPUTED_STATISTICS_ERROR_MESSAGE);
        }
        return List.copyOf(rankCounts);
    }

    public double getRateOfReturn() {
        if (rateOfReturn == -1) {
            throw new IllegalStateException(UNCOMPUTED_RATE_OF_RETURN_ERROR_MESSAGE);
        }

        return rateOfReturn;
    }

    public void compute() {
        calculateRankCounts();
        calculateRateOfReturn();
    }

    public void calculateRankCounts() {
        Map<Rank, Long> unsortedRankCounts = countRanks();
        rankCounts = sortByWinningCountDescThenRankDesc(unsortedRankCounts);
    }

    public void calculateRateOfReturn() {
        long totalPrize = calculateTotalPrize();
        double rate = calculateRate(totalPrize);
        rateOfReturn = roundToOneDecimal(rate);
    }

    private  Map<Rank, Long> countRanks() {
        //모든 등수 별 개수를 0으로 초기화한다.
        Map<Rank, Long> counts = new EnumMap<>(Rank.class);
        Arrays.stream(Rank.values()).forEach(r -> counts.put(r, 0L));

        //모든 로또의 당첨 등수를 계산해서, 등수 별 개수를 1씩 누적한다.
        lottos.stream()
                .map(winningNumbers::evaluate)
                .forEach(rank -> counts.compute(rank, (k, v) -> v + 1));

        return counts;
    }

    private List<Entry<Rank, Long>> sortByWinningCountDescThenRankDesc(Map<Rank, Long> rankCounts) {
        Comparator<Map.Entry<Rank, Long>> byWinningCountDesc =
                Map.Entry.<Rank, Long>comparingByValue(Comparator.reverseOrder());

        Comparator<Map.Entry<Rank, Long>> byRankDesc =
                Comparator.comparing(
                        Map.Entry::getKey,
                        Comparator.comparingInt(Rank::getOrder).reversed()
                );

        return rankCounts.entrySet().stream()
                .sorted(byWinningCountDesc.thenComparing(byRankDesc))
                .toList();
    }

    private long calculateTotalPrize() {
        // rankCounts의 각 엔트리(<Rank, 개수>)에 대해 Rank의 상금과 개수를 곱한 값을 합산하여 총 상금을 구한다.
        return rankCounts.stream()
                .mapToLong(e -> e.getKey().getPrize() * e.getValue())
                .sum();
    }

    private double calculateRate(long totalPrize) {
       return  (totalPrize * 1.0 / purchaseAmount) * 100;
    }

    private static double roundToOneDecimal(double rate) {
        return Math.round(rate * ROUNDING_SCALE) / (double) ROUNDING_SCALE;
    }
}
