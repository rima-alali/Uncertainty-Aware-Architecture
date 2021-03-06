package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util;

import java.util.ArrayList;

public class LookupTable {

	private static class Tuple {
		public double key;
		public double value;

		public Tuple(double key, double value) {
			this.key = key;
			this.value = value;
		}
	}

	// ----------------------------------------------------------
	private ArrayList<Tuple> tuple = new ArrayList<Tuple>();

	public void put(double key, double value) {
		tuple.add(new Tuple(key, value));
	}

	public double get(double key) {
		Tuple max = new Tuple(-1, -1);
		Tuple min = new Tuple(-1, -1);

		for (int i = 1; i < tuple.size(); i++) {
			if (key < tuple.get(i).key) {
				max = tuple.get(i);
				min = tuple.get(i - 1);
				break;
			} else if (key == tuple.get(i).key) {
				max = tuple.get(i);
				min = tuple.get(i);
				break;
			}
		}
		return getFunctionValue(min, max, key);
	}

	private double getFunctionValue(Tuple p1, Tuple p2, double key) {
		if (p1.equals(p2))
			return p1.value;
		double a = (p2.value - p1.value) / (p2.key - p1.key);
		double b = p1.value - a * p1.key;
		double value = a * key + b;
		return value;
	}

	public ArrayList<Double> keys() {
		ArrayList<Double> k = new ArrayList<>();
		for (int i = 0; i < tuple.size(); i++) {
			k.add(tuple.get(i).key);
		}
		return k;
	}

	public ArrayList<Double> values() {
		ArrayList<Double> k = new ArrayList<>();
		for (int i = 0; i < tuple.size(); i++) {
			k.add(tuple.get(i).value);
		}
		return k;
	}
}