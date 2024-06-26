package ernest.zamelczyk.koleotask.api

import okhttp3.Interceptor
import okhttp3.Response

class KoleoHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header("X-KOLEO-Version", "1").build()
        )
    }
}