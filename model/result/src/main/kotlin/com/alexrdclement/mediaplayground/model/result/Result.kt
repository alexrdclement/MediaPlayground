package com.alexrdclement.mediaplayground.model.result

sealed class Result<Success, Failure> {
    data class Success<Success, Failure>(val value: Success) : Result<Success, Failure>()
    data class Failure<Success, Failure>(val failure: Failure) : Result<Success, Failure>()
}

fun <Success, Failure> Result<Success, Failure>.successOrElse(
    onFailure: (Failure) -> Success
): Success {
    return when (this) {
        is Result.Success -> this.value
        is Result.Failure -> onFailure(this.failure)
    }
}

fun <Success> Result<Success, *>.successOrDefault(
    default: Success
): Success {
    return this.successOrElse { default }
}

inline infix fun <Success, Failure> Result<Success, Failure>.guardSuccess(
    block: (Failure) -> Nothing
): Success {
    return when (this) {
        is Result.Success -> this.value
        is Result.Failure -> block(this.failure)
    }
}

fun <Success, Failure, R> Result<Success, Failure>.fold(
    onSuccess: (Success) -> R,
    onFailure: (Failure) -> R,
): R = when (this) {
    is Result.Failure -> onFailure(this.failure)
    is Result.Success -> onSuccess(this.value)
}

inline infix fun <Success, Failure, R> Result<Success, Failure>.map(
    crossinline block: (Success) -> R
): Result<R, Failure> {
    return fold(
        onSuccess = { Result.Success(block(it)) },
        onFailure = { Result.Failure(it) }
    )
}

inline infix fun <Success, Failure, R> Result<Success, Failure>.mapFailure(
    crossinline block: (Failure) -> R
): Result<Success, R> {
    return fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Failure(block(it)) }
    )
}

suspend inline fun <Success, Failure, reified T : Throwable> runCatchingResult(
    onException: (T) -> Failure,
    crossinline block: suspend () -> Result<Success, Failure>,
): Result<Success, Failure> {
    return try {
        block()
    } catch (t: Throwable) {
        if (t !is T) throw t
        Result.Failure(onException(t))
    }
}
