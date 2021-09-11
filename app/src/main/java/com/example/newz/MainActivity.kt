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

        fetchNewz()
        scrolling_news.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        scrolling_news.adapter = adapter

        refresh.setOnRefreshListener {
            shimmer.startShimmer()
            scrolling_news.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                shimmer.stopShimmer()
                scrolling_news.visibility = View.VISIBLE
            },1500)
            fetchNewz()
            refresh.isRefreshing = false
            adapter.notifyDataSetChanged()
        }
    }

    private fun fetchNewz() {
        val url =
            "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=ac80584801114e8eae343d6fbbef1401"
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
                    scrolling_news.visibility = View.VISIBLE
                },1500)
            },
            {
                shimmer.startShimmer()
                scrolling_news.visibility = View.GONE
                Toast.makeText(this, "Couldn't load news", Toast.LENGTH_SHORT).show() })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}