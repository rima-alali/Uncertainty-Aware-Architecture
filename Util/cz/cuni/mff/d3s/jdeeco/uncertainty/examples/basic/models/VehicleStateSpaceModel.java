/*******************************************************************************
 * Copyright 2015 Charles University in Prague
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataRange;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.StateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.LookupTable;


public class VehicleStateSpaceModel extends StateSpaceModel {

	
	protected LookupTable torques;
	protected LookupTable routeSlops;
	protected Double mass;
	
	protected static final double g = 9.80665;

	
	public VehicleStateSpaceModel(double dt) {
		super(dt, 1);
	}

	public VehicleStateSpaceModel(State... states) {
		super(states);
	}

	public VehicleStateSpaceModel(double dt, State... states) {
		super(dt, 1, states);
	}
	
	public VehicleStateSpaceModel(double dt, int paramsNum, State... states) {
		this(dt, paramsNum, new LookupTable(), new LookupTable(), 1000.0, System.nanoTime() / MILLISEC_NANOSEC_FACTOR, states);
	}

	public VehicleStateSpaceModel(double dt, int paramsNum, LookupTable torques, LookupTable routeslopes, double mass, double timestamp, State... states) {
		super(dt, paramsNum, timestamp, states);
		this.torques = torques;
		this.routeSlops = routeslopes;
		this.mass = mass;
	}
	
	
	@Override
	public double value() {
		Double[] ps= getParams();
//		System.out.println(states[0]+"   "+states[1]);
		return getValueFromPlant(states[0].getData(), states[1].getData(),
				torques.get(states[0].getData()), ps[0], ps[1], routeSlops.get(states[1].getData()), mass);
	}

	@Override
	public double minValue() {
		return getValueFromPlant(states[0].getMinBound(), states[1].getMinBound(),
				torques.get(states[0].getMinBound()), 0.0, 1.0, routeSlops.get(states[1].getMinBound()), mass);
	}

	@Override
	public double maxValue() {
		return getValueFromPlant(states[0].getMaxBound(), states[1].getMaxBound(),
				torques.get(states[0].getMaxBound()), 1.0, 0.0, routeSlops.get(states[1].getMaxBound()), mass);
	}

	/**
	 * the order of inputs: speed, position, torque, gas, brake, slope, mass It considered brake
	 * with the reaction time ... 90 km/h ... around 83m -103m to stop
	 * https://www.qld.gov.au/transport/safety/road-safety/driving-safely/stopping-distances
	 * 
	 */
	@Override
	public double getValueFromPlant(Double... vals) {
//		if(vals.length != 7 || hasNull(vals)) {
//			System.err.println("please input 7 params as following:  speed, position, torque, gas, brake, slope, mass");
//			System.exit(0);
//		}
		
		Double speed = refineSpeed(vals[0]);
		Double pos = refinePos(vals[1]);
		Double torque = vals[2];
		Double gas = vals[3];
		Double brake = vals[4];
		Double slope = vals[5];
		Double mass = vals[6];
		double Acceleration = calculateAcceleration(speed, pos, torque, gas, brake, slope, mass);
//		System.out.println("accel ... "+refineAcceleration(pos, speed, Acceleration)+"  "+Acceleration+"  speed "+speed+"  pos "+pos+" torq "+torque+" gas "+gas+" brake "+brake+" slope "+slope+" mass "+mass);
		return refineAcceleration(pos, speed, Acceleration);
	}





	@Override
	public void setParams(Double... params) {
		this.params = params;
	}

	
	public void updateModel(LookupTable toruqes, LookupTable slops, double mass) {
		this.torques = toruqes;
		this.routeSlops = slops;
		this.mass = mass;
	}
	
	public void setMass(Double mass) {
		this.mass = mass;
	}
	
	public Double getMass() {
		return mass;
	}
	
	public void setTorques(LookupTable torques) {
		this.torques = torques;
	}
	
	public LookupTable getTorques() {
		return torques;
	}
	
	public Double getTorque(Double speed) {
		return torques.get(speed);
	}
	
	
	public void setRouteSlops(LookupTable routeSlops) {
		this.routeSlops = routeSlops;
	}
	
	public LookupTable getRouteSlops() {
		return routeSlops;
	}
	
	public Double getRouteSlop(Double pos) {
		return routeSlops.get(pos);
	}	
	
	public DataRange refineSpeed(DataRange dr) {
		DataRange v = dr;
		if(v.getMin() < 0) v.setMin(0);
		if(v.getMin() > 200) v.setMin(200);
		if(v.getMax() < 0) v.setMax(0);
		if(v.getMax() > 200) v.setMax(200);
		if(v.getData() < 0) v.setDataTimeStamp(0, v.getTimeStamp());
		if(v.getData() > 200) v.setDataTimeStamp(200, v.getTimeStamp());
		return v;
	}
	
	public DataRange refinePos(DataRange dr) {
		DataRange v = dr;
		if(v.getMin() < 0) v.setMin(0);
		if(v.getMax() < 0) v.setMax(0);
		if(v.getData() < 0) v.setDataTimeStamp(0, v.getTimeStamp());
		return v;
	}
	
	

//	public void initMeasurments(State accel, State speed, State pos, State gas, State brake, LookupTable torques,
//			LookupTable routeSlopes, Double mass, Double scale, Double currentTime, Double lastTime, Double lastTimeInitaited) {
//		initiateStates(lastTime, speed, pos);
//		setModel(gas, brake, routeSlopes, accel, currentTime);
//		setLastTimeInitiated(lastTime);
//	}
//	
//	public void updateMeasurments(State accel, State speed, State pos, State gas, State brake, LookupTable torques,
//			LookupTable routeSlopes, Double mass, Double scale, Double currentTime, Double lastTime, Double lastTimeInitaited) {
//		updateStates(speed, pos);
//		setModel(gas, brake, routeSlopes, accel, currentTime);
//		initTime(lastTime, lastTimeInitaited);
//	}
	
	
	/*
	 * private
	 */

//	public void setModel(State gas, State brake, LookupTable routeSlopes, State accel, double timestamp) {
//		setParams(gas.getData(), brake.getData());
//		updateModel(torques, routeSlopes, mass);
//		setBasicDataRange(accel.getDataRange(), timestamp);
//	}
//	
//	public void initTime(Double lastTime, Double lastTimeInitaited) {
//		if (lastTime > lastTimeInitaited) {
//			setLastTimeInitiated(lastTime);
//		}
//	}

	private double calculateAcceleration(Double speed, Double pos, Double torque, Double gas, Double brake, Double slope, Double mass) {
		double FEng = gas * torque / 0.005;
		double FResistance = brake * 40000;
		double FEngResistance = 0.0005 * speed;
		double FHill = Math.sin(slope) * g * mass;
		double FFinal = FEng - FResistance - FEngResistance - FHill;
		return FFinal / mass;
	}
	
	
	private boolean hasNull(Double... params) {
		for (int i = 0; i < params.length; i++) {
			if(params[i] == null) return true;
		}
		return false;
	}
	
	private void handleException(Double... params) {
		if (params.length == 0 || params.length > 2) {
			System.err.println("The parameters should be the 'gas' and 'brake' values only ... ");
			System.exit(0);
		}
	}

//	private Double[] separateValues(Double... params) {
//		Double[] ps = new Double[2];
//		if (params[0] >= 0) {
//			ps[0] = params[0];
//			ps[1] = 0.0;
//		} else {
//			ps[0] = 0.0;
//			ps[1] = 1.0;
//		}
//		return ps;
//	}

	private Double refineSpeed(Double speed) {
		if (speed <= 0)
			speed = 0.0;
		if (speed >= 200)
			speed = 200.0;
		return speed;
	}

	private Double refinePos(Double pos) {
		if (pos <= 0)
			return 0.0;
		return pos;
	}

	private Double refineAcceleration(Double pos, Double speed, Double Acceleration) {
		if (pos <= 0 && Acceleration < 0)
			return 0.0;
		
		if (speed <= 0 && Acceleration < 0)
			return 0.0;
		
		if (speed >= 200.0 && Acceleration > 0)
			return 0.0;
		
		return Acceleration;
	}

	@Override
	public Double[] getParams() {
		return params;
	}

}
