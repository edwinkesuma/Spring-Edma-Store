package com.edwinkesuma.springedmastore.features.wallet.application.mapper;

import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletTransactionMapper {

    @Mapping(target = "transactionId", source = "id")
    ResponseWalletTransactionDTO toDTO(WalletTransaction walletTransaction);
}
