package io.woohyeon.lotto.lotto_web.legacy.view;


import camp.nextstep.edu.missionutils.Console;
import io.woohyeon.lotto.lotto_web.model.PurchaseAmount;
import io.woohyeon.lotto.lotto_web.support.InputParser;
import java.util.List;

public class InputView {

    public InputView() {
    }

    public PurchaseAmount inputPurchaseAmount() {
        String purchaseAmountInput = Console.readLine();
        int value = InputParser.parseInteger(purchaseAmountInput);
        return new PurchaseAmount(value);
    }

    public List<Integer> inputLottoNumbers() {
        String numbersInput = Console.readLine();
        return InputParser.parseRawNumbers(numbersInput);
    }

    public int inputBonusNumber() {
        String numberInput = Console.readLine();
        return InputParser.parseInteger(numberInput);
    }
}
