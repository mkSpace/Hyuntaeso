package component

class HyungTaeSo(
    private val source: String,
    private val eumso: String,
    private val type: String
) : Component {
    enum class Type(val ko: String) {
        ROOT("어근"), STEM("어간"), TAIL("어미"), AFFIX("접사"), SUFFIX("접미사"), PREFIX("접두사"), JOSA("조사")
    }

    override fun getTypeName(): String = type

    override fun getSource(): String = source

    fun getEumso(): String = eumso

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HyungTaeSo

        if (source != other.source) return false
        if (eumso != other.eumso) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + eumso.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}