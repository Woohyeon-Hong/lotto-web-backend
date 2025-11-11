package lotto.view;

import java.util.List;
import java.util.Map.Entry;
import lotto.model.Lotto;
import lotto.support.LottoStatistics;
import lotto.model.Rank;

public class OutputView {

    private static final String PURCHASE_AMOUNT_INPUT_PROMPT_MESSAGE = "구입금액을 입력해 주세요.";
    private static final String LOTTO_COUNT_RESULT_MESSAGE = "개를 구매했습니다.";
    private static final String LOTTO_NUMBERS_INPUT_PROMPT_MESSAGE = "\n당첨 번호를 입력해 주세요.";
    private static final String BONUS_NUMBER_INPUT_PROMPT_MESSAGE = "\n보너스 번호를 입력해 주세요.";
    private static final String LOTTO_STATISTICS_MESSAGE = "\n당첨 통계\n---";


    public OutputView() {
    }

    public void printPurchaseAmountInputPrompt() {
        System.out.println(PURCHASE_AMOUNT_INPUT_PROMPT_MESSAGE);
    }

    public void printLottoCount(int lottoCount) {
        System.out.println("\n" + lottoCount + LOTTO_COUNT_RESULT_MESSAGE);
    }

    public void printLottos(List<Lotto> lottos) {
        lottos.stream()
                .map(Lotto::formatNumbers)
                .forEach(System.out::println);
    }

    public void printLottoNumbersInputPrompt() {
        System.out.println(LOTTO_NUMBERS_INPUT_PROMPT_MESSAGE);
    }

    public void printBonusNumberInputPrompt() {
        System.out.println(BONUS_NUMBER_INPUT_PROMPT_MESSAGE);
    }

    public void printLottoStatistics(LottoStatistics lottoStatistics) {
        System.out.println(LOTTO_STATISTICS_MESSAGE);

        for (Entry<Rank, Long> rankCount : lottoStatistics.getRankCounts()) {
            Rank rank = rankCount.getKey();
            Long count = rankCount.getValue();

            if (rank == Rank.NONE) continue;

            System.out.printf("%s (%,d원) - %d개\n",
                    rank.getMatchingCountMessage(),
                    rank.getPrize(),
                    count);
        }
    }

    public void printRateOfReturn(LottoStatistics lottoStatistics) {
        System.out.printf("총 수익률은 %,.1f%%입니다.\n", lottoStatistics.getRateOfReturn());
    }

    public void printErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }
}
