package lotto.model;

import static lotto.support.LottoRules.LOTTO_PRICE;

public class PurchaseAmount {

    //예외 메시지
    public static final String NOT_NATURAL_NUMBER_ERROR_MESSAGE =
            "[ERROR] 입력하신 값이 자연수가 아닙니다.";
    public static final String NOT_MULTIPLE_OF_THOUSAND_ERROR_MESSAGE =
            "[ERROR] 입력하신 금액이 1000원 단위가 아닙니다.";

    private final int value;

    public PurchaseAmount(int value) {
        validate(value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void validate(int purchaseAmount) {
        validateNaturalNumber(purchaseAmount);
        validateMultipleOfThousand(purchaseAmount);
    }

    private void validateNaturalNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException(NOT_NATURAL_NUMBER_ERROR_MESSAGE);
        }
    }

    private void validateMultipleOfThousand(int purchaseAmount) {
        if (purchaseAmount % LOTTO_PRICE != 0) {
            throw new IllegalArgumentException(NOT_MULTIPLE_OF_THOUSAND_ERROR_MESSAGE);
        }
    }
}
