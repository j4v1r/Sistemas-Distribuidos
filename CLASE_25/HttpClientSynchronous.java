package com.mkyong.java11.jep321;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

public class HttpClientSynchronous {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // 1. Leer los archivos de texto
        ArrayList<String> nombres = leerArchivo("nombres.txt");
        ArrayList<String> apellidos = leerArchivo("apellidos.txt");
        Random random = new Random(12345); // Semilla fija

        // 2. Plantilla JSON con String.format
        // %s se sustituye por texto y números según el orden de los argumentos
        String jsonTemplate = "{ \"nombre\": \"%s\", \"apellido1\": \"%s\", \"apellido2\": \"%s\", \"promedio\": %s }";

        // 3. Generar combinaciones y enviar peticiones HTTP POST
        for (String nombre : nombres) {
            for (String ap1 : apellidos) {
                for (String ap2 : apellidos) {

                    // Generar calificación aleatoria
                    double numero = random.nextDouble() * 100;
                    numero = Math.round(numero * 10) / 100.0;

                    // Construir el JSON con los datos actuales
                    String json = String.format(jsonTemplate, nombre, ap1, ap2, numero);

                    // Configurar la petición POST
                    HttpRequest request = HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .uri(URI.create("http://localhost:8080/alumnos"))
                            .setHeader("Content-Type", "application/json") // Cabecera necesaria para JSON
                            .build();

                    // Enviar petición y recibir respuesta
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    // Imprimir datos para la captura de pantalla
                    System.out.println("Enviando POST: " + json);
                    System.out.println("Respuesta del Servidor (Código " + response.statusCode() + "): " + response.body());
                    System.out.println("---------------------------------------------------");
                }
            }
        }
    }

    // Método para leer archivo línea por línea
    public static ArrayList<String> leerArchivo(String nombreArchivo) {
        ArrayList<String> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) { // evitar líneas vacías
                    lista.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + nombreArchivo);
            e.printStackTrace();
        }
        return lista;
    }
}