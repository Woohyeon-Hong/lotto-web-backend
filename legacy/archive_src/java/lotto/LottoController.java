package lotto;

import static lotto.support.LottoRules.LOTTO_PRICE;

import java.util.List;
import lotto.model.Lotto;
import lotto.model.PurchaseAmount;
import lotto.support.LottoGenerator;
import lotto.support.LottoStatistics;
import lotto.model.WinningNumbers;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {

    private final InputView inputView;
    private final OutputView outputView;

    public LottoController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public void run() {

        try {
            List<Lotto> lottos = purchaseLottos();
            WinningNumbers winningNumbers = drawWinningNumber();
            processLottoStatistics(winningNumbers, lottos);
        } catch (Exception e) {
            outputView.printErrorMessage(e.getMessage());
        }

    }

    private List<Lotto> purchaseLottos() {
        PurchaseAmount purchaseAmount = requestPurchaseAmount();

        LottoGenerator lottoGenerator = new LottoGenerator(purchaseAmount);
        outputView.printLottoCount(lottoGenerator.getLottoCount());

        List<Lotto> lottos = lottoGenerator.generateLottos();
        outputView.printLottos(lottos);

        return lottos;
    }

    private WinningNumbers drawWinningNumber() {
        List<Integer> lottoNumbers = requestLottoNumbers();
        int bonusNumber = requestBonusNumber();
        return new WinningNumbers(lottoNumbers, bonusNumber);
    }

    private PurchaseAmount requestPurchaseAmount() {
        outputView.printPurchaseAmountInputPrompt();
        return inputView.inputPurchaseAmount();
    }

    private List<Integer> requestLottoNumbers() {
        outputView.printLottoNumbersInputPrompt();
        List<Integer> lottoNumbers = inputView.inputLottoNumbers();
        return lottoNumbers;
    }

    private int requestBonusNumber() {
        outputView.printBonusNumberInputPrompt();
        int bonusNumber = inputView.inputBonusNumber();
        return bonusNumber;
    }

    private void processLottoStatistics(WinningNumbers winningNumbers, List<Lotto> lottos) {
        LottoStatistics statistics =
                computeStatistics(winningNumbers, lottos, lottos.size() * LOTTO_PRICE);

        printLottoStatistics(statistics);
    }

    private static LottoStatistics computeStatistics(WinningNumbers winningNumbers, List<Lotto> lottos,
                                                     int purchaseAmount) {
        LottoStatistics lottoStatistics = new LottoStatistics(winningNumbers, lottos, purchaseAmount);
        lottoStatistics.compute();
        return lottoStatistics;
    }

    private void printLottoStatistics(LottoStatistics lottoStatistics) {
        outputView.printLottoStatistics(lottoStatistics);
        outputView.printRateOfReturn(lottoStatistics);
    }
}
