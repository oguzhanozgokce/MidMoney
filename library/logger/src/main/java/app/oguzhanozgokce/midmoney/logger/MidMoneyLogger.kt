package app.oguzhanozgokce.midmoney.logger

import android.util.Log

object MidMoneyLogger {
    private const val DEFAULT_TAG = "MidMoney"

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.d(tag, message)
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.i(tag, message)
    }

    fun w(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.w(tag, message)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.e(tag, message, throwable)
    }
}
