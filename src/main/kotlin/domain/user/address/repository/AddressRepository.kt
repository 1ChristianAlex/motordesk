package com.khrix.domain.user.address.repository

import com.khrix.domain.user.address.model.Address
import com.khrix.domain.core.*

interface AddressRepository :
    BaseRead<Address>,
    BaseUpdate<Address>,
    BaseCreate<Address>,
    BaseDelete,
    BaseCreateReturn<Address>
