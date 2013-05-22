/**
 * Batch neural gas 2005
 * Marie Cottrell, Barbara Hammer, Alexander Hasenfuß, Thomas Villmann
 * http://www2.in.tu-clausthal.de/~hammer/papers/postscripts/batchng.pdf
 */

package de.tuc.collagecreator.server;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * Batch Neural Gas Implementierung.
 * 
 * @author Jens Drieseberg
 */
public class NeuralGas {
	BufferedImage image;
	int numNeurons;
	Neuron[] neurons;

	NeuralGas(int numNeurons, BufferedImage image) {
		this.image = image;
		this.numNeurons = numNeurons;

		startNG();
	}

	public Point2D[] getPositions() {
		Point2D[] pos = new Point2D[numNeurons];

		for (int i = 0; i < numNeurons; i++) {
			pos[i] = new Point2D.Double(neurons[i].getPos().getX(), neurons[i]
					.getPos().getY());
		}

		return pos;
	}

	private Vector<Point2D> getDataPoints() {
		Vector<Point2D> data = new Vector<Point2D>();
		for (int j = 0; j < image.getWidth(); j++) {
			for (int i = 0; i < image.getHeight(); i++) {
				// get the actual pixel
				int pixel = image.getRGB(j, i);

				// check the color
				if (pixel != 0xFFFFFFFF && ((pixel >> 24) & 0x000000FF) > 0)
					data.add(new Point2D.Double(j, i));
			}
		}

		return data;
	}

	private void initNeurons() {
		// generate initial positions of neurons
		Random random = new Random();
		for (int i = 0; i < neurons.length; i++) {
			// set the actual neuron
			neurons[i] = new Neuron(i);

			// init pos with random value
			neurons[i].setPos(new Point2D.Double(random.nextDouble()
					* image.getWidth() - 1, random.nextDouble()
					* image.getHeight() - 1));
		}
	}

	private void startNG() {
		int numIter = 100;
		double eps = 0.01;

		// quantisation error - break condition
		double quantE = Double.MAX_VALUE;

		// training values
		double lambda_i = numNeurons * 0.5; // initial decay constant
		double lambda_f = 0.01; // lambda update coefficient

		// resize the number of neurons
		neurons = new Neuron[numNeurons];

		// get the datapoints
		Vector<Point2D> dataPoints = getDataPoints();

		// get the number of data points
		int numDP = dataPoints.size();

		// initialize neurons
		initNeurons();

		// rank matrix
		int[][] rank = new int[numNeurons][numDP];

		// run NG
		for (int i = 0; i < numIter; i++) {
			if ((i % 10) == 0)
				System.out.println(i);

			// actual quantisation error
			double actQuant = 0;

			// compute distance between actData and each neuron
			for (int j = 0; j < numDP; j++) {
				// actual datapoint
				Point2D p = (Point2D) dataPoints.elementAt(j);

				for (int n = 0; n < numNeurons; n++)
					neurons[n].setDistanceTo(p);

				// sort neurons by distance from actual distance
				Arrays.sort(neurons, new NeuronComparator());

				// set the rank for each neuron
				for (int n = 0; n < numNeurons; n++)
					rank[neurons[n].getId()][j] = n;

				// add error
				actQuant += neurons[0].getDistance();
			}

			// break if actual ( error / prevError ) < eps
			double relE = 1 - (actQuant / quantE);
			if (relE >= 0 && relE < eps) {
				System.out.println("number of iterations: " + i);
				break;
			} else
				quantE = actQuant;

			// update the learning rate
			double lambda_t_inv = 1 / (lambda_i * Math.pow(lambda_f / lambda_i,
					i / (double) numIter));

			// update neurons
			for (int n = 0; n < numNeurons; n++) {
				Neuron neuron = neurons[n];

				// sum up positions
				double sum = 0;
				Point2D actPos = new Point2D.Double(0, 0);
				for (int j = 0; j < numDP; j++) {
					double h = Math
							.exp(-rank[neuron.getId()][j] * lambda_t_inv);
					double x = ((Point2D) dataPoints.elementAt(j)).getX() * h;
					double y = ((Point2D) dataPoints.elementAt(j)).getY() * h;
					actPos.setLocation(actPos.getX() + x, actPos.getY() + y);
					sum += h;
				}
				actPos.setLocation(actPos.getX() / sum, actPos.getY() / sum);

				// set the position
				neuron.setPos(actPos);
			}
		}
	}
}
