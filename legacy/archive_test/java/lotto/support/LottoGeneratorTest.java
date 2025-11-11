package lotto.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import lotto.model.Lotto;
import lotto.model.PurchaseAmount;
import org.junit.jupiter.api.Test;

class LottoGeneratorTest {

    @Test
    void generateUniqueNumbers_서로_다른_6개의_수를_반환한다() {
        //given - 10 개의 로또를 발행한다고 가정
        LottoGenerator lottoGenerator = new LottoGenerator(new PurchaseAmount(10000));

        //when
        List<Integer> numbers = lottoGenerator.generateUniqueNumbers();

        //then
        assertThat(numbers.size()).isEqualTo(6);                        //6 개의 수를 반환하는지 검증
        assertThat(new HashSet<Integer>(numbers).size()).isEqualTo(6);  //6 개의 수가 서로 다른지 검증
        assertThat(numbers)                                                      //1 ~ 45 사이인지 검증
                .allMatch(number -> number >= 1 && number <= 45);
    }

    @Test
    void generateLottos_발행할_로또_개수를_입려받아_로또를_발행한다() {
        //given
        LottoGenerator lottoGenerator = new LottoGenerator(new PurchaseAmount(6000));

        //when
        List<Lotto> lottos = lottoGenerator.generateLottos();

        //then
        assertThat(lottos.size()).isEqualTo(6);
    }
}