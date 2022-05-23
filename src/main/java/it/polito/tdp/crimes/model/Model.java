package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model 
{
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> best;
	
	
	public Model()
	{
		this.dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese)
	{
		// creazione grafo
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiunta vertici
		Graphs.addAllVertices(grafo, dao.getVertici(categoria, mese));
		
		// aggiunta archi
		for(Adiacenza a: dao.getArchi(categoria, mese))
		{
			Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(), a.getPeso());
		}
		
		// System.out.println("Grafo creato!");
		// System.out.println("Num vertici: " + this.grafo.vertexSet().size());
		// System.out.println("Num archi: " + this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio()
	{
		// scorro gli archi del grafo e calcolo il peso medio
		double pesoTot = 0;
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet())
		{
			pesoTot += this.grafo.getEdgeWeight(e);
		}
		
		double avg = pesoTot / this.grafo.edgeSet().size();
		// System.out.println("peso medio: " + avg);
		
		// ri-scorro tutti gli archi, prendendo quelli con peso maggiore di avg
		List<Adiacenza> res = new ArrayList<Adiacenza>();
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet())
		{
			if(this.grafo.getEdgeWeight(e) > avg)
			{
				res.add(new Adiacenza(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), (int)grafo.getEdgeWeight(e)));
			}
		}
		
		return res;
	}
	
	public List<String> calcolaPercorso(String sorgente, String destinazione)
	{
		best = new LinkedList<String>();
		
		List<String> parziale = new LinkedList<String>();
		parziale.add(sorgente);
		
		cerca(parziale, destinazione);
		
		return best;
	}

	private void cerca(List<String> parziale, String destinazione) 
	{
		// CONDIZIONE DI TERMINAZIONE
		// --> quando arrivo a destinazione
		if(parziale.get(parziale.size()-1).equals(destinazione))
		{
			// è la soluzione migliore?
			if(parziale.size() > best.size())
			{
				best = new LinkedList<String>(parziale);
			}
			
			// in ogni caso, esco dalla ricorsione
			return;
		}
		
		// PASSO RICORSIVO
		// scorro i vicini dell'ultimo inserito e provo le varie "strade"
		for(String vicino: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1)))
		{
			// se parziale non contiene già il vertice
			if(!parziale.contains(vicino))
			{
				// aggiungo
				parziale.add(vicino);
				// lancio ricorsione
				cerca(parziale, destinazione);
				// backtracking
				parziale.remove(parziale.size()-1);
			}
			
			// altrimenti ricorsione infinita, con cammini ciclici
		}
		
	}

	public List<String> getCategorieReato()
	{
		return dao.getCategorieReato();
	}

	public int nVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public List<Adiacenza> getArchi()
	{
		List<Adiacenza> archi = new ArrayList<Adiacenza>();
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) 
		{
			archi.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), 
						(int) this.grafo.getEdgeWeight(e)));
		}
		return archi;
	}
}
