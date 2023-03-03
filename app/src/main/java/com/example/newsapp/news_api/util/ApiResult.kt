package com.example.newsapp.news_api.util

import retrofit2.HttpException
import retrofit2.Response

enum class NewsApiStatus {
    LOADING,
    ERROR,
    SUCCESS,
    EMPTY
}

sealed class ApiResult<out T>(
    val status: NewsApiStatus,
    val data: T? = null,
    val message: String? = null
) {

    class Success<out R>(val _data: R?): ApiResult<R>(
        status = NewsApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    class Error(exception: String) : ApiResult<Nothing>(
        status = NewsApiStatus.ERROR,
        data = null,
        message = exception
    )

    class Loading<out R>: ApiResult<R>(NewsApiStatus.LOADING)
    class Empty : ApiResult<Nothing>(NewsApiStatus.EMPTY)

}

suspend fun <T: Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.Error(response.message())
        }
    } catch (e: HttpException) {
        ApiResult.Error(e.message())
    } catch (e: Throwable) {
        ApiResult.Error(e.toString())
    }
}