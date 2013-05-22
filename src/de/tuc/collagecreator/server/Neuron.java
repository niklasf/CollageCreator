package de.tuc.collagecreator.server;

import java.awt.geom.Point2D;

/**
 * Hilfsklasse zum Speichern der Neuronen.
 * 
 * @author Jens Drieseberg
 */
class Neuron {
	private int id;
	private Point2D pos;
	private double distance;

	public Neuron(int id) {
		this.id = id;
	}

	int getId() {
		return id;
	};

	Point2D getPos() {
		return pos;
	}

	void setPos(Point2D p) {
		pos = p;
	}

	double getDistance() {
		return distance;
	}

	void setDistanceTo(Point2D p) {
		distance = pos.distanceSq(p);
	}
}