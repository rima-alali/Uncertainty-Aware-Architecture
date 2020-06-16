package cz.cuni.mff.d3s.jdeeco.uncertainty.external;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;

public class VehicleProcessModel extends ProcessModel {
	
	protected VehicleStateSpaceModel model;
	protected double time = 0;
	
	
	public VehicleProcessModel(VehicleStateSpaceModel model) {
		this.model = model;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	@Override
	public int stateDimension() {
		return 4;
	}

	@Override
	public void initialState(double[][] x) {
//		State[] states = model.getStates();
		x[0][0] = 0;//states[1].getData();
		x[1][0] = 0;//states[0].getData();
//		model.createBasicDataRange(model.getBasicDataRange().getTimeStamp() + time);
//		double speed = x[1][0] + model.getBasicDataRange().getValue() * time ;
//		double pos = x[0][0] + speed * time ;
//		states[1].setData(pos, model.getBasicDataRange().getTimeStamp() + time);
//		states[0].setData(speed, model.getBasicDataRange().getTimeStamp() + time);
//		model.updateStates(states[0],states[1]);
		x[2][0] = 0;//pos;
		x[3][0] = 0;//speed;
	}

	@Override
	public void initialStateCovariance(double[][] cov) {
		cov[0][0] = 1000;
		cov[1][1] = 1000;
		cov[2][2] = 1000;
		cov[3][3] = 1000;
	}

	@Override
	public void stateFunction(double[][] x, double[][] f) {
		f[0][0] = x[1][0];
		f[1][0] = 0;
		f[2][0] = x[3][0];
		f[3][0] = 0;
	}

	@Override
	public void stateFunctionJacobian(double[][] x, double[][] j) {
		j[0][1] = 1;
		j[2][3] = 1;
	}

	@Override
	public void processNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[1][1] = 1;
		cov[2][2] = 1;
		cov[3][3] = 1;
	}
}
