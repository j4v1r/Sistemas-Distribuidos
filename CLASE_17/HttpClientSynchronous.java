package com.mkyong.java11.jep321;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import java.util.List;
import java.util.Map;
import java.util.Iterator;

import java.util.ArrayList;
import java.util.HashMap;

public class HttpClientSynchronous {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://httpbin.org/get"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print response headers
        HttpHeaders headers = response.headers();

        //headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

        Map<String, List<String>> map = headers.map();
        Iterator<Map.Entry<String, List<String>>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            String key = entry.getKey();
            List<String> values = entry.getValue();
            System.out.println(key + ":" + values);
        }

        //Se copia el Map devuelto a uno nuevo para modificarlo
        Map<String, List<String>> nuevoMapaHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            nuevoMapaHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        //NUEVOS HEADERS 
        nuevoMapaHeaders.computeIfAbsent("Set-Cookie", k -> new ArrayList<>());
        
        //Valores a la lista de "Set-Cookie"
        nuevoMapaHeaders.get("Set-Cookie").add("Max-Age=0");
        nuevoMapaHeaders.get("Set-Cookie").add("id=123");
        nuevoMapaHeaders.get("Set-Cookie").add("theme=dark");

        System.out.println("\n--- MAPA DE HEADERS (forEach) ---");
        nuevoMapaHeaders.forEach((k, v) -> System.out.println(k + ":" + v));


        System.out.println("\n--- MAPA DE HEADERS ORDENADO ALFABÉTICAMENTE ---");
        
        nuevoMapaHeaders.entrySet()            // Obtenemos el Set de entradas (pares clave-valor)
                .stream()                      // Lo convertimos en un Stream
                .sorted(Map.Entry.comparingByKey()) // Lo ordenamos alfabéticamente comparando las llaves
                .forEach(entry -> {            // Iteramos sobre cada entrada ya ordenada
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                });

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

    }

}