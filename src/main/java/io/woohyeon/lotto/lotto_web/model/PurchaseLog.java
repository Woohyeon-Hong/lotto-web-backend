package io.woohyeon.lotto.lotto_web.model;

import io.woohyeon.lotto.lotto_web.dto.response.IssuedLotto;
import io.woohyeon.lotto.lotto_web.dto.response.LottoResultResponse;
import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;

public record PurchaseLog(
        long id,
        int purchaseAmount,
        int issuedCount,
        List<IssuedLotto> issuedLottos,
        List<Entry<Rank, Long>> rankCounts,
        double returnRate,
        LocalDateTime purchasedAt
) {

    public static PurchaseLog from(PurchaseResponse purchase, LottoResultResponse result, long id) {
        LocalDateTime issuedAt = purchase.issuedLottos().isEmpty()
                ? LocalDateTime.now()
                : purchase.issuedLottos().get(0).issuedAt();

        int amount = purchase.issuedCount() * 1000;

        return new PurchaseLog(
                id,
                amount,
                purchase.issuedCount(),
                purchase.issuedLottos(),
                result.rankCounts(),
                result.returnRate(),
                issuedAt
        );
    }
}
