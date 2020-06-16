package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util;

import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;

public class ScenarioDatasetRouteDriver {
	
	protected static final LookupTable driverSpeed = new LookupTable();
	protected static final LookupTable torques = new LookupTable();
	protected static final LookupTable accelerationMin = new LookupTable();
	protected static final LookupTable accelerationMax = new LookupTable();
	protected static final LookupTable routeSlops = new LookupTable();
	protected static final double mass = 1000;
	protected static final double g = 9.80665;

	
	public ScenarioDatasetRouteDriver() {
		driverBehaviour();
		torques();
		routeSlops();
	}
	
	public void accelerationMin(){
		accelerationMin.put(0.0, -6.0);
		accelerationMin.put(35.0, -5.0);
		accelerationMin.put(51.0, -3.0);
		accelerationMin.put(200.0, 0.0);
	}
	
	public void accelerationMax(){
		accelerationMax.put(0.0, 4.0);
		accelerationMax.put(35.0, 3.0);
		accelerationMax.put(51.0, 0.0);
		accelerationMax.put(200.0, 0.0);
	}
	
	public void driverBehaviour(){
		driverSpeed.put(0.0, 90.0);
		driverSpeed.put(50.0, 90.0);
		driverSpeed.put(100.0, 90.0);
		driverSpeed.put(150.0, 90.0);
		driverSpeed.put(200.0, 90.0);
		driverSpeed.put(250.0, 90.0);
		driverSpeed.put(300.0, 90.0);
		driverSpeed.put(350.0, 90.0);
		driverSpeed.put(400.0, 90.0);// Math.PI/12
		driverSpeed.put(450.0, 90.0);// Math.PI/12
		driverSpeed.put(500.0, 90.0);
		driverSpeed.put(550.0, 90.0);
		driverSpeed.put(600.0, 90.0);
		driverSpeed.put(650.0, 90.0);
		driverSpeed.put(700.0, 90.0);
		driverSpeed.put(750.0, 90.0);
		driverSpeed.put(800.0, 90.0);
		driverSpeed.put(850.0, 90.0);
		driverSpeed.put(900.0, 90.0);
		driverSpeed.put(950.0, 90.0);
		driverSpeed.put(1000.0, 90.0);
		driverSpeed.put(1050.0, 90.0);
		driverSpeed.put(1100.0, 90.0);
		driverSpeed.put(1150.0, 90.0);
		driverSpeed.put(1200.0, 90.0);
		driverSpeed.put(1250.0, 90.0);
		driverSpeed.put(1300.0, 90.0);
		driverSpeed.put(1350.0, 90.0);
		driverSpeed.put(1400.0, 90.0);
		driverSpeed.put(1450.0, 90.0);
		driverSpeed.put(1500.0, 90.0);
		driverSpeed.put(1550.0, 50.0);
		driverSpeed.put(1600.0, 90.0);
		driverSpeed.put(1650.0, 90.0);
		driverSpeed.put(1700.0, 90.0);
		driverSpeed.put(1750.0, 100.0);
		driverSpeed.put(1800.0, 110.0);
		driverSpeed.put(1850.0, 100.0);
		driverSpeed.put(1900.0, 90.0);
		driverSpeed.put(1950.0, 90.0);
		driverSpeed.put(2000.0, 90.0);
		driverSpeed.put(2050.0, 90.0);
		driverSpeed.put(2100.0, 90.0);
		driverSpeed.put(2150.0, 50.0);
		driverSpeed.put(2200.0, 90.0);
		driverSpeed.put(2250.0, 90.0);
		driverSpeed.put(2300.0, 90.0);
		driverSpeed.put(2350.0, 90.0);
		driverSpeed.put(2400.0, 90.0);
		driverSpeed.put(2450.0, 50.0);
		driverSpeed.put(2500.0, 90.0);
		driverSpeed.put(2550.0, 90.0);
		driverSpeed.put(2600.0, 90.0);
		driverSpeed.put(2650.0, 90.0);
		driverSpeed.put(2700.0, 90.0);
		driverSpeed.put(2750.0, 90.0);
		driverSpeed.put(2800.0, 90.0);
		driverSpeed.put(2850.0, 90.0);
		driverSpeed.put(2900.0, 90.0);
		driverSpeed.put(2950.0, 90.0);
		driverSpeed.put(3000.0, 90.0);
		driverSpeed.put(3050.0, 90.0);
		driverSpeed.put(3100.0, 90.0);
		driverSpeed.put(3150.0, 90.0);
		driverSpeed.put(3200.0, 90.0);
		driverSpeed.put(3250.0, 90.0);
		driverSpeed.put(3300.0, 90.0);
		driverSpeed.put(3350.0, 90.0);
		driverSpeed.put(3400.0, 90.0);// Math.PI/12
		driverSpeed.put(3450.0, 90.0);// Math.PI/12
		driverSpeed.put(3500.0, 90.0);
		driverSpeed.put(3550.0, 90.0);
		driverSpeed.put(3600.0, 90.0);
		driverSpeed.put(3650.0, 90.0);
		driverSpeed.put(3700.0, 80.0);
		driverSpeed.put(3750.0, 90.0);
		driverSpeed.put(3800.0, 90.0);
		driverSpeed.put(3850.0, 90.0);
		driverSpeed.put(3900.0, 90.0);
		driverSpeed.put(3950.0, 90.0);
		driverSpeed.put(4000.0, 90.0);
		driverSpeed.put(4050.0, 100.0);
		driverSpeed.put(4100.0, 90.0);
		driverSpeed.put(4150.0, 90.0);
		driverSpeed.put(4200.0, 90.0);
		driverSpeed.put(4250.0, 90.0);
		driverSpeed.put(4300.0, 90.0);
		driverSpeed.put(4350.0, 80.0);
		driverSpeed.put(4400.0, 90.0);
		driverSpeed.put(4450.0, 90.0);
		driverSpeed.put(4500.0, 90.0);
		driverSpeed.put(4550.0, 50.0);
		driverSpeed.put(4600.0, 90.0);
		driverSpeed.put(4650.0, 90.0);
		driverSpeed.put(4700.0, 90.0);
		driverSpeed.put(4750.0, 90.0);
		driverSpeed.put(4800.0, 90.0);
		driverSpeed.put(4850.0, 90.0);
		driverSpeed.put(4900.0, 90.0);
		driverSpeed.put(4950.0, 90.0);
		driverSpeed.put(5000.0, 90.0);
		driverSpeed.put(5050.0, 90.0);
		driverSpeed.put(5100.0, 100.0);
		driverSpeed.put(5150.0, 50.0);
		driverSpeed.put(5200.0, 90.0);
		driverSpeed.put(5250.0, 90.0);
		driverSpeed.put(5300.0, 90.0);
		driverSpeed.put(5350.0, 90.0);
		driverSpeed.put(5400.0, 90.0);
		driverSpeed.put(5450.0, 90.0);
		driverSpeed.put(5500.0, 90.0);
		driverSpeed.put(5550.0, 90.0);
		driverSpeed.put(5600.0, 90.0);
		driverSpeed.put(5650.0, 90.0);
		driverSpeed.put(5700.0, 90.0);
		driverSpeed.put(5750.0, 90.0);
		driverSpeed.put(5800.0, 100.0);
		driverSpeed.put(5850.0, 90.0);
		driverSpeed.put(5900.0, 90.0);
		driverSpeed.put(5950.0, 90.0);
		driverSpeed.put(6000.0, 90.0);
		driverSpeed.put(6050.0, 90.0);
		driverSpeed.put(6100.0, 90.0);
		driverSpeed.put(6150.0, 80.0);
		driverSpeed.put(6200.0, 90.0);
		driverSpeed.put(6250.0, 90.0);
		driverSpeed.put(6300.0, 90.0);
		driverSpeed.put(6350.0, 90.0);
		driverSpeed.put(6400.0, 90.0);
		driverSpeed.put(6450.0, 90.0);
		driverSpeed.put(6500.0, 90.0);
		driverSpeed.put(6550.0, 100.0);
		driverSpeed.put(6600.0, 110.0);
		driverSpeed.put(6650.0, 100.0);
		driverSpeed.put(6700.0, 90.0);
		driverSpeed.put(6750.0, 90.0);
		driverSpeed.put(6800.0, 90.0);
		driverSpeed.put(6850.0, 90.0);
		driverSpeed.put(6900.0, 90.0);
		driverSpeed.put(6950.0, 90.0);
		driverSpeed.put(7000.0, 90.0);
		driverSpeed.put(15000.0, 90.0);	
	}
	
	public void routeSlops(){
		routeSlops.put(0.0, 0.0);
		routeSlops.put(50.0, 0.0);
		routeSlops.put(100.0, 0.0);
		routeSlops.put(150.0, 0.0);
		routeSlops.put(200.0, 0.0);
		routeSlops.put(250.0, 0.0);
		routeSlops.put(300.0, 0.0);
		routeSlops.put(350.0, 0.0);
		routeSlops.put(400.0, 0.0);
		routeSlops.put(450.0, 0.0);
		routeSlops.put(500.0, 0.0);
		routeSlops.put(550.0, 0.0);
		routeSlops.put(600.0, 0.0);
		routeSlops.put(650.0, 0.0);
		routeSlops.put(700.0, 0.0);
		routeSlops.put(750.0, 0.0);
		routeSlops.put(800.0, 0.0);
		routeSlops.put(850.0, 0.0);
		routeSlops.put(900.0, 0.0);
		routeSlops.put(950.0, 0.0);
		routeSlops.put(1000.0, 0.0);
		routeSlops.put(1050.0, 0.0);
		routeSlops.put(1100.0, 0.0);
		routeSlops.put(1150.0, 0.0);
		routeSlops.put(1200.0, 0.0);
		routeSlops.put(1250.0, 0.0);
		routeSlops.put(1300.0, 0.0);
		routeSlops.put(1350.0, Math.PI/36); //Math.PI/36
		routeSlops.put(1400.0, -Math.PI/36);
		routeSlops.put(1450.0, 0.0);
		routeSlops.put(1500.0, 0.0);
		routeSlops.put(1550.0, 0.0);
		routeSlops.put(1600.0, 0.0);
		routeSlops.put(1650.0, 0.0);
		routeSlops.put(1700.0, 0.0);
		routeSlops.put(1750.0, 0.0);
		routeSlops.put(1800.0, 0.0);
		routeSlops.put(1850.0, 0.0);
		routeSlops.put(1900.0, 0.0);
		routeSlops.put(1950.0, 0.0);
		routeSlops.put(2000.0, 0.0);
		routeSlops.put(2050.0, 0.0);
		routeSlops.put(2100.0, 0.0);
		routeSlops.put(2150.0, 0.0);
		routeSlops.put(2200.0, 0.0);
		routeSlops.put(2250.0, 0.0);
		routeSlops.put(2300.0, 0.0);
		routeSlops.put(2350.0, 0.0);
		routeSlops.put(2400.0, 0.0);
		routeSlops.put(2450.0, 0.0);
		routeSlops.put(2500.0, 0.0);
		routeSlops.put(2550.0, 0.0);
		routeSlops.put(2600.0, 0.0);
		routeSlops.put(2650.0, 0.0);
		routeSlops.put(2700.0, 0.0);
		routeSlops.put(2750.0, 0.0);
		routeSlops.put(2800.0, 0.0);
		routeSlops.put(2850.0, 0.0);
		routeSlops.put(2900.0, 0.0);
		routeSlops.put(2950.0, 0.0);
		routeSlops.put(3000.0, 0.0);
		routeSlops.put(3050.0, 0.0);
		routeSlops.put(3100.0, 0.0);
		routeSlops.put(3150.0, 0.0);
		routeSlops.put(3200.0, 0.0);
		routeSlops.put(3250.0, 0.0);
		routeSlops.put(3300.0, 0.0);
		routeSlops.put(3350.0, Math.PI/36);
		routeSlops.put(3400.0, 0.0);
		routeSlops.put(3450.0, 0.0);
		routeSlops.put(3500.0, 0.0);
		routeSlops.put(3550.0, 0.0);
		routeSlops.put(3600.0, 0.0);
		routeSlops.put(3650.0, 0.0);
		routeSlops.put(3700.0, -Math.PI/36);
		routeSlops.put(3750.0, 0.0);
		routeSlops.put(3800.0, 0.0);
		routeSlops.put(3850.0, 0.0);
		routeSlops.put(3900.0, 0.0);
		routeSlops.put(3950.0, 0.0);
		routeSlops.put(4000.0, 0.0);
		routeSlops.put(4050.0, 0.0);
		routeSlops.put(4100.0, 0.0);
		routeSlops.put(4150.0, 0.0);
		routeSlops.put(4200.0, 0.0);
		routeSlops.put(4250.0, 0.0);
		routeSlops.put(4300.0, 0.0);
		routeSlops.put(4350.0, 0.0);
		routeSlops.put(4400.0, 0.0);
		routeSlops.put(4450.0, 0.0);
		routeSlops.put(4500.0, 0.0);
		routeSlops.put(4550.0, 0.0);
		routeSlops.put(4600.0, 0.0);
		routeSlops.put(4650.0, 0.0);
		routeSlops.put(4700.0, 0.0);
		routeSlops.put(4750.0, Math.PI/36);
		routeSlops.put(4800.0, 0.0);
		routeSlops.put(4850.0, 0.0);
		routeSlops.put(4900.0, 0.0);
		routeSlops.put(4950.0, 0.0);
		routeSlops.put(5000.0, 0.0);
		routeSlops.put(5050.0, 0.0);
		routeSlops.put(5100.0, -Math.PI/36);
		routeSlops.put(5150.0, 0.0);
		routeSlops.put(5200.0, 0.0);
		routeSlops.put(5250.0, 0.0);
		routeSlops.put(5300.0, 0.0);
		routeSlops.put(5350.0, 0.0);
		routeSlops.put(5400.0, 0.0);
		routeSlops.put(5450.0, Math.PI/36);
		routeSlops.put(5500.0, 0.0);
		routeSlops.put(5550.0, 0.0);
		routeSlops.put(5600.0, 0.0);
		routeSlops.put(5650.0, 0.0);
		routeSlops.put(5700.0, 0.0);
		routeSlops.put(5750.0, 0.0);
		routeSlops.put(5800.0, -Math.PI/36);
		routeSlops.put(5850.0, 0.0);
		routeSlops.put(5900.0, 0.0);
		routeSlops.put(5950.0, 0.0);
		routeSlops.put(6000.0, 0.0);
		routeSlops.put(6050.0, 0.0);
		routeSlops.put(6100.0, 0.0);
		routeSlops.put(6150.0, 0.0);
		routeSlops.put(6200.0, 0.0);
		routeSlops.put(6250.0, 0.0);
		routeSlops.put(6300.0, 0.0);
		routeSlops.put(6350.0, 0.0);
		routeSlops.put(6400.0, 0.0);
		routeSlops.put(6450.0, 0.0);
		routeSlops.put(6500.0, Math.PI/36);
		routeSlops.put(6550.0, -Math.PI/36);
		routeSlops.put(6600.0, Math.PI/36);
		routeSlops.put(6650.0, -Math.PI/36);
		routeSlops.put(6700.0, 0.0);
		routeSlops.put(6750.0, 0.0);
		routeSlops.put(6800.0, 0.0);
		routeSlops.put(6850.0, 0.0);
		routeSlops.put(6900.0, 0.0);
		routeSlops.put(6950.0, 0.0);
		routeSlops.put(7000.0, 0.0);	
		routeSlops.put(15000.0, 0.0);
	}
		
	public void torques(){
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
		torques.put(220.0, 0.0); //100000.0
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
	
	public LookupTable getRouteSlops(State pos, LookupTable t, int w, int future) {
		LookupTable rs = new LookupTable();
		Double slope = getRouteSlops(pos.getData());
		if (slope != null)
			for (Double i = pos.getData(); i < pos.getData() + 20; i++) {
				rs.put(pos.getData(), slope);
			}
		else {
			TreeMap<Double, Double> tree = new TreeMap<>();
			for (Double p : t.keys()) {
				tree.put(p, t.get(p));
			}
			slope = StatisticalOperators.flr(tree, pos.getData(), w, future).getMean();
		}
		return rs;
	}

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
