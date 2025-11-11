package lotto.model;

import static lotto.support.LottoRules.LOTTO_NUMBER_COUNT;
import static lotto.support.LottoRules.MAX_NUMBER;
import static lotto.support.LottoRules.MIN_NUMBER;

import java.util.HashSet;
import java.util.List;

public class Lotto {

    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        validateNumberCount(numbers);
        validateDuplicate(numbers);
        validateRange(numbers);
    }


    // TODO: 추가 기능 구현
    public String formatNumbers() {
        return numbers.stream().sorted().toList().toString();
    }

    public int countMatchingNumbers(List<Integer> lottoNumbers) {
        return (int) numbers.stream()
                .filter(lottoNumbers::contains)
                .count();
    }

    public boolean hasBonusNumber(int bonusNumber) {
        return numbers.contains(bonusNumber);
    }

    private static void validateNumberCount(List<Integer> numbers) {
        if (numbers.size() != LOTTO_NUMBER_COUNT) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는" +  LOTTO_NUMBER_COUNT + "개여야 합니다.");
        }
    }

    private static void validateDuplicate(List<Integer> numbers) {
        if (new HashSet<>(numbers).size() < LOTTO_NUMBER_COUNT) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 서로 달라야 합니다.");
        }
    }

    private void validateRange(List<Integer> numbers) {
        if (numbers.stream().anyMatch(number -> number < MIN_NUMBER || number > MAX_NUMBER)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
    }
}
