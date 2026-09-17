public class PruebaRectangulo {
    public static void main(String[] args) {

        System.out.println("===== PRUEBA CON COORDENADAS CORRECTAS =====");

        try {
            // Coordenada superior izquierda
            Coordenada si = new Coordenada(2, 3);

            // Coordenada inferior derecha
            Coordenada id = new Coordenada(5, 1);

            // Crear rectangulo utilizando el nuevo constructor
            Rectangulo rect1 = new Rectangulo(si, id);

            double ancho, alto;

            System.out.println(rect1);

            alto = rect1.superiorIzquierda().ordenada()
                    - rect1.inferiorDerecha().ordenada();

            ancho = rect1.inferiorDerecha().abcisa()
                    - rect1.superiorIzquierda().abcisa();

            System.out.println("El area del rectangulo es = "
                    + ancho * alto);

        } catch (IllegalArgumentException e) {

            System.out.println(
                "Error al crear el objeto Rectangulo: "
                + e.getMessage()
            );
        }


        System.out.println("\n===== PRUEBA CON COORDENADAS INCORRECTAS =====");

        try {
            // Coordenada que NO esta arriba y a la izquierda
            Coordenada si = new Coordenada(5, 1);

            // Segunda coordenada
            Coordenada id = new Coordenada(2, 3);

            // Intentar crear el rectángulo
            Rectangulo rect2 = new Rectangulo(si, id);

            System.out.println(rect2);

        } catch (IllegalArgumentException e) {

            System.out.println(
                "Error al crear el objeto Rectangulo: "
                + e.getMessage()
            );
        }
    }
}
