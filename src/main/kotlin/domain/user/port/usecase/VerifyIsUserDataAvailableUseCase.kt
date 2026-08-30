package com.khrix.domain.user.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email

data class VerifyIsUserDataAvailableUseCaseCommand(
    val email: Email,
    val cpf: CPF,
)

interface VerifyIsUserDataAvailableUseCase : BaseUseCase<VerifyIsUserDataAvailableUseCaseCommand, Unit>
