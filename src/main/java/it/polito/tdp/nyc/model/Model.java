package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	private NYCDao dao;
	private List<String> provider;
	private Graph<City, DefaultWeightedEdge> grafo;
	private int durata;
	private Map<Integer,Integer> mappa;
	
	public Model() {
		super();
		this.dao = new NYCDao();
		this.provider = dao.getProviders();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public List<String> getProvider(){
		return this.provider;
	}
	
	public void creaGrafo(String p) {
		List<String> citta = dao.getCity(p);
		
		List<City> vertici = dao.getPosizione(p);
		Graphs.addAllVertices(this.grafo, vertici);
		for (City c1: vertici) {
			for (City c2: vertici) {
				if (!c1.equals(c2)) {
					LatLng posizione1 = new LatLng(c1.getLat(), c1.getLng());
					LatLng posizione2 = new LatLng(c2.getLat(), c2.getLng());
					double peso = LatLngTool.distance(posizione1, posizione2, LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, c1,c2, peso);
				}
				
				
			}
		}
	}
	public int getV() {
		return this.grafo.vertexSet().size();
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}
	public List<City> getQuartieri(String p){
		return dao.getPosizione(p);
	}
	
	public int getNhotspot(City c ,String p) {
		return dao.getNHotspot(p, c);
	}
	public List<CityAdiacente> getAdiacenti(City c ){
		List<City> adiacenti = Graphs.neighborListOf(this.grafo, c);
		List<CityAdiacente> lista = new ArrayList<>();
		Collections.sort(lista);
		for (City citta : adiacenti) {
			double peso =this.grafo.getEdgeWeight(this.grafo.getEdge(c, citta));
			lista.add(new CityAdiacente(citta.getCity(), peso));
		}
		return lista;
	}
	
	public void simula(String p, int n, City partenza) {
		Simulator s = new Simulator(grafo, p );
		s.initialize(n, partenza);
		s.run();
		this.durata = s.getTempo();
		this.mappa = s.getMappa();
	}
	
	public int getDurata() {
		return durata;
	}
	
	public Map<Integer, Integer> getMappa(){
		return this.mappa;
	}
	
	
	
	
}
