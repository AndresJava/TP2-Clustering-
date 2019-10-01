package controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import modelo.Modelo;
import vista.Vista;
import javax.swing.border.MatteBorder;

public class PanelControl extends JPanel implements ActionListener
{
	private Modelo modelo;
	private Vista vista;
	
	private JTextField nombre, clusters;;
	private JCheckBox visibilidad;
	private JButton centrarGrafo, eliminarGrafo, clustering;
	
	private ArrayList<MapMarkerDot> coordenadas;
	private MapPolygonImpl poligono;
	
	public PanelControl(String nombre, Modelo modelo, Vista vista) 
	{
		this.modelo = modelo;
		this.vista = vista;
		iniComponentes(nombre);
		dibujarse();
	}

	private void iniComponentes(String nombre) 
	{
		this.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
		this.setBackground(new Color(62, 54, 54));
		this.setLayout(null);
		
		visibilidad = new JCheckBox();
		visibilidad.setBackground(null);
		visibilidad.setForeground(Color.WHITE);
		visibilidad.setBounds(6, 7, 21, 23);
		visibilidad.setToolTipText(nombre);
		visibilidad.setSelected(true);
		visibilidad.setFocusable(false);
		this.add(visibilidad);
		
		this.nombre = new JTextField(nombre);
		this.nombre.setBounds(28, 7, 78, 23);
		this.nombre.setBackground(null);
		this.nombre.setForeground(Color.white);
		this.nombre.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		this.nombre.setBorder(null);
		this.nombre.setEditable(false);
		this.add(this.nombre);
	
		centrarGrafo = new JButton();
		centrarGrafo.setIcon(new ImageIcon(PanelControl.class.getResource("/iconos/iconCentrar.png")));
		centrarGrafo.setToolTipText("Centrar");
		centrarGrafo.setBackground(null);
		centrarGrafo.setBounds(120, 7, 29, 23);
		centrarGrafo.setFocusable(false);
		centrarGrafo.setBorderPainted(false);
		this.add(centrarGrafo);
		
		eliminarGrafo = new JButton();
		eliminarGrafo.setIcon(new ImageIcon(PanelControl.class.getResource("/iconos/iconCesto.png")));
		eliminarGrafo.setToolTipText("Eliminar");
		eliminarGrafo.setBackground(null);
		eliminarGrafo.setBounds(150, 7, 29, 23);
		eliminarGrafo.setFocusable(false);
		eliminarGrafo.setBorderPainted(false);
		this.add(eliminarGrafo);
		
		clusters = new JTextField();
		clusters.setBounds(181, 7, 38, 23);
		this.add(clusters);
		
		clustering = new JButton();
		clustering.setIcon(new ImageIcon(PanelControl.class.getResource("/iconos/iconClustering.png")));
		clustering.setToolTipText("Clustering");
		clustering.setBackground(null);
		clustering.setBounds(221, 7, 29, 23);
		clustering.setFocusable(false);
		clustering.setBorderPainted(false);
		this.add(clustering);
		
		MouseAdapter efectoHover = new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				JButton boton = (JButton) e.getSource();
			
				boton.setBackground(new Color(102, 94, 94));
			}
		
			@Override
			public void mouseExited(MouseEvent e) 
			{
				JButton boton = (JButton) e.getSource();
				
				boton.setBackground(null);
			}
		};
		centrarGrafo.addMouseListener(efectoHover);
		eliminarGrafo.addMouseListener(efectoHover);
		clustering.addMouseListener(efectoHover);
		
	
		visibilidad.addActionListener(this);
		centrarGrafo.addActionListener(this);
		eliminarGrafo.addActionListener(this);
		clustering.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == visibilidad)
			activarODesactivar();
		
		if(e.getSource() == centrarGrafo)
			centrarGrafo();
		
		if(e.getSource() == eliminarGrafo)
			eliminarGrafo();
		
//		if(e.getSource() == clustering) POR AHORA ESTE NO HACE NADA
		
	}
	
	private void activarODesactivar() 
	{
		if(visibilidad.isSelected() == true)
		{
			for(int i=0; i < coordenadas.size(); i++)
				coordenadas.get(i).setVisible(true);
			
			poligono.setVisible(true);
		}
		else 
		{
			for(int i=0; i < coordenadas.size(); i++)
				coordenadas.get(i).setVisible(false);
			
			poligono.setVisible(false);
		}
		
		vista.mapa.updateUI();
	}
	
	private void eliminarGrafo() 
	{
		for(int i=0; i < coordenadas.size(); i++) 
			vista.mapa.removeMapMarker(coordenadas.get(i));

		vista.mapa.removeMapPolygon(poligono);
		vista.mapa.updateUI();
		vista.panelDeControles.eliminar(this);
		vista.panelDeControles.updateUI();
	}
	
	private void centrarGrafo() 
	{
		vista.mapa.setDisplayPosition(modelo.coordenadas().get(0), 13);
	}
	
	private void dibujarse() 
	{
		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		Color color = new Color(R, G, B);
		
		coordenadas = new ArrayList<MapMarkerDot>();
		
		MapMarkerDot coordenada;
		for(Coordinate coor : modelo.coordenadas()) 
		{
			coordenada = new MapMarkerDot(coor);
			coordenada.setBackColor(color);
			coordenadas.add(coordenada);
			vista.mapa.addMapMarker(coordenada);
		}
		
		poligono = new MapPolygonImpl(modelo.coordenadas());
		poligono.setColor(color);
		vista.mapa.addMapPolygon(poligono);
		
		vista.mapa.setDisplayPosition(modelo.coordenadas().get(0), 13);
		
	}
}
