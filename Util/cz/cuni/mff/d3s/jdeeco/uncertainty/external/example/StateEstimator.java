package cz.cuni.mff.d3s.jdeeco.uncertainty.external.example;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.ScenarioDataset;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.KalmanFilter;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.VehicleObservationModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.VehicleProcessModel;

public class StateEstimator {

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;
	protected static final double KM_MILLI_FACTOR = 0.000000278; // m per millisec
	
	VehicleProcessModel model;
	VehicleObservationModel obs;
	KalmanFilter filter;
	
	public StateEstimator(VehicleStateSpaceModel model) {
		this.model = new VehicleProcessModel(model);
		this.obs = new VehicleObservationModel();
		this.filter = new KalmanFilter(this.model);
	}
	
	
	public void addValue(double pos, double speed, double timestep) {
			obs.setPosition(pos, speed);
			model.setTime(timestep);
			filter.predict(timestep);
			filter.update(timestep, obs);
			filter.estimate(obs);
	}
	
	public double[] getVelocity() {
		double[] v = new double[2];
		v[0] = filter.model.state_estimate.data[2][0];
		v[1] = filter.model.state_estimate.data[3][0];
		return v;
	}
	
	public double[] getPosition() {
		double[] v = new double[2];
		v[0] = filter.model.state_estimate.data[0][0];
		v[1] = filter.model.state_estimate.data[1][0];
		return v;
	} 
	
	public static void main(String[] args) {
		double timestamp = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		ScenarioDataset sc = new ScenarioDataset();
//		double scale = 1000;
		//--------------- Vehicle 1 -------------------------------
		State pos = new State("pos", 10.0, timestamp);
		State speed = new State("speed", 90.0, timestamp);
		VehicleStateSpaceModel model = new VehicleStateSpaceModel(KM_MILLI_FACTOR, 2, sc.getTorques(), sc.getRouteSlops(),
				sc.getMass(), timestamp, speed, pos);
		model.setParams(0.5, 0.0);
		model.createBasicDataRange(timestamp);
		StateEstimator est = new StateEstimator(model);
		est.addValue(pos.getData(), speed.getData(), 0.00000001);
		double[] p = est.getPosition();
		double[] s = est.getVelocity();
		System.out.println("position "+p[0]+" speed  "+s[0]+" \n position "+p[1]+" speed "+s[1]);
	}

}
