package com.noteapp.data.model

data class Note(
    val id: String? = null,
    val title: String = "",
    val desc: String = "",
    val color: Int = -1,
    val lastUpdated: Long? = System.currentTimeMillis()
)