package com.osenov.trades.data

import com.google.common.util.concurrent.RateLimiter
import okhttp3.Interceptor
import okhttp3.Response

class TimeoutInterceptor : Interceptor {

    private val limiter = RateLimiter.create(1.0)

    override fun intercept(chain: Interceptor.Chain): Response {
        limiter.acquire()
        return chain.proceed(chain.request())
    }
}