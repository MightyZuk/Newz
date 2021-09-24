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

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = null
        window.statusBarColor = this.resources.getColor(R.color.light_pink)
        supportActionBar?.setCustomView(R.layout.top)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#DB7D7D")))
        setContentView(R.layout.activity_main)

        scrolling_news.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

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

        category.setOnCheckedChangeListener { group, _ ->
            when(group.checkedChipId){
                R.id.tech ->{
                    val tech = "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=ac80584801114e8eae343d6fbbef1401"
                    fetchNewz(tech)
                }
                R.id.business -> {
                    val business = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=ac80584801114e8eae343d6fbbef1401"
                    fetchNewz(business)
                }
                R.id.top_headlines ->{
                    val top = "https://newsapi.org/v2/top-headlines?country=in&apiKey=ac80584801114e8eae343d6fbbef1401"
                    fetchNewz(top)
                }
                R.id.health ->{
                    val health = "https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=ac80584801114e8eae343d6fbbef1401"
                    fetchNewz(health)
                }
                R.id.science ->{
                    val science = "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=ac80584801114e8eae343d6fbbef1401"
                    fetchNewz(science)
                }
                R.id.sports ->{
                    val sports = "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=ac80584801114e8eae343d6fbbef1401"
                    fetchNewz(sports)
                }
            }
        }
    }

    private fun fetchNewz(url : String) {
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
        scrolling_news.visibility = View.GONE
        categoriesView.visibility = View.GONE
        scrolling_news.adapter = adapter
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

}