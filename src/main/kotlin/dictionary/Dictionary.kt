package dictionary

import component.Component
import component.HyungTaeSo
import component.Word

interface Dictionary {
    fun find(eumsos: String?): List<Component>

    fun findWord(eumsos: String?): List<Word>

    fun findHyungTaeSo(eumsos: String?): List<HyungTaeSo>

    fun findHyungTaeSoByType(eumsos: String?, allowedTypeSet: MutableSet<String>): List<HyungTaeSo>

    fun findWordByPumsa(eumsos: String?, allowedPumsaSet: Set<String>): List<Word>

    fun findWordByEon(eumsos: String?, allowedEonSet: Set<String>): List<Word>

    fun isWellContinued(headType: String?, tailType: String?): Boolean
}