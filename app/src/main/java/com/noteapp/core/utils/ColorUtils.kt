package com.noteapp.core.utils

import com.noteapp.R

object ColorUtils {
    fun getHexString(color: Int) : String {
        return String.format("#%06X", color.toString(16))
    }


}