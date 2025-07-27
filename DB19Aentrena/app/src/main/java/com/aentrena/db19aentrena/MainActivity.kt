package com.aentrena.db19aentrena

import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        println("El log más básico")
        Log.v("MainActivity", "Soy log.v")
        Log.d("MainActivity", "Soy log.d")
        Log.w("MainActivity", "Soy log.w")
        Log.e("MainActivity", "Soy log.e")
        cargarDesdeSharedPreferences()

    }

    fun guardarEnSharedPreferences(user: User) {
        getSharedPreferences("MainActivity", MODE_PRIVATE).edit {
            putString(UserKey, user.toJson())
        }
    }

    fun cargarDesdeSharedPreferences() : User {
        val user = Gson().fromJson(
            getSharedPreferences("MainActivity", MODE_PRIVATE).getString(
                UserKey,
                ""
            ), User::class.java
        )
        if (user == null) return User()
        else return user
    }
}

private const val UserKey = "UserKey"

data class User(val name: String, val password: String = "") {
    fun toJson(): String = Gson().toJson(this)
}