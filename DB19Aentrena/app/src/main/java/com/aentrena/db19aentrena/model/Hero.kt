package com.aentrena.db19aentrena.model

import android.os.Parcelable
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import kotlin.random.Random


data class Hero(
    val photo: String,
    val id: String,
    val name: String,
    var currentHealth: Int = 100
) {

    fun isAlive(): Boolean {
        return currentHealth > 0
    }
    fun heal(amount: Int = 20): Int {
        currentHealth += amount
        return currentHealth
    }
    fun reciveDamage(amount: Int = Random.nextInt(10, 61)): Int {
        val damage = Random.nextInt(10, 61)
        currentHealth -= damage
        return currentHealth
    }
}
