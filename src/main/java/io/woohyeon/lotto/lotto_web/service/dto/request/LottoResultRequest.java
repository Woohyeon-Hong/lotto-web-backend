package io.woohyeon.lotto.lotto_web.service.dto.request;

import java.util.List;

public record LottoResultRequest(
        List<Integer> lottoNumbers,
        int bonusNumber
) {
}
