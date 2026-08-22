package com.khrix.application.serviceorder

import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.serviceorder.ServiceOrderDiffResolver
import com.khrix.domain.serviceorder.model.ServiceOrder
import kotlinx.serialization.json.Json
import kotlin.time.Clock

inline fun <reified T> simpleSerialize(data: T): String = Json.encodeToString(data)

class ServiceOrderDiffResolverImpl : ServiceOrderDiffResolver {
    override fun shallowDiff(
        compareA: ServiceOrder,
        compareB: ServiceOrder,
    ): HistoryChanges {
        val changes =
            buildMap {
                if (compareA.status != compareB.status) {
                    put(ServiceOrder::status.name, compareA.status.name)
                }

                if (compareA.complaint != compareB.complaint) {
                    put(ServiceOrder::complaint.name, compareA.complaint)
                }

                if (compareA.diagnosis != compareB.diagnosis) {
                    put(ServiceOrder::diagnosis.name, compareA.diagnosis ?: "")
                }

                if (compareA.operator != compareB.operator) {
                    put(ServiceOrder::operator.name, compareA.operator.id.toString())
                }

                if (compareA.tasks != compareB.tasks) {
                    put(ServiceOrder::tasks.name, simpleSerialize(compareA.tasks.map { it.id }))
                }

                if (compareA.inventoryItems != compareB.inventoryItems) {
                    put(
                        ServiceOrder::inventoryItems.name,
                        simpleSerialize(compareA.inventoryItems.map { it.id }),
                    )
                }
            }

        return HistoryChanges(compareA.id, changedAt = Clock.System.now(), changes)
    }
}
