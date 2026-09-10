public class TrianguloEq extends Figura{
   
   private double lado;

   public TrianguloEq(Coordenada[] vertices){
      super(
            new Coordenada(
                (vertices[0].abcisa() + vertices[1].abcisa() + vertices[2].abcisa()) / 3.0,
                (vertices[0].ordenada() + vertices[1].ordenada() + vertices[2].ordenada()) / 3.0
            ),
            3,
            vertices
        );
      lado=Math.sqrt(Math.pow(vertices[1].abcisa() - vertices[0].abcisa(),2.0)+
         Math.pow(vertices[1].ordenada() - vertices[0].ordenada(),2.0));
   }

   @Override
   public double area(){
      return (Math.sqrt(3)/4)*(lado*lado);
   }
}