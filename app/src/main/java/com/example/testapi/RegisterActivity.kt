package com.example.testapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        button.setOnClickListener {


            if (nameTextField.text.isEmpty()) {
                showAlert("名前が入力されていません", false)
                return@setOnClickListener
            }
            if (textField.text.isEmpty()) {
                showAlert("テキストが入力されていません", false)
                return@setOnClickListener
            }

            post()
            showAlert("登録しました", true)
        }
    }



    private fun post() {
        Fuel.post("http://10.0.2.2/api/v1/users/")
            .jsonBody("{\"name\":\"${nameTextField.text}\", \"text\":\"${textField.text}\"}")
            .response{ result ->

                when (result) {
                    is Result.Failure -> {
                        val error = result.getException()
                        println("error: $error")
                    }
                    is Result.Success -> {
                        val data = result.get()

                        println("result: $data")
                    }
                }

            }
    }

    /// yesButtonを押した時に画面を閉じるアラート
    private fun showAlert(message: String, isFinish: Boolean) {
        alert(message) {
                yesButton {
                    if (isFinish) {
                    finish()
                }
            }
        }.show()
    }
}