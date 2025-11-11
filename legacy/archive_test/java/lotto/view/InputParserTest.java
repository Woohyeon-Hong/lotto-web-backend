package lotto.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class InputParserTest {

    @Test
    void parseInteger_숫자가_입력되면_정상적으로_파싱된다() {
        //given
        String naturalNumberInput = "1000";

        //when
        int purchaseAmount = InputParser.parseInteger(naturalNumberInput);

        //then
        assertThat(purchaseAmount).isEqualTo(1000);
    }

    @Test
    void parseInteger_숫자가_아닌_값이_입렫되면_예외가_발생한다() {
        //given
        String[] wrongs = {
                "abcde",    // 문자
                "",         // empty
                " ",        // blank
                " 1"        // 공백 포함
        };

        //when && then
        Arrays.stream(wrongs)
                .forEach(wrong ->
                        assertThatThrownBy(() ->
                                InputParser.parseInteger(wrong))
                                .isInstanceOf(IllegalArgumentException.class));
    }
}