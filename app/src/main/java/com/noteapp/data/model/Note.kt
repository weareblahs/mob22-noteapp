package com.noteapp.data.model

data class Note(
    val id: String? = null,
    val title: String = "",
    val desc: String = "",
    val color: String = "#FFFFFF",
    val lastUpdated: Long? = System.currentTimeMillis()
){
    fun toHashMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "desc" to desc,
            "lastUpdated" to lastUpdated,
        )
    }
}
