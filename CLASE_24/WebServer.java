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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

import java.security.MessageDigest;

import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson; 

public class WebServer {
    private static final String TASK_ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/status";
    private static final String SEARCH_TOKEN_ENDPOINT = "/searchtoken";
    private static final String IPN_ENDPOINT = "/ipn";
    private static final String ALUMNOS_ENDPOINT = "/alumnos";

    private final int port;
    private HttpServer server;

    private Map<Integer, Alumno> alumnos;
    private int contador;
    private Gson gson;

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
        alumnos = new HashMap<>();
        contador=0;
        gson = new Gson();
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
        HttpContext searchContext = server.createContext(SEARCH_TOKEN_ENDPOINT);
        HttpContext ipnContext = server.createContext(IPN_ENDPOINT);
        HttpContext alumnosContext = server.createContext(ALUMNOS_ENDPOINT);

        statusContext.setHandler(this::handleStatusCheckRequest);
        taskContext.setHandler(this::handleTaskRequest);
        searchContext.setHandler(this::handleSearchTokenRequest);
        ipnContext.setHandler(this::handleIPNRequest);
        alumnosContext.setHandler(this::handleAlumnosRequest);

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
        	exchange.sendResponseHeaders(405,-1);
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
        byte[] responseBytes = null;
        
        try{
            responseBytes = obtenerPassword(requestBytes);
        }catch(Exception e){
            System.out.println("ERROR ERROR ERROR");
        }

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

    private byte[] obtenerPassword(byte[] requestBytes) throws Exception{
        String bodyString = new String(requestBytes);
        String[] stringNumbers = bodyString.split(",");

        String hashObjetivo = stringNumbers[0];
        char inicio = stringNumbers[1].charAt(0);

        System.out.println("Hash objetivo: " + hashObjetivo);
        System.out.println("Letra inicial: "+inicio);
        System.out.println("Buscando contraseña...\n");

        char a = inicio;

            for(char b='a'; b<='z'; b++){
                for(char c='a'; c<='z'; c++){
                    for(char d='a'; d<='z'; d++){
                        for(char e='a'; e<='z'; e++){

                            String intento = "" + a + b + c + d + e;

                            String hash = sha256(intento);

                            if(hash.equals(hashObjetivo)){
                                return String.format("La contraseña es: "+intento+"\n").getBytes();
                            }
                        }
                    }
                }
            }

        return String.format("NULL").getBytes();
    }

    public static String sha256(String texto) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] hash = md.digest(texto.getBytes());

        StringBuilder hex = new StringBuilder();

        for(byte b : hash){
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }

    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        String responseMessage = String.format("El servidor está vivo en el puerto " + port + "\n");
        sendResponse(responseMessage.getBytes(), exchange);
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

    private void handleIPNRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        String userAgent = headers.containsKey("User-Agent") ? headers.get("User-Agent").get(0):"";

        byte[] responseBytes;
        String contentType;

        try{

            if(userAgent.contains("Java 11 HttpClient Bot")){
                String asciiArt = new String(Files.readAllBytes(Paths.get("./resources/ascii-ipn.txt")));
                responseBytes = asciiArt.getBytes();
                contentType = "text/plain; charset=UTF-8";
            } else {
                File file = new File("./resources/ipn.png");
                if (file.exists()) {
                    responseBytes = Files.readAllBytes(file.toPath());
                    contentType = "image/png";
                } else {
                    // Si la imagen no existe, enviar un mensaje de error
                    responseBytes = "Archivo ipn.png no encontrado.".getBytes();
                    contentType = "text/plain; charset=UTF-8";
                }
            }

            exchange.getResponseHeaders().set("Content-Type", contentType);
            sendResponse(responseBytes, exchange);
        
        } catch (IOException e) {

            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        }      
    }

    //CLASE 23: Código para ENDPOINT /alumnos. Métodos GET, POST, PUT y DELETE.
    //CLASE 24: ENDPOINT /alumnos actualizado a API REST JSON con códigos HTTP adecuados
    private void handleAlumnosRequest(HttpExchange exchange) throws IOException {
        String ruta = exchange.getRequestURI().getPath();
        String metodo = exchange.getRequestMethod();
        String[] partes = ruta.split("/");

        try {
            if(metodo.equalsIgnoreCase("get")) {
                // Código del método GET
                if (partes.length < 3) {
                    //400-Bad Request: El cliente envió datos incorrectos.
                    exchange.sendResponseHeaders(400, -1);
                    exchange.close();
                    return;
                }
                
                try {
                    //Verificación de ID numérico
                    int id = Integer.parseInt(partes[2]);
                    
                    Alumno alum = alumnos.get(id);
                    if (alum != null) {
                        //200-OK: La solicitud se ejecutó correctamente.
                        //Se devuelve el JSON del alumno con ID 'id'.
                        sendJsonResponse(exchange, 200, gson.toJson(alum));

                    } else {
                        //404-Not Found: El recurso solicitado no existe.
                        sendJsonResponse(exchange, 404, generarErrorJson("Alumno no encontrado"));
                    }

                } catch (NumberFormatException e) {
                    //400-Bad Request: El cliente envió un ID invalido.
                    sendJsonResponse(exchange, 400, generarErrorJson("ID invalido"));
                }

            } else if(metodo.equalsIgnoreCase("post")) {
                // Código del método POST

                //Leer POST en formato JSON y devolver HTTP 201-Created: Se creó un recurso nuevo.
                byte[] requestBytes = exchange.getRequestBody().readAllBytes();
                String bodyString = new String(requestBytes);
                
                //Convertir el JSON entrante a un objeto Alumno
                Alumno inputAlumno = gson.fromJson(bodyString, Alumno.class);

                contador++;
                int id = contador;
                
                //Aseguramos que el nuevo alumno tenga el ID autogenerado
                Alumno nuevoAlumno = new Alumno(id, inputAlumno.getNombre(), 
                                               inputAlumno.getApellido1(), 
                                               inputAlumno.getApellido2(), 
                                               inputAlumno.getPromedio());

                alumnos.put(id, nuevoAlumno);

                //Devolver JSON del alumno insertado con código 201-Created: Se creó un nuevo alumno.
                sendJsonResponse(exchange, 201, gson.toJson(nuevoAlumno));
                
            } else if(metodo.equalsIgnoreCase("put")) {
                // Código del método PUT
                try {
                    //Verificación de ID numérico
                    int id = Integer.parseInt(partes[2]);
                    
                    //Actualizar si existe (200-OK:La solicitud se ejecutó correctamente), 
                    //o devolver error si no (404-Not Found: El recurso solicitado no existe).
                    if (!alumnos.containsKey(id)) {
                        //404-Not Found: El alumno con ID 'id' no existe.
                        sendJsonResponse(exchange, 404, generarErrorJson("Alumno no encontrado"));
                        return;
                    }

                    byte[] requestBytes = exchange.getRequestBody().readAllBytes();
                    String bodyString = new String(requestBytes);
                    Alumno inputAlumno = gson.fromJson(bodyString, Alumno.class);
                    
                    Alumno alumActualizado = new Alumno(id, inputAlumno.getNombre(), 
                                                  inputAlumno.getApellido1(), 
                                                  inputAlumno.getApellido2(), 
                                                  inputAlumno.getPromedio());

                    alumnos.replace(id, alumActualizado);

                    Map<String, String> mensaje = new HashMap<>();
                    mensaje.put("mensaje", "Alumno actualizado");
                    sendJsonResponse(exchange, 200, gson.toJson(mensaje));

                } catch (NumberFormatException e) {
                    sendJsonResponse(exchange, 400, generarErrorJson("ID invalido"));
                }

            } else if(metodo.equalsIgnoreCase("delete")) {
                // Código del método DELETE
                try {
                    //Verificación de ID numérico
                    int id = Integer.parseInt(partes[2]);
                    
                    //Borrar si existe (204-No Content: Operación correcta, pero sin contenido de respuesta), 
                    //o devolver error si no (404-Not Found: El recurso solicitado no existe).
                    if (!alumnos.containsKey(id)) {
                        //404-Not Found: El alumno con ID 'id' no existe.
                        sendJsonResponse(exchange, 404, generarErrorJson("Alumno no encontrado"));
                        return;
                    }
                    
                    alumnos.remove(id);
                    
                    //Código 204-No Content: Se eliminó alumno correctamente, 
                    //pero sin contenido de respuesta.
                    exchange.sendResponseHeaders(204, -1);
                    exchange.close();

                } catch (NumberFormatException e) {
                    sendJsonResponse(exchange, 400, generarErrorJson("ID invalido"));
                }
                
            } else {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
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

    //CLASE 24: Método auxiliar para enviar respuestas JSON estándar.
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        byte[] responseBytes = jsonResponse.getBytes("UTF-8");

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        exchange.close();
    }

    //CLASE 24: Método auxiliar para crear el mapa de errores JSON
    private String generarErrorJson(String mensajeError) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensajeError);

        return gson.toJson(error);
    }
}
