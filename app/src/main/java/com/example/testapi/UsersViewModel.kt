package com.example.testapi

import android.content.Context
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result

class UsersViewModel {


    var model: UsersModel = UsersModel()

    /// Userの更新

    fun updateText(id: String, name: String, text: String, callBack: (Boolean) -> Unit) {
        Fuel.put("http://10.0.2.2/api/v1/users/${id}")
            .jsonBody("{\"name\":\"${name}\", \"text\":\"${text}\"}")
            .response { result ->
                when (result) {
                    is Result.Failure -> {
                        val error = result.getException()
                        println("error: $error")
                        callBack(false)

                    }
                    is Result.Success -> {
                        val data = result.get()
                        println("result: $data")
                        callBack(true)

                    }
                }
            }
    }
}