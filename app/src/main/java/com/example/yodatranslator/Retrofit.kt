package com.example.yodatranslator

import android.util.Log
import android.view.View
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

const val translateBaseUrl = "https://api.funtranslations.com"
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(translateBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val translationService: TranslationApi = retrofit.create(TranslationApi::class.java)


class TranslationRequest(val text: String)

class TranslationResponse(val contents: Content) {
    data class Content(
        val translated: String,
        val text: String,
        val translation: String
    )
}

interface TranslationApi {
    @POST("/translate/yoda.json")
    fun translateToYoda(@Body request: TranslationRequest): Call<TranslationResponse>

    @POST("/translate/morse.json")
    fun translateToMorse(@Body request: TranslationRequest): Call<TranslationResponse>
}

fun yodaTranslate(text: String, translatedText: TextView){
    translationService
        .translateToYoda(TranslationRequest(text))
        .enqueue(object : Callback<TranslationResponse> {
            override fun onResponse(
                call: Call<TranslationResponse>,
                response: Response<TranslationResponse>
            ) {
                Log.d("TRANSLATION_LOG", "Yoda says: ${response.body()?.contents?.translated}")
                translatedText.text = "${response.body()?.contents?.translated}"
            }
            override fun onFailure(call: Call<TranslationResponse>, t: Throwable) {
            }
        })
}


fun morseTranslate(text: String) {
    translationService
        .translateToMorse(TranslationRequest(text))
        .enqueue(object : Callback<TranslationResponse> {
            override fun onResponse(
                call: Call<TranslationResponse>,
                response: Response<TranslationResponse>
            ) {
                Log.d(
                    "TRANSLATION_LOG",
                    "${response.body()?.contents?.text} in Morse code: ${response.body()?.contents?.translated}"
                )
            }

            override fun onFailure(call: Call<TranslationResponse>, t: Throwable) {
            }
        })
}