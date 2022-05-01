package hangeul

import java.lang.Exception

open class HangeulException : Exception {
    constructor()

    constructor(message: String?) : super(message)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String?, cause: Throwable?) : super(
        message,
        cause
    )
}