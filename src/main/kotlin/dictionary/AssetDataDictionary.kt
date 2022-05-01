package dictionary

import component.Component
import component.HyungTaeSo
import component.Word
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class AssetDataDictionary : Dictionary {

    private val wordTable: MutableMap<String, List<Word>> = TreeMap()
    private val htsTable: MutableMap<String, List<HyungTaeSo>> = TreeMap()
    private val sequenceSet: MutableSet<String> = TreeSet()

    init {
        readAllLines("dic.txt").forEach { line ->
            if (line.startsWith("#")) return@forEach

            val tokens = separateLine(line)
            val word = createWord(tokens)

            var words = wordTable[word.getEumso()]?.toMutableList()
            if (words == null) {
                words = mutableListOf(word)
                wordTable[word.getEumso()] = words
            } else {
                words.add(word)
            }
        }

        readAllLines("hts.txt").forEach { line ->
            if (line.startsWith("#")) return@forEach

            val tokens = separateLine(line)
            val hts = createHTS(tokens)

            var htss = htsTable[hts.getEumso()]?.toMutableList()
            if (htss == null) {
                htss = mutableListOf(hts)
                htss.add(hts)
                htsTable[hts.getEumso()] = htss
            } else {
                htss.add(hts)
            }
        }

        readAllLines("seq.txt").forEach { line ->
            sequenceSet.add(line)
        }
    }

    override fun find(eumsos: String?): List<Component> {
        eumsos ?: return emptyList()
        val components = mutableListOf<Component>()

        val words = findWord(eumsos)
        if (words.isNotEmpty()) {
            components.addAll(words)
        }

        val htss = findHyungTaeSo(eumsos)
        if (htss.isNotEmpty()) {
            components.addAll(htss)
        }

        return if (components.isEmpty()) emptyList()
        else components
    }

    override fun findWord(eumsos: String?): List<Word> =
        if (eumsos == null) emptyList() else wordTable[eumsos] ?: emptyList()

    override fun findHyungTaeSo(eumsos: String?): List<HyungTaeSo> =
        if (eumsos == null) emptyList() else htsTable[eumsos] ?: emptyList()

    override fun findHyungTaeSoByType(eumsos: String?, allowedTypeSet: MutableSet<String>): List<HyungTaeSo> {
        eumsos ?: return emptyList()

        if (allowedTypeSet.contains(HyungTaeSo.Type.AFFIX.ko)) {
            allowedTypeSet.add(HyungTaeSo.Type.PREFIX.ko)
            allowedTypeSet.add(HyungTaeSo.Type.SUFFIX.ko)
        }

        return htsTable[eumsos]?.filter { hts -> allowedTypeSet.contains(hts.getTypeName()) } ?: emptyList()
    }

    override fun findWordByPumsa(eumsos: String?, allowedPumsaSet: Set<String>): List<Word> {
        eumsos ?: return emptyList()

        return wordTable[eumsos]?.filter { word -> allowedPumsaSet.contains(word.getPumsa()) } ?: emptyList()
    }

    override fun findWordByEon(eumsos: String?, allowedEonSet: Set<String>): List<Word> {
        eumsos ?: return emptyList()
        return wordTable[eumsos]?.filter { word -> allowedEonSet.contains(word.getEon()) } ?: emptyList()
    }

    override fun isWellContinued(headType: String?, tailType: String?): Boolean {
        headType ?: tailType ?: return false
        return sequenceSet.contains("$headType+$tailType")
    }

    private fun readAllLines(assetName: String): List<String> {
        val list = mutableListOf<String>()
        try {
            val reader = BufferedReader(
                FileReader("D:\\Develope\\etc\\hangeul\\src\\main\\resources\\$assetName")
            )
            var line: String? = reader.readLine()
            while (line != null) {
                list.add(line)
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return list
    }

    private fun separateLine(line: String): List<String> {
        val list = mutableListOf<String>()
        val tokenizer = StringTokenizer(line, ",")
        while (tokenizer.hasMoreTokens()) list.add(tokenizer.nextToken())
        return list
    }

    private fun createWord(items: List<String>): Word =
        Word(source = items[0], root = items[1], eumso = items[2], pumsa = items[3], eon = items[4])

    private fun createHTS(items: List<String>): HyungTaeSo =
        HyungTaeSo(source = items[0], eumso = items[1], type = items[2])
}