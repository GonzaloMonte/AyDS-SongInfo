package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : Activity() {
    private lateinit var textPane1: TextView
    private lateinit var database: ArticleDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane1 = findViewById(R.id.textPane1)
        database = ArticleDatabase(this)
        open(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun open(artist: String?) {
        Thread {
            database.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            println("Article by test: ${database.ArticleDao().getArticleByArtistName("test")}")
            println("Article by nada: ${database.ArticleDao().getArticleByArtistName("nada")}")
        }.start()
        getArtistInfo(artist)
    }

    private fun getArtistInfo(artistName: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        try {
            val article = database.ArticleDao().getArticleByArtistName(artistName!!)
            if (article != null) {
                displayArticle(article)
            } else {
                val response = lastFMAPI.getArtistInfo(artistName).execute()
                if (response.isSuccessful) {
                    val articleEntity = parseArticle(response.body())
                    if (articleEntity != null) {
                        saveArticle(articleEntity)
                        displayArticle(articleEntity)
                    } else {
                        runOnUiThread { textPane1.text = "No Results" }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseArticle(jsonString: String?): ArticleEntity? {
        val gson = Gson()
        val jobj = gson.fromJson(jsonString, JsonObject::class.java)
        val artist = jobj["artist"].asJsonObject
        val bio = artist["bio"].asJsonObject
        val extract = bio["content"]
        val url = artist["url"]
        if (extract == null) {
            return null
        }
        val text = extract.asString.replace("\\n", "\n")
        val formattedText = textToHtml(text, artist["name"].asString)
        return ArticleEntity(artist["name"].asString, formattedText, url.asString)
    }

    private fun saveArticle(article: ArticleEntity) {
        Thread {
            database.ArticleDao().insertArticle(article)
        }.start()
    }

    private fun displayArticle(article: ArticleEntity) {
        val imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPane1.text = Html.fromHtml(article.biography)
            findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(article.articleUrl)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"

        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>${term!!.uppercase(Locale.getDefault())}</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}