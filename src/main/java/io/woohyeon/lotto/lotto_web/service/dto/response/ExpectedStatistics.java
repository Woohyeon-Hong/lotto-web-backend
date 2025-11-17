package io.woohyeon.lotto.lotto_web.service.dto.response;

import io.woohyeon.lotto.lotto_web.model.Rank;
import io.woohyeon.lotto.lotto_web.model.RankCount;
import java.util.List;
import java.util.Map.Entry;

public record ExpectedStatistics(
        int totalSamples,
        double averageReturnRate,
        List<RankCount> accumulatedRankCounts
) {
}
