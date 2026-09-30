//PROYECTO02
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleGui2 extends JFrame{

	
	public static void main(String[] args){
 		SimpleGui2 gui = new SimpleGui2();
 		gui.setVisible(true);
	}

 	public SimpleGui2(){
		setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Panel p = new Panel();
		add(p);

		p.movimiento();
	}

	private class Panel extends JPanel {

		private int posX=0;
		private int posY=0;

		public void movimiento(){
			Timer timer = new Timer(16, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					posX+=2;
					posY+=1;

					repaint();
				}
			});
			timer.start();
		}
 	
 		@Override
 		public void paintComponent(Graphics g){
 			g.setColor(Color.blue);

			Polygon poligono=new Polygon();
 			poligono.addPoint(0, 0);
 			poligono.addPoint(100, 0);
 			poligono.addPoint(50, 100);
 			g.drawPolygon(poligono);
 		}
 	}
 }
