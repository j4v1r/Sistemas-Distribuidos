package com.mkyong.java11.jep321;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientSynchronous {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // URL limpia sin saltos de línea y con las comillas codificadas (%22)
        String url = "https://translation.googleapis.com/language/translate/v2?target=es&key=AIzaSyCMuukUV8bgc016K8oSJrsNEzv-xYOfOf4&q=%22Political%20correctness%20is%20the%20attempt%20to%20control%20language%20and%20conduct%20by%20imposing%20an%20ideological%20hegemony,%20which%20is%20the%20very%20antithesis%20of%20free%20speech.%22";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprimir cabeceras de respuesta
        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

        // Imprimir código de estado
        System.out.println("Código de estado: " + response.statusCode());

        // Imprimir cuerpo de la respuesta devuelto por la API
        System.out.println("Respuesta: " + response.body());
    }
}

