package com.example.testapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private lateinit var userModel:List<UsersModel>

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

        listView.setOnItemClickListener { _, _, _, id ->
            val intent: Intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.putExtra("userid", userModel[id.toInt()].id)
            intent.putExtra("name", userModel[id.toInt()].name)
            intent.putExtra("text", userModel[id.toInt()].text)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        fetchAPI()
    }



    /// Userの全件検索
    private fun fetchAPI() {

                val httpAsync = "http://10.0.2.2/api/v1/users/"
                    .httpGet()
                    .responseString { r, s, result ->
                        when (result) {
                            is Result.Failure -> {
                                val error = result.getException()
                                println("error: $error")

                                listView.adapter = null

                                Toast.makeText(applicationContext,"リストの取得に失敗しました", Toast.LENGTH_LONG).show()
                            }
                            is Result.Success -> {
                                val data = result.get()

                                val listType = object : TypeToken<List<UsersModel>>() {}.type
                                userModel = Gson().fromJson<List<UsersModel>>(data, listType)
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
