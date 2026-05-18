package com.khrix.domain.core


abstract class BaseTryBlock<TOutData>(
) {
    private val className get() = this::class.simpleName ?: javaClass.simpleName

    suspend fun tryBlock(
        description: String,
        callback: suspend () -> TOutData,
    ): Result<TOutData> {
        return try {
            Result.success(callback())
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(Exception(description, ex))
        }
    }
}

abstract class BaseUseCaseImpl<TInputData, TOutData>() : BaseTryBlock<TOutData>() {
    suspend fun execute(command: TInputData): Result<TOutData> {
        return tryBlock(useCaseDescription()) { internalExecute(command) }
    }

    protected abstract suspend fun internalExecute(
        command: TInputData,
    ): TOutData

    protected abstract suspend fun useCaseDescription(): String
}

abstract class BaseUseCaseNoParamImpl<TOutData>() : BaseTryBlock<TOutData>() {
    suspend fun execute(): Result<TOutData> {
        return tryBlock(useCaseDescription()) { internalExecute() }
    }

    protected abstract suspend fun internalExecute(): TOutData

    protected abstract suspend fun useCaseDescription(): String
}

interface BaseUseCase<TInputData, TOutData> {
    suspend fun execute(
        command: TInputData
    ): Result<TOutData>

    suspend fun internalExecute(
        command: TInputData
    ): TOutData

    suspend fun useCaseDescription(): String
}

interface BaseUseCaseNoParam<TInputData, TOutData> : BaseUseCase<Unit, TOutData> {
    suspend fun execute(): Result<TOutData>
    suspend fun internalExecute(): TOutData
}