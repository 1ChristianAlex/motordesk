package com.khrix.domain.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseTryBlock<TOutData> {
    protected val logger: Logger? = LoggerFactory.getLogger(javaClass)

    suspend fun tryBlock(
        description: String,
        callback: suspend () -> TOutData,
    ): Result<TOutData> =
        try {
            logger?.info("Executing - $description")
            Result.success(callback())
        } catch (ex: Exception) {
            logger?.error(ex.message, ex.cause)
            Result.failure(ex)
        }
}

abstract class BaseUseCaseImpl<TInputData, TOutData> : BaseTryBlock<TOutData>() {
    suspend fun execute(command: TInputData): Result<TOutData> = tryBlock(useCaseDescription()) { internalExecute(command) }

    protected abstract suspend fun internalExecute(command: TInputData): TOutData

    protected abstract suspend fun useCaseDescription(): String
}

interface BaseUseCase<TInputData, TOutData> {
    suspend fun execute(command: TInputData): Result<TOutData>

    suspend fun internalExecute(command: TInputData): TOutData

    suspend fun useCaseDescription(): String
}
