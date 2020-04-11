package com.example.testapi

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = swipelayout
        floatingActionButton.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout?.setOnRefreshListener {
            fetchAPI()
            swipelayout.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()

        fetchAPI()
    }



    private fun fetchAPI() {

                val httpAsync = "http://10.0.2.2/api/v1/users/"
                    .httpGet()
                    .responseString { r, s, result ->
                        when (result) {
                            is Result.Failure -> {
                                val error = result.getException()
                                println("error: $error")
                            }
                            is Result.Success -> {
                                val data = result.get()

                                val listType = object : TypeToken<List<UsersModel>>() {}.type
                                val userModel = Gson().fromJson<List<UsersModel>>(data, listType)
                                println("userModel: $userModel")

                                var dataArray = mutableListOf<String>()

                                for (user in userModel) {
                                    dataArray.add(user.name)
                                }

                                val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, dataArray)
                                listView.adapter = adapter

                            }
                        }
                    }

                httpAsync.join()
            }
}
