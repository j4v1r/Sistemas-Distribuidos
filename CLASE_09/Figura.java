public abstract class Figura implements Desplazable{
   
   public Coordenada centro;
   public Coordenada[] vertices;

   public Figura(Coordenada ctro, int numV, Coordenada[] vers){
      vertices = new Coordenada[numV];
      this.centro=ctro;
      this.vertices=vers;
   }

   public abstract double area();

   @Override
   public void desplazar(double dx, double dy) {
      
      // Desplazar la coordenada del centro
      centro.setX(centro.abcisa() + dx);
      centro.setY(centro.ordenada() + dy);

      // Desplazar cada vértice en el arreglo de Coordenadas
      for (Coordenada v : vertices) {
         if (v != null) {
            v.setX(v.abcisa() + dx);
            v.setY(v.ordenada() + dy);
         }
      }
   }

   public void mostrarVertices() {
      System.out.println("Vértices:");
      for (Coordenada v : vertices) {
         System.out.println("  "+v);
      }
   }
}