package io.woohyeon.lotto.lotto_web.dto.request;

import io.woohyeon.lotto.lotto_web.dto.response.IssuedLotto;
import java.util.List;

public record LottoResultRequest(
        List<IssuedLotto> issuedLottos,
        List<Integer> lottoNumbers,
        int bonusNumber
) {
}
