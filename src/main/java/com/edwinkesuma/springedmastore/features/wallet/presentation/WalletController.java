package com.edwinkesuma.springedmastore.features.wallet.presentation;

import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestDebitDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.usecase.DebitUseCase;
import com.edwinkesuma.springedmastore.features.wallet.application.usecase.TopUpUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final TopUpUseCase topUpUseCase;
    private final DebitUseCase debitUseCase;

    @PostMapping("/topup")
    public ResponseEntity<ResponseWalletTransactionDTO> topUp(@Valid @RequestBody RequestTopUpDTO dto) {
        ResponseWalletTransactionDTO transaction = topUpUseCase.execute(dto);

        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/debit")
    public ResponseEntity<ResponseWalletTransactionDTO> debit(@Valid @RequestBody RequestDebitDTO request) {
        ResponseWalletTransactionDTO transaction = debitUseCase.execute(request);

        return ResponseEntity.ok(transaction);
    }
}
