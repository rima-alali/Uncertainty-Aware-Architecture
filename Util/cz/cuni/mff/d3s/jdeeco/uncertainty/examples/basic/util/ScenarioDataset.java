package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util;

import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;

public class ScenarioDataset {

	protected static final LookupTable driverSpeed = new LookupTable();
	protected static final LookupTable torques = new LookupTable();
	protected static final LookupTable accelerationMin = new LookupTable();
	protected static final LookupTable accelerationMax = new LookupTable();
	protected static final LookupTable routeSlops = new LookupTable();
	protected static final double mass = 1000;
	protected static final double g = 9.80665;

	public ScenarioDataset() {
		driverBehaviour();
		torques();
		routeSlops();
	}

	public void accelerationMin() {
		accelerationMin.put(0.0, -6.0);
		accelerationMin.put(35.0, -5.0);
		accelerationMin.put(51.0, -3.0);
		accelerationMin.put(200.0, 0.0);
	}

	public void accelerationMax() {
		accelerationMax.put(0.0, 4.0);
		accelerationMax.put(35.0, 3.0);
		accelerationMax.put(51.0, 0.0);
		accelerationMax.put(200.0, 0.0);
	}

	public void driverBehaviour() {
		driverSpeed.put(0.0, 90.0);
		driverSpeed.put(1000.0, 90.0);
		driverSpeed.put(2000.0, 90.0);
		driverSpeed.put(3000.0, 90.0);
		driverSpeed.put(4000.0, 90.0);
		driverSpeed.put(5000.0, 90.0);
		driverSpeed.put(6000.0, 90.0);
		driverSpeed.put(7000.0, 90.0);
		driverSpeed.put(8000.0, 90.0);// .... 150.0
		driverSpeed.put(9000.0, 90.0);
		driverSpeed.put(10000.0, 90.0);
		driverSpeed.put(11000.0, 90.0);
		driverSpeed.put(12000.0, 90.0);// ... 150.0
		driverSpeed.put(13000.0, 90.0);
		driverSpeed.put(14000.0, 90.0);
		driverSpeed.put(10000000.0, 90.0);
	}

	public void routeSlops() {
		routeSlops.put(0.0, 0.0);
		routeSlops.put(1000.0, 0.0);
		routeSlops.put(2000.0, 0.0);
		routeSlops.put(3000.0, 0.0);
		routeSlops.put(4000.0, 0.0);// Math.PI/12
		routeSlops.put(5000.0, 0.0);// -Math.PI/12
		routeSlops.put(6000.0, 0.0);
		routeSlops.put(7000.0, 0.0);
		routeSlops.put(8000.0, 0.0);
		routeSlops.put(9000.0, 0.0);
		routeSlops.put(10000.0, 0.0);
		routeSlops.put(11000.0, 0.0);
		routeSlops.put(12000.0, 0.0);// Math.PI/12
		routeSlops.put(13000.0, 0.0);
		routeSlops.put(14000.0, 0.0);
		routeSlops.put(15000.0, 0.0);
		routeSlops.put(10000000.0, 0.0);
	}

	public void torques() {
		torques.put(-10000000.0, 180.0);
		torques.put(0.0, 180.0);
		torques.put(8.0, 180.0);
		torques.put(20.0, 180.0);
		torques.put(28.0, 170.0);
		torques.put(40.0, 170.0);
		torques.put(60.0, 150.0);
		torques.put(80.0, 115.0);
		torques.put(100.0, 97.0);
		torques.put(120.0, 80.0);
		torques.put(140.0, 70.0);
		torques.put(160.0, 60.0);
		torques.put(180.0, 50.0);
		torques.put(200.0, 40.0);
		torques.put(220.0, 0.0); // 100000.0
		torques.put(1000000.0, 0.0);
	}

	public Double getDriverBehavior(Double pos) {
		return driverSpeed.get(pos);
	}

	public Double getTorques(Double speed) {
		return torques.get(speed);
	}

	public LookupTable getTorques() {
		return torques;
	}

	public Double getRouteSlops(Double pos) {
		return routeSlops.get(pos);
	}

//	public LookupTable getRouteSlops(State pos, LookupTable t, int w, int future) {
//		LookupTable rs = new LookupTable();
//		Double slope = getRouteSlops(pos.getData());
//		if (slope != null)
//			for (Double i = pos.getData(); i < pos.getData() + 20; i++) {
//				rs.put(pos.getData(), slope);
//			}
//		else {
//			TreeMap<Double, Double> tree = new TreeMap<>();
//			for (Double p : t.keys()) {
//				tree.put(p, t.get(p));
//			}
//			slope = StatisticalOperators.flr(tree, pos.getData(), w, future).getMean();
//			rs.put(key, value);
//		}
//		return rs;
//	}

	public LookupTable getRouteSlops() {
		return routeSlops;
	}

	public double getG() {
		return g;
	}

	public double getMass() {
		return mass;
	}
}
