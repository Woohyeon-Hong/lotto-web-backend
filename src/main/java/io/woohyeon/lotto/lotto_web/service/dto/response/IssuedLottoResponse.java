package io.woohyeon.lotto.lotto_web.service.dto.response;

import io.woohyeon.lotto.lotto_web.model.Lotto;
import java.time.LocalDateTime;
import java.util.List;

public record IssuedLottoResponse(
        List<Integer> numbers,
        LocalDateTime issuedAt
) {
    public static IssuedLottoResponse from(Lotto lotto, LocalDateTime issuedAt) {
        return new IssuedLottoResponse(lotto.getNumbers(), issuedAt);
    }

    public static List<IssuedLottoResponse> from(List<Lotto> lottos, LocalDateTime issuedAt) {
        return lottos.stream()
                .map(lotto -> from(lotto, issuedAt))
                .toList();
    }
}
