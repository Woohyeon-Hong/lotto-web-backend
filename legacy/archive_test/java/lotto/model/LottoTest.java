package lotto.model;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // TODO: 추가 기능 구현에 따른 테스트 코드 작성

    @Test
    void 로또_번호에_1에서_45_를_벗어나는_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void formatNumbers_로또_번호를_오름차순으로_정렬해_문자열로_반환한다() {
        //given
        Lotto lotto = new Lotto(List.of(21, 8, 41, 23, 42, 43));

        //when
        String formattedNumbers = lotto.formatNumbers();

        //then
        assertThat(formattedNumbers).isEqualTo("[8, 21, 23, 41, 42, 43]");
    }

    @Test
    void countMatchingNumbers_당첨번호와_일치돠는_개수를_반환한다() {
        //given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);

        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 10, 11, 12, 13, 14)),     // 1 개 일치
                new Lotto(List.of(1, 2, 11, 12, 13, 14)),      // 2 개 일치
                new Lotto(List.of(1, 2, 3, 12, 13, 14)),       // 3 개 일치
                new Lotto(List.of(1, 2, 3, 4, 13, 14)),        // 4 개 일치
                new Lotto(List.of(1, 2, 3, 4, 5, 14)),         // 5 개 일치
                new Lotto(List.of(1, 2, 3, 4, 5, 6))           // 6 개 일치
        );

        //when
        List<Integer> results = new ArrayList<Integer>();
        lottos.forEach(lotto -> results.add(lotto.countMatchingNumbers(winningNumbers)));

        //then
        for (int i = 1; i <= 6; i++) {
            assertThat(results.get(i - 1)).isEqualTo(i);
        }
    }

    @Test
    void hasBonusNumber_보너스_번호가_포함되면_True를_반환한다() {
        //given
        int bonusNumber = 1;
        Lotto includingBonusNumber = new Lotto(List.of(1,2,3,4,5,6));

        //when
        boolean hasBonus = includingBonusNumber.hasBonusNumber(bonusNumber);

        //then
        assertThat(hasBonus).isTrue();
    }
}
