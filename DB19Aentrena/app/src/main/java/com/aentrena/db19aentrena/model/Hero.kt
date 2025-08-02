package com.aentrena.db19aentrena.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hero(
    val photo: String,
    val id: String,
    val name: String,
): Parcelable {
    /*
    fun isAlive()
    fun heal()
    fun recibeDamage()
     */
}
