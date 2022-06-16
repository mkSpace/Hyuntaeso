import candidate.CandidateSearcher
import dictionary.AssetDataDictionary
import hangeul.Hangeul

fun main() {
    val sample = "안녕하세요 제 이름은 박재민 입니다. 우리 모두 사이 좋게 지내요! 2.참 고 : 어떻게 1인 무점포 창업 으로 수익이 발생하는지 설명회 당일 확실히 보여드리겠습니다. 3.날 짜 : 11 월 16일 금요일 오후 2 시 ~ 또는 29일 목요일 오후 2시 중 선택   [자세히 보기]"
        .replace(".", "")
        .replace("!", "")
        .replace("?", "")
    println(sample)
    println(Hangeul.spreadHangeulString(sample))
    val dictionary = AssetDataDictionary()
    val wordList = mutableListOf<String>()
    sample.split(" ").forEach { word ->
        val searcher = CandidateSearcher(dictionary, word, Hangeul.spreadHangeulString(word))
        searcher.search()
        val allCandidates = searcher.getCandidateArrayList().flatten()
            .map { it.componentList }
            .map { components -> components.filter { it.getTypeName() == "명사" } }
            .flatten()
            .map { it.getSource() }
        allCandidates.firstOrNull()?.let { wordList.add(it) }
    }
    println(wordList)
}