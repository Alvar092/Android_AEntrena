package com.aentrena.db19aentrena.login

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.aentrena.db19aentrena.User
import com.aentrena.db19aentrena.databinding.ActivityLoginBinding
import com.aentrena.db19aentrena.game.GameActivity
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        println("El log más básico")
        Log.v("MainActivity", "Soy log.v")
        Log.d("MainActivity", "Soy log.d")
        Log.w("MainActivity", "Soy log.w")
        Log.e("MainActivity", "Soy log.e")

        val user = cargarDesdeSharedPreferences()

        with(binding) {
            etUser.setText(user.name)
            etPassword.setText(user.password)
            bLogin.setOnClickListener {
                onLoginClicked()
            }
            etUser.doAfterTextChanged {
                setButtonState()
            }
            etPassword.doAfterTextChanged {
                setButtonState()
            }
            setButtonState()
        }
    }

    private fun setButtonState() {
        with(binding) {
            bLogin.isEnabled =
                etPassword.text.toString().isNotBlank() && etUser.text.toString().isNotBlank()
        }
    }

    private fun onLoginClicked() {
        with(binding) {
            val user = User(etUser.text.toString(), etPassword.text.toString())
            msRememberUser?.isChecked.let {
                if (it == true) {
                    guardarEnSharedPreferences(user)
                } else {
                    guardarEnSharedPreferences(User())
                }
            }
        }
        GameActivity.startActivity(this)
    }

    fun guardarEnSharedPreferences(user: User) {
        getSharedPreferences("MainActivity", MODE_PRIVATE).edit {
            putString(UserKey, user.toJson())
        }
    }

    fun cargarDesdeSharedPreferences(): User {
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