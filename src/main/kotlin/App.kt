import candidate.CandidateSearcher
import dictionary.AssetDataDictionary
import hangeul.Hangeul

fun main() {
    val sample = "안녕하세요"
    val dictionary = AssetDataDictionary()
    val searcher = CandidateSearcher(dictionary, sample, Hangeul.spreadHangeulString(sample))
    searcher.search()
    println(searcher.candidateArrayList)
}