package com.example.testapi

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var userViewModel = UsersViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = swipelayout
        floatingActionButton.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.putExtra("userid", "")
            startActivity(intent)
        }

        swipeRefreshLayout?.setOnRefreshListener {
            fetchModel()
            swipelayout.isRefreshing = false
        }

        listView.setOnItemClickListener { _, _, _, id ->
            val intent: Intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.putExtra("userid", userViewModel.model[id.toInt()].id)
            intent.putExtra("name", userViewModel.model[id.toInt()].name)
            intent.putExtra("text", userViewModel.model[id.toInt()].text)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        fetchModel()
    }



    /// Userを全検索
    private fun fetchModel() {
        userViewModel.fetchAPI { result ->
            if(result != null) {
                val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, result)

                listView.adapter = adapter

            } else {
                listView.adapter = null
                Toast.makeText(applicationContext,"リストの取得に失敗しました", Toast.LENGTH_LONG).show()
            }
        }
    }




}
