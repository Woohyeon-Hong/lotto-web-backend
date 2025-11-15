package io.woohyeon.lotto.lotto_web.service.dto.response;

import io.woohyeon.lotto.lotto_web.model.Rank;
import io.woohyeon.lotto.lotto_web.support.LottoStatistics;
import java.util.List;
import java.util.Map.Entry;

public record LottoResultResponse(
        List<Entry<Rank, Long>> rankCounts,
        double returnRate
) {

    public static LottoResultResponse from(LottoStatistics lottoStatistics) {
        return new LottoResultResponse(lottoStatistics.getRankCounts(), lottoStatistics.getRateOfReturn());
    }
}
