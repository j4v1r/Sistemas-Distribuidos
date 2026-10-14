//PROYECTO03
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
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

import javax.swing.*;

public class Servidor extends JFrame {
    
    private static final String STATUS_ENDPOINT = "/status";
    private static final String CREAR_ASTEROIDE_ENDPOINT = "/crear_asteroide";

    private final int port;
    private HttpServer server;

    private int num_ast=0;

    public static void main(String[] args) {
        int serverPort = 8080;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }

        Servidor webServer = new Servidor(serverPort);
        webServer.startServer();

        System.out.println("Servidor escuchando en el puerto " + serverPort);

        // 2. Nueva lógica gráfica: Crear la ventana y agregar el PanelAsteroides
        JFrame ventana = new JFrame("Servidor - Simulador de Asteroides");
        ventana.setSize(1000, 1000);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Si cierras la ventana, se apaga el servidor
        ventana.setLocationRelativeTo(null); // Centrar en la pantalla
        
        // Instancias y agregas tu panel negro a la ventana
        PanelAsteroides panel = new PanelAsteroides();
        ventana.add(panel);
        
        // Haces visible la ventana
        ventana.setVisible(true);
    }

    public Servidor(int port) {
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
        HttpContext crearAsteroideContext = server.createContext(CREAR_ASTEROIDE_ENDPOINT);

        statusContext.setHandler(this::handleStatusCheckRequest);
        crearAsteroideContext.setHandler(this::handleCrearAsteroideRequest);

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        String responseMessage = "TOTAL DE ASTEROIDES CREADOS: "+num_ast;
        sendResponse(responseMessage.getBytes(), exchange);
    }

    private void handleCrearAsteroideRequest(HttpExchange exchange) throws IOException {
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
        String[] datosAsteroide = bodyString.split(",");

        int num_crear = Integer.parseInt(datosAsteroide[3]);
        num_ast+=num_crear;

        /*for(String d : datosAsteroide){
            System.out.println(d);
        }*/

        return String.format("Los asteroides fueron creados.\n").getBytes();
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