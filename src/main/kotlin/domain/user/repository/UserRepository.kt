package com.khrix.domain.user.repository

import com.khrix.domain.core.*
import com.khrix.domain.user.model.User

interface UserRepository :
    BaseRead<User>,
    BaseUpdate<User>,
    BaseDelete,
    BaseCreateReturn<User>,
    BaseCreate<User>
