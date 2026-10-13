//PROYECTO03
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Cliente {

    public static void main(String[] args) throws Exception {

        String respuestaServidor;

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


        //Iniciar y dibujar la interfaz gráfica de terminal con Lanterna
        Screen screen = new TerminalScreen(
            new DefaultTerminalFactory().createTerminal()
        );
        screen.startScreen();
        screen.clear();

        TextGraphics tg = screen.newTextGraphics();
        
        tg.putString(2, 1, "--- CREADOR DE ASTEROIDES ---");

        tg.putString(2, 2, respuestaServidor);

        //Pedir los datos de creación del asteroide
        tg.putString(2, 4, "Ingresa la rapidez del asteroide (v): ");
        screen.refresh();
        String dato1 = leerDato(screen, 40, 4);

        tg.putString(2, 5, "Ingrese una coordenada (vector de dirección): ");
        screen.refresh();
        String dato2 = leerDato(screen, 48, 5);

        tg.putString(2, 6, "Ingrese la velocidad angular del asteroide y sentido de rotación: ");
        screen.refresh();
        String dato3 = leerDato(screen, 68, 6);

        tg.putString(2, 7, "Ingrese el número de asteroides a crear: ");
        screen.refresh();
        String dato4 = leerDato(screen, 43, 7);

        //Preparar los datos y enviarlos al servidor
        String datosAsteroide = dato1 + "," + dato2 + "," + dato3 + "," +dato4;
        tg.putString(2, 9, "Enviando datos al servidor...");
        screen.refresh();

        String respuesta = enviarDatosAsteroide(datosAsteroide);

        //Mostrar respuesta
        tg.putString(2, 11, "Respuesta del Servidor:");
        tg.putString(2, 12, respuesta);
        
        //Mantener abierto hasta presionar 'X'
        tg.putString(2, 14, "[Presiona la tecla 'X' para cerrar el programa]");
        screen.refresh();

        while (true) {
            KeyStroke key = screen.readInput(); // Bloquea hasta que se presiona una tecla
            if (key != null && key.getKeyType() == KeyType.Character) {
                // Convertimos a mayúscula para que acepte tanto 'x' como 'X'
                char tecla = Character.toUpperCase(key.getCharacter());
                if (tecla == 'X') {
                    break; // Rompe el ciclo infinito y sale
                }
            }
        }

        screen.stopScreen();
    }

    private static String leerDato(Screen screen, int x, int y) throws Exception {
        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke key = screen.readInput();
            
            if (key.getKeyType() == KeyType.Enter) {
                break; // El usuario terminó de escribir
            } else if (key.getKeyType() == KeyType.Backspace) {
                // Lógica para borrar caracteres
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    screen.newTextGraphics().putString(x, y, input.toString() + " "); // Sobrescribe con espacio
                    screen.refresh();
                }
            } else if (key.getKeyType() == KeyType.Character) {
                // Concatena el nuevo carácter
                input.append(key.getCharacter());
                screen.newTextGraphics().putString(x, y, input.toString());
                screen.refresh();
            }
        }
        return input.toString();
    }

    private static String enviarDatosAsteroide(String cuerpo) {
            try {
                HttpClient httpClient = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1)
                        .connectTimeout(Duration.ofSeconds(10))
                        .build();

                // Se usa BodyPublishers.ofString para incrustar los números en la petición POST
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                        .uri(URI.create("http://localhost:8080/crear_asteroide"))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().trim();
                
        } catch (Exception e) {
            return "Error de conexión: Verifica que tu WebServer esté encendido en el puerto 8080.";
        }
    }
}