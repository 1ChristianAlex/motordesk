package com.khrix.domain.user.repository

import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.user.model.User

interface UserRepository :
    BaseRead<User>,
    BaseUpdate<User>,
    BaseDelete,
    BaseCreateReturn<User>
