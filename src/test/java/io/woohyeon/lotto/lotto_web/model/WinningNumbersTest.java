package io.woohyeon.lotto.lotto_web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class WinningNumbersTest {

//    @Test
//    void validateLottoNumberCount_당첨번호가_6개가_아니면_예외가_발생한다() {
//        //given
//        List<Integer> lottoNumbers = List.of(1,2,3,4,5,6,7);
//
//        //when && then
//        assertThatThrownBy(() ->
//                inputView.validateLottoNumberCount(numbers))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void validateUniqueLottoNumbers_입렫된_6개의_당첨번호는_서로_달라야한다() {
//        //given
//        List<Integer> numbers = List.of(1,1,2,3,4,5,6);
//
//        //when && then
//        assertThatThrownBy(() ->
//                inputView.validateLottoNumberCount(numbers))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void validateLottoNumberRange_1에서_45_사이_밖의_범위의_숫자가_입력되면_예외가_발생한다() {
//        //given
//        int outOfRangeNumber = 46;
//
//        //when && then
//        assertThatThrownBy(() ->
//                inputView.validateLottoNumberRange(outOfRangeNumber))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void validateBonusNumberNotInWinningNumbers_당첨번호에_포함된_번호가_입력되면_예외가_발생한다() {
//        //given
//        List<Integer> winningNumbers = List.of(1,2,3,4,5,6);
//        int bonusNumber = 1;
//
//        //when && then
//        assertThatThrownBy(() ->
//                inputView.validateBonusNumberNotInWinningNumbers(winningNumbers, bonusNumber))
//                .isInstanceOf(IllegalArgumentException.class);
//    }

    @Test
    void validate_로또_번호에는_서로_다른_1에서_45_사이의_숫자_6개가_와야_한다() {
        //given
        List<Integer> correct = List.of(1,2,3,4,5,6); //서로 다른 6개의 1에서 45 사이의 숫자들

        List<List<Integer>> wrongs = List.of(
                List.of(1,2,3,4,5,6,7), // 로또 번호가 6개가 아닌 경우
                List.of(1,1,2,3,4,5,6),  // 중복되는 번호가 포함된 경우
                List.of(1,1,2,3,4,5,46)  // 1에서 45 사이의 숫자가 아닌 경우
        );

        int bonusNumber = 11;   // 로또 숫자와 중복되지 않는 1에서 45 사이의 숫자


        //when && then
        assertThat(new WinningNumbers(correct, bonusNumber)).isInstanceOf(WinningNumbers.class);

        wrongs.forEach(wrong ->
                assertThatThrownBy(
                        (() -> new WinningNumbers(wrong, bonusNumber))
                ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void validate_보너스_번호에는_로또_번화와는_다른_1에서_45_사이의_숫자가_와야_한다() {
        //given
        List<Integer> lottoNumbers = List.of(1,2,3,4,5,6);  //서로 다른 6개의 1에서 45 사이의 숫자들

        int correct = 11;   // 로또 숫자와 중복되지 않는 1에서 45 사이의 숫자

        List<Integer> wrongs = List.of(
                1,  //로또 숫자와 중복되는 경우
                0, // 1보다 작은 경우
                46  //45보다 큰 경우
        );

        //when && then
        assertThat(new WinningNumbers(lottoNumbers, correct)).isInstanceOf(WinningNumbers.class);

        wrongs.forEach(wrong ->
                assertThatThrownBy(
                        (() -> new WinningNumbers(lottoNumbers, wrong))
                ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void evaluate_일치_개수와_보너스_번호_일_여부에_따라_당첨_등수를_결정한다() {
        //given
        WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);

        List<Lotto> lottos = List.of(
                new Lotto(List.of(1,2,3,4,5,6)),        // 1등
                new Lotto(List.of(1,2,3,4,5,7)),        // 2등
                new Lotto(List.of(1,2,3,4,5,10)),       // 3등
                new Lotto(List.of(1,2,3,4,10,11)),      // 4등
                new Lotto(List.of(1,2,3,10,11,12)),     // 5등
                new Lotto(List.of(1,2,10,11,12,13)),    // 등수 외
                new Lotto(List.of(10,11,12,13,14,7))    // 보너스 번호만 당첨
        );

        //when
        Rank first = winningNumbers.evaluate(lottos.get(0));
        Rank second = winningNumbers.evaluate(lottos.get(1));
        Rank third = winningNumbers.evaluate(lottos.get(2));
        Rank fourth = winningNumbers.evaluate(lottos.get(3));
        Rank fifth = winningNumbers.evaluate(lottos.get(4));
        Rank none = winningNumbers.evaluate(lottos.get(5));
        Rank onlyBonusNumber = winningNumbers.evaluate(lottos.get(6));

        //then
        assertThat(first).isSameAs(Rank.FIRST);
        assertThat(second).isSameAs(Rank.SECOND);
        assertThat(third).isSameAs(Rank.THIRD);
        assertThat(fourth).isSameAs(Rank.FOURTH);
        assertThat(fifth).isSameAs(Rank.FIFTH);
        assertThat(none).isSameAs(Rank.NONE);
        assertThat(onlyBonusNumber).isSameAs(Rank.NONE);
    }
}