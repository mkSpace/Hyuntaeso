package component

class Mistake(val reason: String, val type: String) {
    companion object {
        const val MISTAKE_JOSA_JONGSUNG = "mis_eun_neun"
        const val MISTAKE_ANH_AN = "mis_an_anh"
        const val MISTAKE_DOE_DWAE = "mis_doe_dwae"
        const val WRONG_CONTINUED = "err_wrong_contd"
        const val WRONG_NO_ROOT = "err_no_root"
    }

    private var confuses: MutableList<String> = mutableListOf()
    private var isWrong = true

    fun setConfuses(vararg cases: String) {
        println("Mistake $type is WRONG!!!!!!!!!!!!!!\n\n")
        isWrong = false
        confuses = ArrayList()
        for (c in cases) confuses.add(c)
    }
}