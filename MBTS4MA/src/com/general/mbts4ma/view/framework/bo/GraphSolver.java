package com.general.mbts4ma.view.framework.bo;

import java.util.LinkedList;
import java.util.List;
import com.github.eta.esg.EventSequenceGraph;
import com.github.eta.esg.Vertex;
import com.github.eta.testingtree.ETAPruningWithGreedyExpansionEsg4wscSolver;
import com.mxgraph.view.mxGraph;

public abstract class GraphSolver {

	private static List<List<Vertex>> cess = new LinkedList<List<Vertex>>();

	public static synchronized List<List<Vertex>> solve(mxGraph graph) throws Exception {
		cess.clear();

		EventSequenceGraph esg = GraphConverter.convertToESG(graph);

		// int numberOfNodes = esg.getNumberOfNodes();
		// int numberOfEdges = esg.getNumberOfEdges();

		ETAPruningWithGreedyExpansionEsg4wscSolver solver = new ETAPruningWithGreedyExpansionEsg4wscSolver(esg);

		solver.calculateCes();

		cess = solver.getCESs();

		return cess;
	}

	public static List<List<Vertex>> getCess() {
		return cess;
	}

	public static synchronized String getCESsAsString() {
		StringBuilder sb = new StringBuilder("");

		if (cess != null && !cess.isEmpty()) {
			int totalNumberOfEvents = 0;

			int numberOfCESs = cess.size();

			for (List<Vertex> ces : cess) {
				if (sb.length() > 0) {
					sb.append("\r\n\r\n");
				}

				int numberOfEvents = ces.size();

				totalNumberOfEvents += numberOfEvents;

				sb.append("Number of Events: ").append(numberOfEvents);
				sb.append("\r\n");
				sb.append(ces.toString().replace("[[", "[").replace("]]", "]"));
			}

			if (sb.length() > 0) {
				sb.append("\r\n\r\n");
			}

			sb.append("Number of CESs: ").append(numberOfCESs);
			sb.append("\r\n");
			sb.append("Total Number of Events: ").append(totalNumberOfEvents);
		}

		return sb.toString();
	}

	public static synchronized void printCESs(List<List<Vertex>> cess) {
		System.out.println("#########################");

		getCESsAsString();

		System.out.println("#########################");

	}

}
