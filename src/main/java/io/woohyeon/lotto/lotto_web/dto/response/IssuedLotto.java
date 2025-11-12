package io.woohyeon.lotto.lotto_web.dto.response;

import io.woohyeon.lotto.lotto_web.model.Lotto;
import java.time.LocalDateTime;
import java.util.List;

public record IssuedLotto(
        List<Integer> numbers,
        LocalDateTime issuedAt
) {

    public static IssuedLotto from(Lotto lotto) {
        return new IssuedLotto(lotto.getNumbers(), LocalDateTime.now());
    }

    public static List<IssuedLotto> fromList(List<Lotto> lottos) {
        return lottos.stream()
                .map(IssuedLotto::from)
                .toList();
    }
}
