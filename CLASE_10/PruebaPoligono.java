public class PruebaPoligono {

    public static void main(String[] args) {

        PoligonoIrreg poligono = new PoligonoIrreg();

        System.out.println("=== POLIGONO RECIEN CREADO ===");
        System.out.println(poligono);

        for (int i = 0; i < 3; i++) {

            double x = (Math.random() * 200.0) - 100.0;
            double y = (Math.random() * 200.0) - 100.0;

            x = Math.round(x * 1000.0) / 1000.0;
            y = Math.round(y * 1000.0) / 1000.0;

            Coordenada nuevaCoordenada =
                    new Coordenada(x, y);

            poligono.anadeVertice(nuevaCoordenada);
        }

        System.out.println("=== POLIGONO DESPUES DE AGREGAR 3 VERTICES ===");
        System.out.println(poligono);

        System.out.println("=== POLIGONO CON SUS VÉRTICES ORDENADOS ===");
        poligono.ordenaVertices();
        System.out.println(poligono);
    }
}