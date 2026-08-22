package com.khrix.domain.user.address.port.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.user.address.model.Address

interface AddressRepository :
    BaseRead<Address>,
    BaseUpdate<Address>,
    BaseCreate<Address>,
    BaseDelete,
    BaseCreateReturn<Address>
