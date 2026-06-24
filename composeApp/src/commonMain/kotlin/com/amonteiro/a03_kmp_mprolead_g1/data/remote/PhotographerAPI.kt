package com.amonteiro.a03_kmp_mprolead_g1.data.remote

import com.amonteiro.a03_kmp_mprolead_g1.BuildConfig
import com.amonteiro.a03_kmp_mprolead_g1.di.initKoin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import org.koin.mp.KoinPlatform

@Serializable //KotlinX impose cette annotation
data class PhotographerDTO(
    var id: Int,
    val stageName: String,
    val photoUrl: String,
    val story: String,
    val portfolio: List<String>,
)

suspend fun testPhotographer() {

    initKoin()

    val photographerAPI = KoinPlatform.getKoin().get<PhotographerAPI>()

    println(photographerAPI.loadPhotographers().joinToString(separator = "\n\n"))


    //Pour que le programme s'arrête, inutile sur Android
    photographerAPI.close()
}

class PhotographerAPI(val client : HttpClient ) {

    companion object {
        private const val API_URL =
            "https://www.amonteiro.fr/api/photographers?apikey=${BuildConfig.PHOTOGRAPHER_API_KEY}"
    }
    //Déclaration du client


    //GET
    suspend fun loadPhotographers(): List<PhotographerDTO> {
        val response = client.get(API_URL) {
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        return response.body()
    }

    fun close() {
        client.close()
    }
}