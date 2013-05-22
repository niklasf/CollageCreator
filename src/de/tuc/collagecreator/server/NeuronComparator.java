package de.tuc.collagecreator.server;

import java.util.Comparator;

/**
 * Hilfsklasse zum Sortieren der Neuronen.
 * 
 * @author Jens Drieseberg
 */
class NeuronComparator implements Comparator<Neuron> {
	public int compare(Neuron n1, Neuron n2) {
		if (n1.getDistance() > n2.getDistance())
			return 1;
		else if (n1.getDistance() < n2.getDistance())
			return -1;
		return 0;
	}
}