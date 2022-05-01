package hangeul

class Hangeul {
    companion object {
        const val YongEon = "용언"
        const val ChaeEon = "체언"
        const val SooSikEon = "수식언"
        const val GwanGyeEon = "관계언"
        const val DokRipEon = "독립언"
        const val MyungSa = "명사"
        const val DaeMyungSa = "대명사"
        const val SuSa = "수사"
        const val DongSa = "동사"
        const val HyungYongSa = "형용사"
        const val GwanHyungSa = "관형사"
        const val BuSa = "부사"
        const val GamTanSa = "감탄사"
        const val JoSa = "조사"

        const val BASE_CODE = 0xAC00 // = 44032
        const val HANGEUL_END = 0xD79F

        const val CHOSUNG_CODE = 588
        const val JUNGSUNG_CODE = 28
        const val COMP_JAEUM_BEGIN = 0x3131
        const val COMP_JAEUM_END = 0x314E
        const val COMP_MOEUM_BEGIN = 0X314F
        const val COMP_MOEUM_END = 0X3163

        // 초성 리스트. 00 ~ 18
        val CHOSUNG_LIST = charArrayOf(
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
            'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )

        // 중성 리스트. 00 ~ 20
        val JUNGSUNG_LIST = charArrayOf(
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ',
            'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ',
            'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ',
            'ㅡ', 'ㅢ', 'ㅣ'
        )

        // 종성 리스트. 00 ~ 27 + 1(1개 없음)
        val JONGSUNG_LIST = charArrayOf(
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
            'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ',
            'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ',
            'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )

        fun getJongsungIndex(jongsung: Char): Int {
            for (i in JONGSUNG_LIST.indices) if (jongsung == JONGSUNG_LIST[i]) return i
            return -1
        }

        fun getChosungIndex(chosung: Char): Int {
            for (i in CHOSUNG_LIST.indices) if (chosung == CHOSUNG_LIST[i]) return i
            return -1
        }

        fun getJungsungIndex(jungsung: Char): Int {
            for (i in JUNGSUNG_LIST.indices) if (jungsung == JUNGSUNG_LIST[i]) return i
            return -1
        }

        fun isJaEum(ch: Char): Boolean {
            return ch.code in COMP_JAEUM_BEGIN..COMP_JAEUM_END
        }

        fun isMoEum(ch: Char): Boolean {
            return ch.code in COMP_MOEUM_BEGIN..COMP_MOEUM_END
        }

        fun isJamo(ch: Char): Boolean {
            return isJaEum(ch) || isMoEum(ch)
        }

        fun isHangeul(codePoint: Int): Boolean {
            return (codePoint in BASE_CODE..HANGEUL_END
                    || codePoint in COMP_JAEUM_BEGIN..COMP_MOEUM_END)
        }

        /**
         * @param hangeulText This parameter string must be composed by only Hangeul!
         * Other
         * @return
         */
        @Throws(HangeulException::class)
        fun toHangeulList(hangeulText: String): List<Hangeul>? {
            val hangeulList = ArrayList<Hangeul>(hangeulText.length)
            for (i in 0 until hangeulText.length) {
                val ch = hangeulText[i]
                hangeulList.add(Hangeul(ch))
            }
            return hangeulList
        }

        @Throws(HangeulException::class)
        fun spreadHangeulString(hangeulText: String): String {
            val builder = StringBuilder()
            for (i in 0 until hangeulText.length) {
                val ch = hangeulText[i]
                if (isHangeul(ch.code)) builder.append(Hangeul(ch).spread()) else builder.append(ch)
            }
            return builder.toString()
        }

        /**
         * 한글 음소를 결합함~
         * ㅇㅏㄶㄱㅡㄹㅐ -> 않그래
         * ㄴㅎ를 ㄶ으로 만들지는 않습니당.
         *
         * @param eumsos
         * @return
         * @throws HangeulException
         */
        @Throws(HangeulException::class)
        fun combineHangeulEumso(eumsos: String): String {
            // TODO: 구현안됨.
            val sb = StringBuilder()
            var cho = ' '
            var jung = ' '
            var jong = ' ' // 조합중인 초중종성
            var lastChar = 0.toChar() // 루프를 돌 때 이전의 글자
            var isLastCharJaeum = false // 마지막 글자가 자음임?
            for (i in 0 until eumsos.length) {
                val ch = eumsos[i]

                // ch 가 모음임
                if (isMoEum(ch)) {
                    // 이전 글자가 자음일 경우
                    if (isLastCharJaeum) {
                        // 종성으로 조합중일 경우에는
                        // 초성+중성을 합쳐서 빼고(sb에 추가!)
                        // 종성 -> 초성, ch는 모음으로 넣음
                        if (jong != ' ') {
                            //System.out.printf("한글의 탄생1!: %c + %c + %c\n", cho, jung, jong);
                            sb.append(Hangeul(cho, jung, ' ').toChar())
                            cho = jong
                            jung = ch
                            jong = ' '
                        } else {
                            jung = ch
                        }
                    } else if (lastChar.code == 0 || !isLastCharJaeum) {
                        if (cho != ' ' && jung != ' ') {
                            //System.out.printf("한글의 탄생2!: %c + %c + %c\n", cho, jung, jong);
                            sb.append(Hangeul(cho, jung, ' ').toChar())
                        }

                        //System.out.printf("한글의 탄생3!: %c + %c + %c\n", cho, jung, jong);
                        sb.append(ch)
                        jung = ' '
                        cho = jung
                    }
                    isLastCharJaeum = false
                } else if (isJaEum(ch)) {
                    // 이전 글자가 자음
                    // 이 경우는 아래 2가지
                    // 1. 초성만 완성된 상태에서 또 자음이 나옴(ex: ㅋㅋㅋ)
                    // 2. 종성까지 완벽히 구현된 뒤 자음이 나옴(ex: 안ㄴ)
                    if (isLastCharJaeum) {
                        // 2번의 경우
                        if (cho != ' ' && jung != ' ' && jong != ' ') {
                            //System.out.printf("한글의 탄생4!: %c + %c + %c\n", cho, jung, jong);
                            sb.append(Hangeul(cho, jung, jong).toChar())
                            cho = ch
                            jong = ' '
                            jung = jong
                        } else {
                            //System.out.printf("한글의 탄생5!: %c + %c + %c\n", cho, jung, jong);
                            if (cho != ' ') sb.append(cho)
                            cho = ch
                            jong = ' '
                            jung = jong
                        }
                    } else {
                        // 2번의 경우
                        if (cho != ' ' && jung != ' ') {
                            jong = ch
                        } else {
                            if (lastChar.code == 0) {
                                cho = ch
                                jong = ' '
                                jung = jong
                            } else {
                                //System.out.printf("한글의 탄생6!: %c + %c + %c\n", cho, jung, jong);
                                if (cho != ' ') sb.append(cho)
                                cho = ch
                                jong = ' '
                                jung = jong
                            }
                        }
                    }
                    isLastCharJaeum = true
                } else  // 한글아님.
                {
                    if (cho != ' ') {
                        sb.append(Hangeul(cho, jung, jong).toChar())
                        jong = ' '
                        jung = jong
                        cho = jung
                    }
                    sb.append(ch)
                    isLastCharJaeum = false
                }
                lastChar = ch
            }
            if (cho != ' ') {
                //System.out.printf("최후의 한글~ %c + %c + %c\n", cho, jung, jong);
                sb.append(Hangeul(cho, jung, jong).toChar())
            }
            return sb.toString()
        }

        fun HangeulListToString(hangeulList: List<Hangeul>): String? {
            val builder = StringBuilder(hangeulList.size)
            return builder.toString()
        }

        @Throws(HangeulException::class)
        fun cutJamo(hlist: List<Hangeul>, end: Int): String? {
            var jamoCount = 0
            for (i in hlist.indices) {
                val h = hlist[i]
                jamoCount += h.getJamoCount()
                if (jamoCount >= end) {
                    val sb = StringBuilder()
                    for (j in 0 until i) {
                        sb.append(hlist[j].toChar())
                    }
                    val over = jamoCount - end
                    if (over == 0) sb.append(h.toChar())
                    if (over == 1) sb.append(Hangeul(h.chosung, h.jungsung, ' ').toChar())
                    if (over == 2) sb.append(h.chosung)
                    return sb.toString()
                }
            }
            return HangeulListToString(hlist)
        }
    }

    private var chosung = ' '
    private var jungsung = ' '
    private var jongsung = ' '

    @Throws(HangeulException::class)
    constructor(hangeulCharacter: Char) {
        val code = hangeulCharacter.code


        if (code in COMP_JAEUM_BEGIN..COMP_JAEUM_END) {
            chosung = hangeulCharacter
            return
        }
        if (code in COMP_MOEUM_BEGIN..COMP_MOEUM_END) {
            jungsung = hangeulCharacter
            return
        }

        if (!Hangeul.isHangeul(hangeulCharacter.code)) throw NotHangeulException(hangeulCharacter)

        val base = hangeulCharacter.code - BASE_CODE
        val c1 = (base / CHOSUNG_CODE).toChar().code
        val c2 = ((base - CHOSUNG_CODE * c1) / JUNGSUNG_CODE).toChar().code
        val c3 = (base - CHOSUNG_CODE * c1 - JUNGSUNG_CODE * c2).toChar().code

        chosung = CHOSUNG_LIST[c1]
        jungsung = JUNGSUNG_LIST[c2]
        jongsung = JONGSUNG_LIST[c3]
    }

    @Throws(HangeulException::class)
    constructor(chosung: Char, jungsung: Char, jongsung: Char) {
        if (dbg(chosung != ' ' && !Hangeul.isJaEum(chosung), "초성이상") ||
            dbg(jungsung != ' ' && !Hangeul.isMoEum(jungsung), "중성이상") ||
            dbg(jongsung != ' ' && !Hangeul.isJaEum(jongsung), "종성이상")
        ) throwHE(chosung, jungsung, jongsung)
        this.chosung = chosung
        this.jungsung = jungsung
        this.jongsung = jongsung
    }

    fun getChosung(): Char {
        return chosung
    }

    fun getJungsung(): Char {
        return jungsung
    }

    fun getJongsung(): Char {
        return jongsung
    }

    private fun dbg(b: Boolean, t: String): Boolean {
        if (b) println(t)
        return b
    }

    @Throws(NotHangeulException::class)
    private fun throwHE(c1: Char, c2: Char, c3: Char) {
        throw NotHangeulException(
            String.format(
                "Could not compose the Hangeul with '%c' '%c' '%c'",
                c1, c2, c3
            )
        )
    }

    fun isStrange(): Boolean {
        return isHangeul(chosung.code) ||
                isHangeul(jungsung.code) ||
                isHangeul(jongsung.code)
    }

    fun toChar(): Char {
        val c1 = getChosungIndex(chosung)
        val c2 = getJungsungIndex(jungsung)
        val c3 = getJongsungIndex(jongsung)
        if (c2 == -1 || c3 == -1) {
            return chosung
        }
        return if (c1 * c2 * c3 == -1) 0.toChar() else (BASE_CODE + c3 + c2 * JUNGSUNG_CODE + c1 * CHOSUNG_CODE).toChar()
    }

    fun getJamoCount(): Int {
        var count = 0
        if (chosung != ' ') ++count
        if (jungsung != ' ') ++count
        if (jongsung != ' ') ++count
        return count
    }

    fun toChars(): CharArray? {
        return charArrayOf(
            chosung,
            jungsung,
            jongsung
        )
    }

    fun spread(): String? {
        val builder = StringBuilder()
        if (chosung != ' ') builder.append(chosung)
        if (jungsung != ' ') builder.append(jungsung)
        if (jongsung != ' ') builder.append(jongsung)
        return builder.toString()
    }
}