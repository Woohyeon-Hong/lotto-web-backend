package io.woohyeon.lotto.lotto_web.support;

import io.woohyeon.lotto.lotto_web.dto.response.LottoResultResponse;
import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import io.woohyeon.lotto.lotto_web.model.PurchaseLog;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class LottoStore {

    private Long id = 1L;
    private final List<PurchaseLog> logs = new ArrayList<>();

    public long save(PurchaseResponse purchase, LottoResultResponse result) {
        long id = this.id++;
        logs.add(PurchaseLog.from(purchase, result, id));
        return id;
    }

    public Optional<PurchaseLog> findById(long id) {
        return logs.stream().filter(l -> l.id() == id).findFirst();
    }

    public List<PurchaseLog> findRecentRecords() {
        List<PurchaseLog> copy = new ArrayList<>(logs);
        copy.sort(Comparator.comparing(PurchaseLog::purchasedAt).reversed());
        return copy;
    }
}
