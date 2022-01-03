package com.example.compose.jetchat

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest

import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

val ktorClient = HttpClient(Android){

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}
