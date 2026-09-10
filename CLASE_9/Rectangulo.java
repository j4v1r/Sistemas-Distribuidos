public class Rectangulo extends Figura{
   
   private double base, altura; 

   public Rectangulo(Coordenada[] vertices){
      super(
         new Coordenada(
            (vertices[0].abcisa() + vertices[2].abcisa()) / 2,
            (vertices[0].ordenada() + vertices[2].ordenada()) / 2
         ),
         4,
         vertices
      );

      base=Math.sqrt(Math.pow(vertices[1].abcisa() - vertices[0].abcisa(),2.0)+
         Math.pow(vertices[1].ordenada() - vertices[0].ordenada(),2.0));
      altura=Math.sqrt(Math.pow(vertices[2].abcisa() - vertices[0].abcisa(),2.0)+
         Math.pow(vertices[2].ordenada() - vertices[0].ordenada(),2.0));
   }

   @Override
   public double area(){
      return base*altura;
   }
}