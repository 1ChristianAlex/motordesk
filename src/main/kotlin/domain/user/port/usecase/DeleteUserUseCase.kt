package com.khrix.domain.user.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.User

interface DeleteUserUseCase : BaseUseCase<User, Unit>
