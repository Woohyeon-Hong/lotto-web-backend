package io.woohyeon.lotto.lotto_web.dto.response;

import io.woohyeon.lotto.lotto_web.model.Lotto;
import java.util.List;

public record PurchaseResponse(
        int issuedCount,
        List<IssuedLotto> issuedLottos
) {
    public static PurchaseResponse from(List<Lotto> lottos) {
        return new PurchaseResponse(lottos.size(),
                IssuedLotto.fromList(lottos));
    }
}
