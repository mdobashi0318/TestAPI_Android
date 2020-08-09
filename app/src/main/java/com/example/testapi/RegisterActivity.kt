package com.example.testapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class RegisterActivity : AppCompatActivity() {


    private var userid: String? = null

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        userid = intent.getStringExtra("userid")

        if(userid != "") {
            nameTextField.setText(intent.getStringExtra("name").toString())
            textField.setText(intent.getStringExtra("text").toString())
        }

        button.setOnClickListener {


            if (nameTextField.text.isEmpty()) {
                showAlert("名前が入力されていません", false)
                return@setOnClickListener
            }
            if (textField.text.isEmpty()) {
                showAlert("テキストが入力されていません", false)
                return@setOnClickListener
            }

            if(userid == "") {
                post()
            } else {
                updateText()
            }
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
                        Toast.makeText(applicationContext,"登録に失敗しました", Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        val data = result.get()

                        println("result: $data")
                        Toast.makeText(applicationContext,"登録しました", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

            }
    }


    /// Userの更新
    private fun updateText() {
        Fuel.put("http://10.0.2.2/api/v1/users/${userid}")
            .jsonBody("{\"name\":\"${nameTextField.text}\", \"text\":\"${textField.text}\"}")
            .response{ result ->

                when (result) {
                    is Result.Failure -> {
                        val error = result.getException()
                        println("error: $error")
                        Toast.makeText(applicationContext,"更新に失敗しました", Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        val data = result.get()

                        println("result: $data")
                        Toast.makeText(applicationContext,"更新しました", Toast.LENGTH_LONG).show()
                        finish()
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
