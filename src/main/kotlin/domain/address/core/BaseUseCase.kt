package com.khrix.domain.address.core


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

abstract class BaseUseCase<TInputData, TOutData>() : BaseTryBlock<TOutData>() {
    suspend fun execute(inputData: TInputData): Result<TOutData> {
        return tryBlock(useCaseDescription()) { internalExecute(inputData) }
    }

    protected abstract suspend fun internalExecute(
        inputData: TInputData,
    ): TOutData

    protected abstract suspend fun useCaseDescription(): String
}

abstract class BaseUseCaseNoParam<TOutData>() : BaseTryBlock<TOutData>() {
    suspend fun execute(): Result<TOutData> {
        return tryBlock(useCaseDescription()) { internalExecute() }
    }

    protected abstract suspend fun internalExecute(): TOutData

    protected abstract suspend fun useCaseDescription(): String
}
