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
import java.util.Random;

public class WebServer {

    private static final String TASK_ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/status";
    private static final String SEARCH_TOKEN_ENDPOINT = "/searchtoken";
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
        } catch (IOException e) 
        {
            e.printStackTrace();
            return;
        }

        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext taskContext = server.createContext(TASK_ENDPOINT);
        HttpContext searchContext = server.createContext(SEARCH_TOKEN_ENDPOINT);

        statusContext.setHandler(this::handleStatusCheckRequest);
        taskContext.setHandler(this::handleTaskRequest);
        searchContext.setHandler(this::handleSearchTokenRequest);

        server.setExecutor(Executors.newFixedThreadPool(1));
        server.start();
    }


    private void handleSearchTokenRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        boolean isDebugMode = false;
        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {
            isDebugMode = true;
        }

        long startTime = System.nanoTime();

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(requestBytes);
        String[] parts = bodyString.split(",");
        
        int n = Integer.parseInt(parts[0].trim());
        String target = parts[1].trim();

        int contador = 0;
        char[] cadenota = new char[n * 4];
        Random rand = new Random();

        // Generar la cadenota
        for (int i = 0; i < n * 4; i++) {
            if ((i + 1) % 4 == 0) {
                cadenota[i] = ' ';
            } else {
                int random_char = rand.nextInt(26) + 65;
                cadenota[i] = (char) random_char;
            }
        }

        // Extraer los 3 caracteres a buscar
        char t1 = target.charAt(0);
        char t2 = target.charAt(1);
        char t3 = target.charAt(2);

        // Buscar las coincidencias
        for (int i = 0; i < n * 4; i += 4) {
            if (cadenota[i] == t1 && cadenota[i + 1] == t2 && cadenota[i + 2] == t3) {
                contador++;
            }
        }

        // Solo devolver el número de coincidencias
        String respuesta = String.format("La subcadena se repitió %d veces.\n", contador);
        byte[] responseBytes = respuesta.getBytes();
        
        long finishTime = System.nanoTime();

        if (isDebugMode) {
            long totalNanos = finishTime - startTime;
            
            // --- CÁLCULO DE SEGUNDOS Y MILISEGUNDOS ---
            long segundos = totalNanos / 1_000_000_000L;
            // Obtenemos el sobrante en nanos y lo convertimos a milisegundos
            long milisegundos = (totalNanos % 1_000_000_000L) / 1_000_000L; 
            
            // Construcción del mensaje con el nuevo formato
            String debugMessage = String.format(
                "La operacion tomo %d nanosegundos = %d segundos con %d milisegundos.",
                totalNanos, segundos, milisegundos
            );

            
            exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
        }

        sendResponse(responseBytes, exchange);
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        // --- MODIFICACIÓN: Contar e imprimir los Headers recibidos ---
        System.out.println("\n[CABECERAS RECIBIDAS EN /task]");
        System.out.println("Total de headers: " + headers.size());
        
        headers.forEach((key, values) -> {
            // Un header puede contener múltiples valores separados por comas
            System.out.printf("  %s -> %s\n", key, values);
        });
        System.out.println("----------------------------------------");
        //
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

        try{
            Thread.sleep(5000);
        }catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // --- MODIFICACIÓN: Leer, medir e imprimir el cuerpo del mensaje ---
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(requestBytes);

        System.out.println("Número de bytes en el cuerpo del mensaje: " + requestBytes.length + " bytes");
        System.out.println("Cuerpo del mensaje: " + bodyString);
        System.out.println("----------------------------------------");
        //
        byte[] responseBytes = calculateResponse(requestBytes);
        long finishTime = System.nanoTime();

        if (isDebugMode) {
            long totalNanos = finishTime - startTime;
            
            // --- CÁLCULO DE SEGUNDOS Y MILISEGUNDOS ---
            long segundos = totalNanos / 1_000_000_000L;
            // Obtenemos el sobrante en nanos y lo convertimos a milisegundos
            long milisegundos = (totalNanos % 1_000_000_000L) / 1_000_000L; 
            
            // Construcción del mensaje con el nuevo formato
            String debugMessage = String.format(
                "La operacion tomo %d nanosegundos = %d segundos con %d milisegundos.",
                totalNanos, segundos, milisegundos
            );

            
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

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
}
