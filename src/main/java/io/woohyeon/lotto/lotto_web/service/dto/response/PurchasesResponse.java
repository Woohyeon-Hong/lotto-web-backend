package io.woohyeon.lotto.lotto_web.service.dto.response;

import java.util.List;

public record PurchasesResponse(
        int count,
        List<PurchaseSummaryResponse> purchases
) {
    public static PurchasesResponse from(List<PurchaseSummaryResponse> items) {
        return new PurchasesResponse(items.size(), items);
    }
}
