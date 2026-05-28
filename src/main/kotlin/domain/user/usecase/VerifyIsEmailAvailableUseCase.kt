package com.khrix.domain.user.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.valueobject.user.Email


interface VerifyIsEmailAvailableUseCase : BaseUseCase<Email, Boolean>