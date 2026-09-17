public class Rectangulo {
    private Coordenada superiorIzq, inferiorDer;

    public Rectangulo() {
        superiorIzq = new Coordenada(0, 0);
        inferiorDer = new Coordenada(0, 0);
    }

    // Constructor original
    public Rectangulo(double xSupIzq, double ySupIzq,
                      double xInfDer, double yInfDer) {

        superiorIzq = new Coordenada(xSupIzq, ySupIzq);
        inferiorDer = new Coordenada(xInfDer, yInfDer);
    }

    public Rectangulo(Coordenada superiorIzq, Coordenada inferiorDer) {

        // Verificar que la primera coordenada esta arriba y a la izquierda de la segunda
        if (superiorIzq.abcisa() >= inferiorDer.abcisa()
                || superiorIzq.ordenada() <= inferiorDer.ordenada()) {

            throw new IllegalArgumentException(
                "La primer coordenada no se encuentra arriba y a la izquierda de la segunda"
            );
        }

        this.superiorIzq = superiorIzq;
        this.inferiorDer = inferiorDer;
    }

    // Metodo getter de la coordenada superior izquierda
    public Coordenada superiorIzquierda() {
        return superiorIzq;
    }

    // Metodo getter de la coordenada inferior derecha
    public Coordenada inferiorDerecha() {
        return inferiorDer;
    }

    // Sobreescritura del metodo toString()
    @Override
    public String toString() {
        return "Esquina superior izquierda: " + superiorIzq
                + "\tEsquina inferior derecha: " + inferiorDer + "\n";
    }
}
