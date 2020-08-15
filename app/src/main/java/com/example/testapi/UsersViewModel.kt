package com.example.testapi

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UsersViewModel {

    lateinit var  model: List<UsersModel>

    /// Userの全件検索
    fun fetchAPI(result: (MutableList<String>?) -> Unit) {
        val httpAsync = "http://10.0.2.2/api/v1/users/"
            .httpGet()
            .responseString { r, s, result ->
                when (result) {
                    is Result.Failure -> {
                        val error = result.getException()
                        println("error: $error")

                        result(null)
                    }

                    is Result.Success -> {
                        val data = result.get()

                        val listType = object : TypeToken<List<UsersModel>>() {}.type
                        model = Gson().fromJson<List<UsersModel>>(data, listType)
                        println("userModel: $model")

                        var dataArray = mutableListOf<String>()

                        for (user in this.model) {
                            dataArray.add(user.name)
                        }
                        result(dataArray)
                    }
                }
            }
        httpAsync.join()
    }




    /// Userの追加
    fun post(name: String, text: String, callBack: (Boolean) -> Unit) {
        Fuel.post("http://10.0.2.2/api/v1/users/")
            .jsonBody("{\"name\":\"${name}\", \"text\":\"$}\"}")
            .response{ result ->
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