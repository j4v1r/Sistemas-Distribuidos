import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Comparator;

public class PoligonoIrreg {

    private ArrayList<Coordenada> vertices;

    public PoligonoIrreg() {
        vertices = new ArrayList<Coordenada>();
    }

    public void anadeVertice(Coordenada nuevaCoordenada) {
        vertices.add(nuevaCoordenada);
    }

    public void ordenaVertices(){
        this.vertices.sort(Comparator.comparingDouble(Coordenada::magnitud));
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("PoligonoIrreg con ")
          .append(vertices.size())
          .append(" vertices:\n");

        for (Coordenada vertice : vertices) {
            sb.append("Vertice: ")
              .append(vertice)
              .append("\n");
        }

        return sb.toString();
    }
}