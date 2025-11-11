package lotto.model;

import static lotto.support.LottoRules.LOTTO_NUMBER_COUNT;
import static lotto.support.LottoRules.MAX_NUMBER;
import static lotto.support.LottoRules.MIN_NUMBER;

import java.util.HashSet;
import java.util.List;

public class WinningNumbers {

    //예외 메시지
    public static final String INVALID_LOTTO_COUNT_ERROR_MESSAGE =
            "[ERROR] 당첨 번호는 6개여야 합니다.";
    public static final String OUT_OF_RANGE_NUMBER_ERROR_MESSAGE =
            "[ERROR] 입력하신 숫자는 1에서 45 사이의 값이어야 합니다.";
    public static final String DUPLICATE_LOTTO_NUMBER_ERROR_MESSAGE =
            "[ERROR] 당첨 번호는 서로 달라야 합니다.";
    public static final String DUPLICATE_BONUS_NUMBER_ERROR_MESSAGE =
            "[ERROR] 입력하신 보너스 번호가 이미 당첨번호에 포함돼 있습니다.";

    private final List<Integer> numbers;
    private final int bonusNumber;

    public WinningNumbers(List<Integer> lottoNumbers, int bonusNumber) {
        validate(lottoNumbers, bonusNumber);

        this.numbers = List.copyOf(lottoNumbers);
        this.bonusNumber = bonusNumber;
    }

    public Rank evaluate(Lotto lotto) {
        int matchCount = lotto.countMatchingNumbers(this.numbers);
        boolean bonusMatch = lotto.hasBonusNumber(this.bonusNumber);
        return Rank.of(matchCount, bonusMatch);
    }

    public void validate(List<Integer> lottoNumbers, int bonusNumber) {
        validateLottoNumbers(lottoNumbers);
        validateBonusNumber(lottoNumbers, bonusNumber);
    }

    private void validateLottoNumbers(List<Integer> lottoNumbers) {
        validateLottoNumberCount(lottoNumbers);
        validateUniqueLottoNumbers(lottoNumbers);
        lottoNumbers.forEach(this::validateLottoNumberRange);
    }

    private void validateBonusNumber(List<Integer> lottoNumbers, int number) {
        validateLottoNumberRange(number);
        validateBonusNumberNotInWinningNumbers(lottoNumbers, number);
    }


    private void validateLottoNumberCount(List<Integer> winningNumbers) {
        if (winningNumbers.size() != LOTTO_NUMBER_COUNT) {
            throw new IllegalArgumentException(INVALID_LOTTO_COUNT_ERROR_MESSAGE);
        }
    }


    private void validateUniqueLottoNumbers(List<Integer> winningNumbers) {
        if (new HashSet<Integer>(winningNumbers).size() != LOTTO_NUMBER_COUNT) {
            throw new IllegalArgumentException(DUPLICATE_LOTTO_NUMBER_ERROR_MESSAGE);
        }
    }

    private void validateLottoNumberRange(int number) {
        if (number > MAX_NUMBER || number < MIN_NUMBER) {
            throw new IllegalArgumentException(OUT_OF_RANGE_NUMBER_ERROR_MESSAGE);
        }
    }


    private void validateBonusNumberNotInWinningNumbers(List<Integer> winningNumbers, int number) {
        if (winningNumbers.contains(number)) {
            throw new IllegalArgumentException(DUPLICATE_BONUS_NUMBER_ERROR_MESSAGE);
        }
    }
}
