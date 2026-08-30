package com.khrix.domain.user.port.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.User

interface GetUserUseCase : BaseUseCase<Int, User>

class UserNotFoundException(
    id: Int,
) : NoSuchElementException("No user found with id $id")
