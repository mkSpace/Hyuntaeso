package candidate

import component.Component
import component.Word
import dictionary.Dictionary
import hangeul.Hangeul
import hangeul.HangeulException

class CandidateSearcher(
    private val dictionary: Dictionary,
    private val originalText: String,
    private val eumso: String
) {
    private val candiList: MutableList<CandidateArray> = mutableListOf()

    fun search() {
        val one = dictionary.find(eumso)
        if(one.isNotEmpty()) {
            val candies = CandidateArray()
            candies.add(createCandidate(one))
            candiList.add(candies)
        }
        for (i in eumso.length - 1 downTo 0) {
            val head = eumso.substring(0, i)
            val tail = eumso.substring(i)
            val tails: List<Component> = dictionary.find(tail)
            val candies: MutableList<Candidate> = ArrayList()
            candies.add(createCandidate(tails))
            searchSubset(head, candies)
        }
    }

    private fun createCandidate(components: List<Component>): Candidate {
        return Candidate().apply {
            if (components.isNotEmpty()) this.componentList = components.toMutableList()
            this@apply.originalText = this@CandidateSearcher.originalText
        }
    }

    private fun searchSubset(eumso: String, candies: List<Candidate>) {
        for (i in eumso.length - 1 downTo 0) {
            val head = eumso.substring(0, i)
            val tail = eumso.substring(i)
            var tails: List<Component> = dictionary.find(tail)
            if (candies.isNotEmpty()) tails = filterComponentThatCanBefore(tails, candies[0].componentList)
            if (tails.isEmpty()) {
                if (i == 0) {
                    val combined: String
                    try {
                        combined = Hangeul.combineHangeulEumso(eumso)
                        val unknownWord = Word(
                            combined,
                            eumso,
                            combined,
                            Word.Pumsa.UNKNOWN.ko,
                            Word.Eun.UNKNOWN.ko
                        )
                        val newCandies = CandidateArray()
                        newCandies.addAll(candies)
                        val candi = createCandidate(emptyList())
                        candi.componentList.add(unknownWord)
                        newCandies.add(0, candi)
                    } catch (e: HangeulException) {
                        e.printStackTrace()
                    }
                    break
                }
                continue
            }

            val newCandies = CandidateArray()
            newCandies.addAll(candies)
            newCandies.add(0, createCandidate(tails))

            if (i > 0) searchSubset(head, newCandies) else candiList.add(newCandies)
        }
    }

    private fun filterComponentThatCanBefore(heads: List<Component>, tails: List<Component>): List<Component> {
        val filterd = mutableListOf<Component>()
        for (head in heads) {
            tails.forEach { tail ->
                if (dictionary.isWellContinued(head.getTypeName(), tail.getTypeName())) {
                    filterd.add(head)
                    return@forEach
                }
            }
        }
        return if (filterd.isEmpty()) emptyList()
        else filterd
    }

    fun getCandidateArrayList(): List<CandidateArray> = candiList
}