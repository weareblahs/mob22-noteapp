package com.noteapp.data.model

data class Note(
    val id: String? = null,
    val title: String = "",
    val desc: String = "",
    val color: Int = -1,
    val lastUpdated: Long? = System.currentTimeMillis()
) {
    fun toHashMap(): Map<String,Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "desc" to desc,
            "color" to color,
            "lastUpdated" to lastUpdated
        )
    }
}
