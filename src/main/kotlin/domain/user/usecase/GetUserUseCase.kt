package com.khrix.domain.user.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.User

interface GetUserUseCase : BaseUseCase<Int, User>

class UserNotFoundException(id: Int) : Exception("No user found with id $id")