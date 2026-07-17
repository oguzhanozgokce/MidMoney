package app.oguzhanozgokce.midmoney.network.error

import app.oguzhanozgokce.midmoney.error.AppError
import app.oguzhanozgokce.midmoney.error.ErrorMapper
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkErrorMapper @Inject constructor() : ErrorMapper {

    override fun map(throwable: Throwable): AppError? = when (throwable) {
        is SocketTimeoutException -> AppError.Timeout
        is UnknownHostException -> AppError.Network
        is HttpException -> if (throwable.code() == HTTP_UNAUTHORIZED) {
            AppError.Unauthorized
        } else {
            AppError.Server(throwable.code())
        }
        is IOException -> AppError.Network
        else -> null
    }

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
    }
}
