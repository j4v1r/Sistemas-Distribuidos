import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Path;

public class GoogleOAuthJWT {

    public static void main(String[] args) throws Exception {

        // 1. Leer JSON credentials
        String json = new String(Files.readAllBytes(Paths.get("project-4921087f-b128-4317-813-82bb2c0d5758.json")));

        JsonObject creds = JsonParser.parseString(json).getAsJsonObject();

        String clientEmail = creds.get("client_email").getAsString();
        String privateKeyPem = creds.get("private_key").getAsString();
        String tokenUri = creds.get("token_uri").getAsString();

        // 2. Limpiar clave privada PEM
        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);

        // 3. Crear Header JWT
        String headerJson = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
        String header = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));

        // 4. Crear Payload JWT
        long now = Instant.now().getEpochSecond();

        String payloadJson = "{"
                + "\"iss\":\"" + clientEmail + "\","
                + "\"scope\":\"https://www.googleapis.com/auth/devstorage.read_only\","
                + "\"aud\":\"" + tokenUri + "\","
                + "\"exp\":" + (now + 3600) + ","
                + "\"iat\":" + now
                + "}";

        String payload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

        // 5. Firmar JWT
        String unsignedToken = header + "." + payload;

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(unsignedToken.getBytes(StandardCharsets.UTF_8));
        byte[] signed = signature.sign();

        String jwt = unsignedToken + "." + base64UrlEncode(signed);

        // 6. Enviar a OAuth token endpoint
        String requestBody = "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer"
                + "&assertion=" + jwt;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUri))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 7. Extraer el access_token del JSON devuelto por OAuth
        JsonObject jsonTokenResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        String accessToken = jsonTokenResponse.get("access_token").getAsString();
        System.out.println("Token OAuth 2.0 obtenido exitosamente.");

        // 8. Solicitar el archivo al bucket usando el token
        String urlArchivo = "https://storage.googleapis.com/storage/v1/b/bucket-cl28/o/ipn.png?alt=media";

        HttpRequest downloadRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlArchivo))
                .header("Authorization", "Bearer " + accessToken) // Equivalente a -H "Authorization: Bearer token"
                .GET()
                .build();

        // 9. Enviar petición y guardar como archivo (Equivalente a -o cupcake.jpg)
        HttpResponse<Path> downloadResponse = client.send(downloadRequest, HttpResponse.BodyHandlers.ofFile(Paths.get("ipn.png")));

        // 10. Mostrar resultados
        System.out.println("Código de estado de la descarga: " + downloadResponse.statusCode());
        
        if (downloadResponse.statusCode() == 200) {
            System.out.println("Archivo guardado exitosamente en: " + downloadResponse.body().toAbsolutePath());
        } else {
            System.out.println("Hubo un error al descargar. Revisa que el token tenga permisos de lectura y la URL sea correcta.");
        }
    }

    private static String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }
}
