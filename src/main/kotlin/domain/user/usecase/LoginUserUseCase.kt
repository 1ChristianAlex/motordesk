package com.khrix.domain.user.usecase

import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.model.User

interface LoginUserUseCase : BaseUseCase<LoginTypes, User>

class InvalidCredentialsException : Exception("User not found with provided credentials")