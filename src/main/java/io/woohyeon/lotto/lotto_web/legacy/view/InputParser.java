package io.woohyeon.lotto.lotto_web.legacy.view;

import java.util.Arrays;
import java.util.List;

public class InputParser {

    //예외 메시지
    public static final String NOT_A_NUMBER_ERROR_MESSAGE =
            "[ERROR] 입력하신 값이 숫자가 아닙니다.";

    private static final String LOTTO_NUMBER_DELIMITER = ",";

    public static int parseInteger(String numberInput) {
        int purchaseAmount;

        try {
            purchaseAmount = Integer.parseInt(numberInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException( NOT_A_NUMBER_ERROR_MESSAGE);
        }

        return purchaseAmount;
    }

    public static List<Integer> parseRawNumbers(String rawNumbers) {
        String[] splits = rawNumbers.split(LOTTO_NUMBER_DELIMITER);

        return Arrays.stream(splits)
                .map(String::trim)
                .map(InputParser::parseInteger)
                .toList();
    }

}
