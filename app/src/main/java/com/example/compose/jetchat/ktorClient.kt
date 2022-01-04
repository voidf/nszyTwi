package com.example.compose.jetchat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.media.ImageReader
import android.net.sip.SipErrorCode
import android.net.sip.SipErrorCode.TIME_OUT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.compose.jetchat.data.errorUserData
import kotlinx.coroutines.runBlocking
import java.util.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

val ktorClient = HttpClient(Android){
    install(JsonFeature) {
        serializer = KotlinxSerializer(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })

        engine {
            connectTimeout = SipErrorCode.TIME_OUT
            socketTimeout = SipErrorCode.TIME_OUT
        }
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
//                Log.v("Logger Ktor =>", message)
            }

        }
        level = LogLevel.ALL
    }

    install(ResponseObserver) {
        onResponse { response ->
//            Log.d("HTTP status:", "${response.status.value}")
        }
    }
    install(HttpCookies) {
        // Will keep an in-memory map with all the cookies from previous requests.
        storage = AcceptAllCookiesStorage()
    }
    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}
val api_host = "http://4kr.top:8588"

@kotlinx.serialization.Serializable
data class Resp<T>(
    val data: T,
    val msg: String,
    val status: Boolean
)

@kotlinx.serialization.Serializable
data class UserData(
    val username: String,
    val avatar: String? = null,
    val desc: String? = null,
    val follows: List<UserData>? = null
)

@kotlinx.serialization.Serializable
data class SingleTwiData(
    val id: String,
    val content: String,
    val author: UserData,
    val is_top: Boolean,
    val post_time: Long,
    val comments: List<SingleTwiData>? = null
)



suspend fun getAllTwi(): MutableList<SingleTwiData> {
    try {
        val r = ktorClient.get<Resp<List<SingleTwiData>>>("$api_host/twi/all")
        val li = mutableListOf<SingleTwiData>(*r.data.toTypedArray())
        Log.d("<<<suc>>>", "${r.data}")
        return li
    } catch (e: Exception) {
        Log.d("【req】", "$e")
        throw e
    }
}
@kotlinx.serialization.Serializable
data class NewTwiForm(
    val content: String,
)

suspend fun sendTwi(content: String){
    try {
        val r: HttpResponse = ktorClient.post("$api_host/twi/new"){
            body=NewTwiForm(content)
        }
        if (r.status != HttpStatusCode.Accepted)
        {
            Log.d("【sendTwi】[${r.status.value}]", r.content.toString())
        }
    } catch (e: Exception){
        Log.d("【sendTwi】Error!", e.toString())
    }
}
@kotlinx.serialization.Serializable
data class CommentTwiForm(
    val content: String,
    val tid: String
)

suspend fun sendComment(content: String, tid: String){
    try {
        val r: HttpResponse = ktorClient.post("$api_host/twi/comment"){
            body=CommentTwiForm(content, tid)
        }
        if (r.status != HttpStatusCode.Accepted) {
            Log.d("【sendComment】[${r.status.value}]", r.content.toString())
        }
    } catch (e: Exception){
        Log.d("【sendComment】Error!", e.toString())
    }
}

suspend fun sendFo(uid: String){
    try {
        val r: HttpResponse = ktorClient.get("$api_host/user/fo?uid=$uid")
        if (r.status != HttpStatusCode.Accepted) {
            Log.d("【sendFo】[${r.status.value}]", r.content.toString())
        }
    } catch (e: Exception){
        Log.d("【sendFo】Error!", e.toString())
    }
}

suspend fun sendUnfo(uid: String){
    try {
        val r: HttpResponse = ktorClient.get("$api_host/user/unfo?uid=$uid")
        if (r.status != HttpStatusCode.Accepted) {
            Log.d("【sendUnfo】[${r.status.value}]", r.content.toString())
        }
    } catch (e: Exception){
        Log.d("【sendUnfo】Error!", e.toString())
    }
}

suspend fun getUser(uid: String): UserData {
    try {
        val r = ktorClient.get<Resp<UserData>>("$api_host/user/profile?uid=$uid")
        return r.data
    } catch (e: Exception){
        Log.d("【getUser】Error!", e.toString())
        return errorUserData
    }
}




//class Result<T> (x: T?, status: Int){
//    companion object{
//        val Loading = Result(null, 0)
//    }
//}

//@Composable
//fun loadNetworkImage(
//    url: String,
//): State<Result<Image>> {
//
//    // Creates a State<T> with Result.Loading as initial value
//    // If either `url` or `imageRepository` changes, the running producer
//    // will cancel and will be re-launched with the new inputs.
//    return produceState<Result<Image>>(initialValue = Result<Image>.Loading, url, imageRepository) {
//
//        // In a coroutine, can make suspend calls
//        val image = imageRepository.load(url)
//
//        // Update State with either an Error or Success result.
//        // This will trigger a recomposition where this State is read
//        value = if (image == null) {
//            Result.Error
//        } else {
//            Result.Success(image)
//        }
//    }
//}
