//PROYECTO04
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2

/*
 *  MIT License
 *
 *  Copyright (c) 2019 Michael Pogrebinsky - Distributed Systems & Cloud Computing with Java
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;

import java.util.Map;
import java.util.HashMap;

public class WebServer {
    private static final String TASK_ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/status";
    private static final String MINE_TEXT_ENDPOINT = "/minetext";
    private static final String DISPATCHER_ENDPOINT = "/dispatcher";

    private final int port;
    private HttpServer server;

    public static void main(String[] args) {
        int serverPort = 8080;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }

        WebServer webServer = new WebServer(serverPort);
        webServer.startServer();

        System.out.println("Servidor escuchando en el puerto " + serverPort);
    }

    public WebServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext taskContext = server.createContext(TASK_ENDPOINT);
        HttpContext mineContext = server.createContext(MINE_TEXT_ENDPOINT);
        HttpContext dispatcherContext = server.createContext(DISPATCHER_ENDPOINT);

        statusContext.setHandler(this::handleStatusCheckRequest);
        taskContext.setHandler(this::handleTaskRequest);
        mineContext.setHandler(this::handleMineTextRequest);
        dispatcherContext.setHandler(this::handleDispatcherRequest);

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        if (headers.containsKey("X-Test") && headers.get("X-Test").get(0).equalsIgnoreCase("true")) {
            String dummyResponse = "123\n";
            sendResponse(dummyResponse.getBytes(), exchange);
            return;
        }

        boolean isDebugMode = false;
        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {
            isDebugMode = true;
        }

        long startTime = System.nanoTime();

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        byte[] responseBytes = calculateResponse(requestBytes);

        long finishTime = System.nanoTime();

        if (isDebugMode) {
            String debugMessage = String.format("La operación tomó %d nanosegundos", finishTime - startTime);
            exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
        }

        sendResponse(responseBytes, exchange);
    }

    private byte[] calculateResponse(byte[] requestBytes) {
        String bodyString = new String(requestBytes);
        String[] stringNumbers = bodyString.split(",");

        BigInteger result = BigInteger.ONE;

        for (String number : stringNumbers) {
            BigInteger bigInteger = new BigInteger(number);
            result = result.multiply(bigInteger);
        }

        return String.format("El resultado de la multiplicación es %s\n", result).getBytes();
    }

    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        String responseMessage = "El servidor está vivo\n";
        sendResponse(responseMessage.getBytes(), exchange);
    }

    private void handleMineTextRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        // HEADERS personalizados del ENDPOINT
        Headers headers = exchange.getRequestHeaders();

        if (headers.containsKey("X-Test") && headers.get("X-Test").get(0).equalsIgnoreCase("true")) {
            String dummyResponse = "123\n";
            sendResponse(dummyResponse.getBytes(), exchange);
            return;
        }

        boolean isDebugMode = false;
        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {
            isDebugMode = true;
        }

        long startTime = System.nanoTime();

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        byte[] responseBytes = calcularPuntaje(requestBytes);

        long finishTime = System.nanoTime();

        if (isDebugMode) {
            long totalNanos = finishTime - startTime;
            long segundos = totalNanos / 1_000_000_000L;
            long milisegundos = (totalNanos % 1_000_000_000L) / 1_000_000L; 
            String debugMessage = String.format("La operacion tomo %d nanosegundos = %d segundos con %d milisegundos", 
                totalNanos, segundos, milisegundos);
            exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
        }

        sendResponse(responseBytes, exchange);
    }

    private byte[] calcularPuntaje(byte[] requestBytes) {
        String bodyString = new String(requestBytes);
        String[] argumentos = bodyString.split(",");
        
        String oracion = argumentos[1];
        String[] palabras = oracion.split("\\s+");

        Map<String, Integer> conteoPalabras = new HashMap<>();

        return String.format("La calificación del libro es %s\n", result).getBytes();
    }

    private void handleDispatcherRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        boolean isDebugMode = headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true");
        long startTime = System.nanoTime();

        // 1. Leer los datos enviados por la página web (ej. "frase_a_buscar")
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(requestBytes, java.nio.charset.StandardCharsets.UTF-8);
        String hashObjetivo = bodyString.trim(); // Extraer la oración a buscar
        String[] palabras = oracion.split("\\s+"); // Arreglo con las palabras de la oración para calcular el IDF

        // 2. Calcular el IDF para todos los libros en la carpeta 'LIBROS_TXT'
        File carpetaTextos = new File("./LIBROS_TXT");
        File[] archivos = carpetaTextos.listFiles();

        List<String> listaTareas = new ArrayList<>();

        int totalLibros = 0;

        // Map para guardar cuántos libros contienen la palabra
        Map<String, Integer> mapContador = new HashMap<>(); 
        // Inicializar el Map en 0
        for(String p : palabras) { 
            mapContador.put(p, 0); 
        }

        // Obtener el número de libros que contienen cada palabra de la oración
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    totalLibros++;
                    for (String palabra : palabras) {
                        if (existePalabra(archivo, palabra)) {
                            mapContador.put(palabra, mapContador.get(palabra) + 1);
                        }
                    }
                }
            }
        }

        // Constructor de Strings para el formato de String con el IDF de cada palabra al ENDPOINT 
        StringBuilder idfDataBuilder = new StringBuilder();

        for (String palabra : palabras) {
            int librosQueLaContienen = mapContador.get(palabra);
            double idf = 0.0;
            
            // Evitar división por cero si la palabra no existe en ningún libro
            if (librosQueLaContienen > 0) {
                idf = Math.log10((double) totalLibros / librosQueLaContienen);
            }
            
            // Formato final de la cadena con IDF para el ENDPOINT: palabra:1.52|otra:0.84|
            idfDataBuilder.append(palabra).append(":").append(idf).append("|");
        }

        // Conversión a String para enviar el IDF al ENDPOINT
        String idfData = idfDataBuilder.toString();

        // Adjuntar las tareas para el calculo de los textos en la carpeta 'LIBROS_TXT'
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    // Ejemplo: "don_quijote.txt,la frase de búsqueda,palabra1:1.5|palabra2:0.9|"
                    listaTareas.add(archivo.getName() + "," + fraseDeBusqueda + "," + idfData);
                }
            }
        }

        // 3. Enviar a los Workers usando Aggregator.java
        Aggregator aggregator = new Aggregator();
        List<String> workers = List.of("http://localhost:8081/minetext", "http://localhost:8082/minetext", "http://localhost:8083/minetext");
        
        System.out.println("Iniciando despacho de tareas a los workers...");
        List<String> resultados = aggregator.sendTasksToWorkers(workers, listaTareas);

        // 4. Filtrar el resultado exitoso
        String respuestaFinal = "Contraseña no encontrada\n";
        for (String res : resultados) {
            if (res != null && !res.equals("NULL")) {
                respuestaFinal = res; // Guardamos "La contraseña es: escom"
                break;
            }
        }

        byte[] responseBytes = respuestaFinal.getBytes();
        long finishTime = System.nanoTime();

        // 5. Header de depuración de tiempo
        if (isDebugMode) {
            long totalNanos = finishTime - startTime;
            long segundos = totalNanos / 1_000_000_000L;
            long milisegundos = (totalNanos % 1_000_000_000L) / 1_000_000L; 
            String debugMessage = String.format("La operacion tomo %d nanosegundos = %d segundos con %d milisegundos", 
                totalNanos, segundos, milisegundos);
            exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
        }

        sendResponse(responseBytes, exchange);
    }

    private boolean existePalabra(File archivo, String palabra) {
        boolean existe = false;

        try {
            String contenido = Files.readString(archivo.toPath());
            String[] palabrasArchivo = contenido.toLowerCase().split("\\s+");
            String palabraBuscada = palabra.toLowerCase().trim();

            for (String p : palabrasArchivo) {
                // Se quitan los signos de puntuación pegados a la palabra para evitar falsos negativos
                String palabraLimpia = p.replaceAll("[^a-záéíóúñ0-9]", "");
                if (palabraLimpia.equals(palabraBuscada)) {
                    existe = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo: " + archivo.getName());
        }

        return existe;
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
}
