import candidate.CandidateSearcher
import dictionary.AssetDataDictionary
import hangeul.Hangeul

fun main() {
    val sample = "안녕하세요 제 이름은 박재민 입니다. 우리 모두 사이 좋게 지내요!".let {
        it.replace(".", "")
            .replace("!", "")
            .replace("?", "")
    }
    println(sample)
    println(Hangeul.spreadHangeulString(sample))
    val dictionary = AssetDataDictionary()
    sample.split(" ").forEach {
        val searcher = CandidateSearcher(dictionary, it, Hangeul.spreadHangeulString(it))
        searcher.search()
        println(searcher.getCandidateArrayList().filter { it.isNotEmpty() })
    }
}