package mx.edu.utng.oic.doloreshidalgoturismo.utils

import android.util.Log

/**
 * Utilidad para logging consistente en toda la aplicación
 */
object Logger {
    private const val TAG = "DoloreshidalgoTurismo"

    /** Log de depuración (Debug) */
    fun d(message: String) {
        Log.d(TAG, message)
    }

    /** Log de error (Error) */
    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }

    /** Log de información (Info) */
    fun i(message: String) {
        Log.i(TAG, message)
    }

    /** Log de advertencia (Warning) */
    fun w(message: String) {
        Log.w(TAG, message)
    }
}