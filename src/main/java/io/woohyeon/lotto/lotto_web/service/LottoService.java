package io.woohyeon.lotto.lotto_web.service;

import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import io.woohyeon.lotto.lotto_web.model.Lotto;
import io.woohyeon.lotto.lotto_web.model.PurchaseAmount;
import io.woohyeon.lotto.lotto_web.support.LottoGenerator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LottoService {

    public PurchaseResponse purchaseLottos(int purchaseAmount) {
        LottoGenerator lottoGenerator = new LottoGenerator(new PurchaseAmount(purchaseAmount));
        List<Lotto> generateLottos = lottoGenerator.generateLottos();

        return PurchaseResponse.from(generateLottos);
    }
}
