public class Coordenada {
    private double x, y;
    private double magnitud;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
        this.magnitud = Math.sqrt(x*x+y*y);
    }

    // Metodo getter de x
    public double abcisa() {
        return x;
    }

    // Metodo getter de y
    public double ordenada() {
        return y;
    }

    public double magnitud(){
        return magnitud;
    }
    // Sobreescritura del método de la superclase objeto
    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}