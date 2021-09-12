package com.example.newz

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request.Method.GET
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = NewzAdapter(this)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = null
        window.statusBarColor = this.resources.getColor(R.color.light_pink)
        supportActionBar?.setCustomView(R.layout.top)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#DB7D7D")))
        setContentView(R.layout.activity_main)

        scrolling_news.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        scrolling_news.adapter = adapter

        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=ac80584801114e8eae343d6fbbef1401"
        fetchNewz(url)

        refresh.setOnRefreshListener {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            scrolling_news.visibility = View.GONE
            categoriesView.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                scrolling_news.visibility = View.VISIBLE
                categoriesView.visibility = View.VISIBLE
            }, 1500)
            refresh.isRefreshing = false
            adapter.notifyDataSetChanged()
        }

    }

    private fun fetchNewz(url : String) {
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
        scrolling_news.visibility = View.GONE
        categoriesView.visibility = View.GONE
        val arrayList = ArrayList<NewzData>()
        val jsonObjectRequest = object : JsonObjectRequest(GET, url, null,
            {
                val jsonArray = it.getJSONArray("articles")
                for (articles in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(articles)
                    val newzData = NewzData(
                        jsonObject.getString("title"),
                        jsonObject.getString("url"),
                        jsonObject.getString("urlToImage")
                    )
                    arrayList.add(newzData)
                }
                adapter.updateNewz(arrayList)
                Handler(Looper.getMainLooper()).postDelayed({
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    scrolling_news.visibility = View.VISIBLE
                    categoriesView.visibility = View.VISIBLE
                }, 1500)
            },
            {
                Toast.makeText(this, "Couldn't load news", Toast.LENGTH_SHORT).show()
                shimmer.startShimmer()
                scrolling_news.visibility = View.GONE
                categoriesView.visibility = View.GONE
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun categories(view: View) {
        when(view.id){
            R.id.technology ->{
                val url = "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=ac80584801114e8eae343d6fbbef1401"
                fetchNewz(url)
            }
            R.id.business -> {
                val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=ac80584801114e8eae343d6fbbef1401"
                fetchNewz(url)
            }
            R.id.top_headlines ->{
                val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=ac80584801114e8eae343d6fbbef1401"
                fetchNewz(url)
            }
            R.id.health ->{
                val url = "https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=ac80584801114e8eae343d6fbbef1401"
                fetchNewz(url)
            }
            R.id.science ->{
                val url = "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=ac80584801114e8eae343d6fbbef1401"
                fetchNewz(url)
            }
            R.id.sports ->{
                val url = "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=ac80584801114e8eae343d6fbbef1401"
                fetchNewz(url)
            }
        }
    }


}