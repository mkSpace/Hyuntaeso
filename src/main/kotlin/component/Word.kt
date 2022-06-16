package component

class Word(
    private val source: String,
    private val eumso: String,
    private val root: String,
    private val pumsa: String,
    private val eon: String
) : Component {

    enum class Pumsa(val ko: String) {
        MYUNGSA("명사"), DAEMYUNGSA("대명사"), SUSA("수사"), DONGSA("동사"), HYUNGYONGSA("형용사"),
        BUSA("부사"), GWANHYUNGSA("관형사"), JOSA("조사"), GAMTANSA("감탄사"), UNKNOWN("알수없는품사")
    }

    enum class Eun(val ko: String) {
        CHAEEON("체언"), YONGEON("용언"), SUSIKEON("수식언"), GWANGYEEON("관계언"),
        DOKRIPEON("독립언"), UNKNOWN("알수없는언")
    }

    override fun getTypeName(): String = pumsa

    override fun getSource(): String = source

    fun getEumso(): String = eumso

    fun getEon(): String = eon

    fun getPumsa(): String = pumsa

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Word

        if (source != other.source) return false
        if (eumso != other.eumso) return false
        if (root != other.root) return false
        if (pumsa != other.pumsa) return false
        if (eon != other.eon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + eumso.hashCode()
        result = 31 * result + root.hashCode()
        result = 31 * result + pumsa.hashCode()
        result = 31 * result + eon.hashCode()
        return result
    }

    override fun toString(): String {
        return "Word(source='$source', eumso='$eumso', root='$root', pumsa='$pumsa', eon='$eon')"
    }


}