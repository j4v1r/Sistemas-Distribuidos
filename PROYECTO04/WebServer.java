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
import java.util.Comparator;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        String bodyString = new String(requestBytes, java.nio.charset.StandardCharsets.UTF_8).trim();
        String[] argumentos = bodyString.split(";;;");
        
        String nombreArchivo = argumentos[0].trim();
        System.out.println("--> Procesando tarea para el libro: " + nombreArchivo);
        
        String oracion = argumentos[1].trim();
        String idfData = argumentos.length > 2 ? argumentos[2].trim() : "";

        // Reconstruir el mapa de valores IDF
        Map<String, Double> idfMap = new HashMap<>();
        if (!idfData.isEmpty()) {
            String[] paresIdf = idfData.split("\\|");
            for (String par : paresIdf) {
                if (par.isEmpty()) continue;
                String[] claveValor = par.split(":");
                if (claveValor.length == 2) {
                    idfMap.put(claveValor[0], Double.parseDouble(claveValor[1]));
                }
            }
        }

        String[] palabrasOracion = oracion.toLowerCase().split("\\s+");
        double puntajeTotal = 0.0;

        // Leer el libro asignado y calcular el TF-IDF
        try {
            File archivo = new File("./LIBROS_TXT/" + nombreArchivo);
            String contenido = leerArchivoSeguro(archivo);
            String[] palabrasArchivo = contenido.toLowerCase().split("\\s+");
            int totalPalabrasLibro = palabrasArchivo.length;

            for (String palabraBuscada : palabrasOracion) {
                String palabraBuscadaLimpia = palabraBuscada.replaceAll("[^a-záéíóúñ0-9]", "");
                int apariciones = 0;

                // Contar el Term Frequency (TF)
                for (String pLibro : palabrasArchivo) {
                    String pLimpia = pLibro.replaceAll("[^a-záéíóúñ0-9]", "");
                    if (pLimpia.equals(palabraBuscadaLimpia)) {
                        apariciones++;
                    }
                }

                // Calcular TF y multiplicarlo por el IDF proporcionado
                double tf = (double) apariciones / totalPalabrasLibro;
                double idf = idfMap.getOrDefault(palabraBuscadaLimpia, 0.0);
                
                puntajeTotal += (tf * idf);
            }

        } catch (IOException e) {
            System.out.println("Error en el Worker leyendo el archivo: " + nombreArchivo);
        }

        // 4. Retornar el formato requerido por el Aggregator: "nombreLibro.txt,1.45"
        return (nombreArchivo + ";;;" + puntajeTotal).getBytes(java.nio.charset.StandardCharsets.UTF_8);
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
        String oracion = new String(requestBytes, java.nio.charset.StandardCharsets.UTF_8);

        // Revisar si la oración está vacía
        if (oracion.isEmpty()) {
            sendResponse("La frase está vacía.\n".getBytes(), exchange);
            return;
        }

        String[] palabras = oracion.toLowerCase().split("\\s+"); // Arreglo con las palabras de la oración para calcular el IDF

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

        // 2.1 Obtener el número de libros que contienen cada palabra de la oración
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

        // 2.2 Cálculo del IDF de cada palabra
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

        // 3. Adjuntar las tareas para el calculo del puntaje de los textos en la carpeta 'LIBROS_TXT'
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    // Ejemplo: "don_quijote.txt,la frase de búsqueda,palabra1:1.5|palabra2:0.9|"
                    listaTareas.add(archivo.getName() + ";;;" + oracion + ";;;" + idfData);
                }
            }
        }

        // 4. Enviar a los Workers usando Aggregator.java
        Aggregator aggregator = new Aggregator();
        List<String> workers = List.of("http://localhost:8081/minetext", "http://localhost:8082/minetext", "http://localhost:8083/minetext");
        //List<String> workers = List.of("http://10.128.0.15:8080/minetext", "http://10.128.0.16:8080/minetext", "http://10.128.0.17:8080/minetext");
        
        System.out.println("Iniciando despacho de tareas a los workers...");
        List<Aggregator.ResultadoLibro> resultadosFinales = aggregator.sendTasksToWorkers(workers, listaTareas);

        // Ordenar matemáticamente de Mayor a Menor puntaje
        resultadosFinales.sort((r1, r2) -> Double.compare(r2.puntaje(), r1.puntaje()));

        // 7. Formatear la cadena de respuesta que recibirá el usuario
        StringBuilder respuestaFinal = new StringBuilder();
        respuestaFinal.append("Resultados ordenados por relevancia:\n\n");
        
        for (Aggregator.ResultadoLibro res : resultadosFinales) {
            respuestaFinal.append(res.nombreLibro())
                          .append(" -> Puntaje TF-IDF: ")
                          .append(String.format("%.6f", res.puntaje()))
                          .append("\n");
        }

        byte[] responseBytes = respuestaFinal.toString().getBytes();
        long finishTime = System.nanoTime();

        // 7. Header de depuración de tiempo
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
            String contenido = leerArchivoSeguro(archivo);
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

    private String leerArchivoSeguro(File archivo) throws IOException {
        try {
            // Intentar leer el texto con el estándar moderno (UTF-8)
            return Files.readString(archivo.toPath(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            // Si el archivo tiene acentos en formato antiguo, usar ISO-8859-1
            return Files.readString(archivo.toPath(), java.nio.charset.StandardCharsets.ISO_8859_1);
        }
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
