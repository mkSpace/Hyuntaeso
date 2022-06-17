import candidate.CandidateSearcher
import dictionary.AssetDataDictionary
import excel.ExcelReader
import hangeul.Hangeul

fun main() {
    val dataList =
        ExcelReader.xlsToDataList("/Users/jaemin/Desktop/WorkSpace/etc/Hyuntaeso/src/main/resources/spam_data_test5.xlsx")
    val dictionary = AssetDataDictionary()
    val wordGroupList =
        dataList.groupBy { it.type }.map { it.key to it.value.map { it.subtractNouns(dictionary) }.flatten() }
    val spamList = wordGroupList.first().second.groupBy { it }.map { it.key to it.value.size }
    val hamList = wordGroupList.last().second.groupBy { it }.map { it.key to it.value.size }
    val hamCount = hamList.sumOf { it.second }
    val spamCount = spamList.sumOf { it.second }
    println("hamList: $hamList\nspamList: $spamList")
    println("hamCount: $hamCount, spamCount: $spamCount")
    val test = "오늘 저녁 같이 먹자 내일은 시간이 안돼 답장 빨리해줘 제발"
    val testWordList = mutableListOf<String>()
    test.split(" ").forEach { word ->
        val searcher = CandidateSearcher(dictionary, word, Hangeul.spreadHangeulString(word))
        searcher.search()
        val allCandidates = searcher.getCandidateArrayList().flatten()
            .map { it.componentList }
            .map { components -> components.filter { it.getTypeName() == "명사" && it.getSource().length > 1 } }
            .flatten()
            .map { it.getSource() }
        allCandidates.firstOrNull()?.let { testWordList.add(it) }
    }
    println("test word: $testWordList")
    var spamProbability = 1.00
    var hamProbability = 1.00
    testWordList.forEach { testWord ->
        println("HAM!! : ${hamList.find { it.first == testWord }}")
        println("SPAM!! : ${spamList.find { it.first == testWord }}")
        hamProbability *= (((hamList.find { it.first == testWord }?.second?.toDouble()
            ?: 0.005)) / hamCount.toDouble())
        spamProbability *= (((spamList.find { it.first == testWord }?.second?.toDouble()
            ?: 0.005)) / spamCount.toDouble())
    }
    println("spamProb : $spamProbability, hamProb: $hamProbability")
    println(if (spamProbability > hamProbability) "SPAM" else "HAM")
}