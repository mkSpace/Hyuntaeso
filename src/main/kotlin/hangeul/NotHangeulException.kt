package hangeul

class NotHangeulException @JvmOverloads constructor(
    detailMessage: String? = null,
    throwable: Throwable? = null
) : HangeulException(detailMessage, throwable) {
    constructor(ch: Char) : this("$ch 는 한글이 아닙니다.")
}