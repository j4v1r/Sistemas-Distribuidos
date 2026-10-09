//PROYECTO03
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Cliente {

    public static void main(String[] args) throws Exception {
        
        // 1. Realizar la petición síncrona al servidor HTTP
        String respuestaServidor = "";
        
        try {
            // Se configura el cliente con un tiempo de espera máximo de 10 segundos
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            // Se construye la petición GET apuntando al endpoint de estado
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/status"))
                    .build();

            // Se envía la petición de forma síncrona y se obtiene el cuerpo como String
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Guardamos la respuesta eliminando saltos de línea extra
            respuestaServidor = response.body().trim(); 
            
        } catch (Exception e) {
            respuestaServidor = "Error: No se pudo conectar con el servidor en el puerto 8080.";
        }

        // 2. Iniciar y dibujar la interfaz gráfica de terminal con Lanterna
        Screen screen = new TerminalScreen(
            new DefaultTerminalFactory().createTerminal()
        );

        screen.startScreen();
        screen.clear();

        // Se imprime el resultado obtenido de la petición síncrona
        screen.newTextGraphics().putString(2, 2, "Comunicación Síncrona (WSL Ubuntu)");
        screen.newTextGraphics().putString(2, 4, "Respuesta: " + respuestaServidor);
        screen.newTextGraphics().putString(2, 6, "[Presiona cualquier tecla para salir]");

        screen.refresh();
        screen.readInput();
        screen.stopScreen();
    }

}