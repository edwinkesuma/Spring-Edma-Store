package com.edwinkesuma.springedmastore.features.wallet.application.mapper;

import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletTransactionMapper {
    ResponseWalletTransactionDTO toDTO(WalletTransaction walletTransaction);
}
