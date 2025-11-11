package io.woohyeon.lotto.lotto_web.service;

import static io.woohyeon.lotto.lotto_web.support.LottoRules.LOTTO_PRICE;

import io.woohyeon.lotto.lotto_web.dto.request.LottoResultRequest;
import io.woohyeon.lotto.lotto_web.dto.response.IssuedLotto;
import io.woohyeon.lotto.lotto_web.dto.response.LottoResultResponse;
import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import io.woohyeon.lotto.lotto_web.model.Lotto;
import io.woohyeon.lotto.lotto_web.model.PurchaseAmount;
import io.woohyeon.lotto.lotto_web.model.WinningNumbers;
import io.woohyeon.lotto.lotto_web.support.LottoGenerator;
import io.woohyeon.lotto.lotto_web.support.LottoStatistics;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LottoService {

    public PurchaseResponse purchaseLottosWith(int purchaseAmount) {
        LottoGenerator lottoGenerator = new LottoGenerator(new PurchaseAmount(purchaseAmount));
        List<Lotto> generatedLottos = lottoGenerator.generateLottos();
        return PurchaseResponse.from(generatedLottos);
    }


    public LottoResultResponse calculateStatisticsOf(LottoResultRequest request) {
        WinningNumbers winningNumbers = new WinningNumbers(request.lottoNumbers(), request.bonusNumber());

        List<IssuedLotto> issuedLottos = request.issuedLottos();
        List<Lotto> lottos = issuedLottos.stream().map(issuedLotto -> new Lotto(issuedLotto.numbers())).toList();

        LottoStatistics lottoStatistics = new LottoStatistics(winningNumbers, lottos,
                request.issuedLottos().size() * LOTTO_PRICE);

        lottoStatistics.compute();

        return LottoResultResponse.from(lottoStatistics);
    }
}
