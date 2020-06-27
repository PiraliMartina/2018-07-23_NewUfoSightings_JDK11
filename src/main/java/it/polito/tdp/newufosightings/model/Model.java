package it.polito.tdp.newufosightings.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.Arco;
import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	private NewUfoSightingsDAO dao;
	private Graph<State, DefaultWeightedEdge> grafo;
	private Map<String, State> mappaStati;

	public Model() {
		dao = new NewUfoSightingsDAO();
	}

	public List<String> getAllShapes(int anno) {
		return dao.getAllShapes(anno);
	}
	
	public void creaGrafo(int anno, String shape) {
		grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		mappaStati = new TreeMap<String, State>();
		dao.loadAllStates(mappaStati);
		
		//VERTICI
		Graphs.addAllVertices(grafo, mappaStati.values());
		
		//ARCHI
		for(Arco a: dao.getAllEdges(anno, shape, mappaStati)) {
			Graphs.addEdgeWithVertices(grafo, a.getS1(), a.getS2(), a.getPeso());
		}
	}
	
	public int numVertex() {
		return grafo.vertexSet().size();
	}
	
	public int numEdges() {
		return grafo.edgeSet().size();
	}
	
	public List<State> getAllVertex(){
		List<State> ls = new LinkedList<State>(grafo.vertexSet());
		ls.sort(new Comparator<State>() {

			@Override
			public int compare(State o1, State o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		return ls;
	}
	
	
	public int pesoAdiacenti(State s) {
		int tot = 0;
		for(State vicino: Graphs.neighborListOf(grafo, s)) {
			DefaultWeightedEdge e = grafo.getEdge(s, vicino);
			tot+=grafo.getEdgeWeight(e);
		}
		return tot;
	}
	
	public Map<String, Double>simula(int anno, String shape, int T, int alpha) {
		Simulator sim = new Simulator(anno, shape);
		sim.simula(T, alpha);
		return sim.getDEFCON();
	}
	
	
	
}
