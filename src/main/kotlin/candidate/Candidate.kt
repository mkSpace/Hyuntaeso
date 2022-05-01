package candidate

import component.Component

class Candidate {

    var componentList: MutableList<Component> = mutableListOf()
    var originalText: String? = null
    var text: String? = null

    override fun toString(): String {
        if (componentList.isEmpty()) return "(empty)"
        return "${componentList.first().getSource()}(${componentList.map { it.getTypeName() }.joinToString(",")})"
    }
}