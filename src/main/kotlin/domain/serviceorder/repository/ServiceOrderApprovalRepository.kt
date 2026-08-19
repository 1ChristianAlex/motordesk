package com.khrix.domain.serviceorder.repository

import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.serviceorder.model.ServiceOrderApprovalToken

interface ServiceOrderApprovalRepository :
    BaseRead<ServiceOrderApprovalToken>,
    BaseUpdate<ServiceOrderApprovalToken>,
    BaseCreateReturn<ServiceOrderApprovalToken> {
    suspend fun getByToken(token: String): ServiceOrderApprovalToken?
}
