package io.woohyeon.lotto.lotto_web.controller;

import io.woohyeon.lotto.lotto_web.dto.response.PurchaseResponse;
import io.woohyeon.lotto.lotto_web.service.LottoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LottoController {

    private final LottoService lottoService;

    public LottoController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @GetMapping("/lottos")
    public String purchaseForm() {
        return "purchase-test";
    }

    @PostMapping("/lottos")
    public String purchase(@RequestParam("purchaseAmount") int purchaseAmount, Model model) {
        PurchaseResponse purchaseResponse = lottoService.purchaseLottos(purchaseAmount);
        model.addAttribute("purchase", purchaseResponse);
        return "purchase-result-test";
    }

    @GetMapping("/winning-numbers")
    public String winningForm() {
        return "winning-number-test";
    }
}
