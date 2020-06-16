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
package cz.cuni.mff.d3s.jdeeco.uncertainty.example.baseline;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.History;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.TimeState;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyEvaluation;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.LookupTable;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.ScenarioDataset;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.logger.LoggerFormatter;

public class Main {

	public String id;
	public String name;
	public String lid;

	// ################ internal knowledge #####################

	public State vehPos = new State("vehPos", 0.0);
	public State vehSpeed = new State("vehSpeed", 0.0);
	public State vehAccel = new State("vehAccel", 0.0);
	public State vehGas = new State("vehGas", 0.0);
	public State vehBrake = new State("vehBrake", 0.0);

	public LookupTable vehTorques = new LookupTable();
	public LookupTable vehRouteSlopes = new LookupTable();
	public Double vehMass = 1000.0;
	public Double vehLength = 3.0;
	public VehicleStateSpaceModel vehModel;

	public Double vehDiseredDistance = 5.0;
	public Double vehDiseredSpeed = 90.0;

	public Double actualPos = 0.0;
	public Double actualSpeed = 0.0;
	public Double targetPos = 0.0;
	public Double targetSpeed = 0.0;

	public Controller controller = new Controller();
	public Double pidPosOutput = 0.0;
	public Double pidSpeedOutput = 0.0;

	// ############ reference vehicle knowledge ################
	public State refPos = new State("refPos", 0.0);
	public State refSpeed = new State("refSpeed", 0.0);
	public State refAccel = new State("refAccel", 0.0);
	public State refGas = new State("refGas", 0.0);
	public State refBrake = new State("refBrake", 0.0);

	public LookupTable refTorques = new LookupTable();
	public LookupTable refRouteSlopes = new LookupTable();
	public Double refMass = 1000.0;
	public Double refLength = 3.0;
	public VehicleStateSpaceModel refModel;

	public Double refactualPos = 0.0;
	public Double refactualSpeed = 0.0;
	public Double reftargetPos = 0.0;
	public Double reftargetSpeed = 0.0;

	public Controller refcontroller = new Controller();
	public Double refpidPosOutput = 0.0;
	public Double refpidSpeedOutput = 0.0;

	// ############ distance ################
	public State distance = new State("distance", 0.0);
	public TimeState brakingDistance = new TimeState("distance", 0.0);
	public TimeState diffBrakingDistance = new TimeState("BrakingDistance", 0.0);

	// ############ history ################
	public History history = new History();

	protected static final double MILLI_NANO_FACTOR = 1000000;
	protected static ScenarioDataset sc = new ScenarioDataset();
	private static final Logger LOGGER = Logger.getLogger(CACCACCGuard.class.getName());
	protected static final double KM_SEC_FACTOR = 0.000278; // m per sec

	public static void main(String[] args) {
		setLogger();

		Main m = new Main();
		m.init();
		for (int i = 0; i < 1000000; i++) {
			m.exe();
		}
	}

	public void init() {
		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;
		refPos.setData(new DataTimeStamp<Double>(0.0, currentTime));
		refSpeed.setData(new DataTimeStamp<Double>(0.0, currentTime));
		refTorques = sc.getTorques();
		refRouteSlopes = sc.getRouteSlops();
		refMass = sc.getMass();
		refModel = new VehicleStateSpaceModel(KM_SEC_FACTOR, 2, refTorques, refRouteSlopes, refMass, currentTime,
				refSpeed, refPos);
		refModel.setParams(refGas.getData(), refBrake.getData());

		vehPos.setData(new DataTimeStamp<Double>(0.0, currentTime));
		vehSpeed.setData(new DataTimeStamp<Double>(0.0, currentTime));
		vehTorques = sc.getTorques();
		vehRouteSlopes = sc.getRouteSlops();
		vehMass = sc.getMass();
		vehModel = new VehicleStateSpaceModel(KM_SEC_FACTOR, 2, vehTorques, vehRouteSlopes, vehMass, currentTime,
				vehSpeed, vehPos);
		vehModel.setParams(vehGas.getData(), vehBrake.getData());

		controller = new Controller();
		targetPos = 0.0;
		targetSpeed = 0.0;
	}

	public void exe() {
		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;
		ScenarioDataset sc = new ScenarioDataset();

		refAccel.setData(getAcceleration(refSpeed.getData(), refPos.getData(), refTorques, refGas.getData(),
				refBrake.getData(), refMass), currentTime);

		refModel.setParams(refGas.getData(), refBrake.getData());
//		vehRouteSlopes.value = sc.getRouteSlops(vehPos.value.getData(), vehPos.value.getData() + 10);
		refModel.updateModel(refTorques, refRouteSlopes, refMass);
		refPos.addNumber(refpidPosOutput);
		refSpeed.addNumber(refpidSpeedOutput);
		refModel.setBasicDataRange(refAccel.getDataRange());
		refModel.initiateStates(currentTime, refSpeed, refPos);
		refModel.setLastTimeInitiated(currentTime);

		// -------------------- states --------------------
		State[] states = InaccuracyEvaluation.fstates(refModel, 0, 1.0, currentTime);
		refSpeed.setData(refModel.refineSpeed(states[0].getDataRange()));
		refPos.setData(refModel.refinePos(states[1].getDataRange()));
		refModel.updateStates(refSpeed, refPos);

//		refSpeed.setData(refSpeed.getData() + refAccel.getData() * 0.1, currentTime);
//		refPos.setData(refPos.getData() + refSpeed.getData() * 0.1, currentTime);

		refactualPos = refPos.getData();
		refactualSpeed = refSpeed.getData();
		reftargetPos = refPos.getData();
		reftargetSpeed = 90.0;

		System.out.println(
				"Vehicle2 : " + refactualSpeed + " " + reftargetSpeed + " " + refactualPos + " " + reftargetPos);
		double[] refout = controller.getActuators(refactualSpeed, reftargetSpeed, refactualPos, reftargetPos);
		refGas.setData(new DataTimeStamp<Double>(refout[0], currentTime));
		refBrake.setData(new DataTimeStamp<Double>(refout[1], currentTime));
		refpidPosOutput = controller.getPidPosOutput();
		refpidSpeedOutput = controller.getPidSpeedOutput();

		vehAccel.setData(getAcceleration(vehSpeed.getData(), vehPos.getData(), sc.getTorques(), vehGas.getData(),
				vehBrake.getData(), sc.getMass()), currentTime);

		vehModel.setParams(vehGas.getData(), vehBrake.getData());
//		vehRouteSlopes.value = sc.getRouteSlops(vehPos.value.getData(), vehPos.value.getData() + 10);
		vehModel.updateModel(vehTorques, vehRouteSlopes, vehMass);
		vehPos.addNumber(pidPosOutput);
		vehSpeed.addNumber(pidSpeedOutput);
		vehModel.setBasicDataRange(vehAccel.getDataRange());
		vehModel.initiateStates(currentTime, vehSpeed, vehPos);
		vehModel.setLastTimeInitiated(currentTime);

		// -------------------- states --------------------
		states = InaccuracyEvaluation.fstates(vehModel, 0, 1.0, currentTime);
		vehSpeed.setData(vehModel.refineSpeed(states[0].getDataRange()));
		vehPos.setData(vehModel.refinePos(states[1].getDataRange()));
		vehModel.updateStates(vehSpeed, vehPos);


		distance = InaccuracyOperators.minus(refPos, vehPos, currentTime);
		distance.addNumber(-refLength);
		brakingDistance = InaccuracyEvaluation.cvLowerState(vehSpeed, 0.0, vehPos, vehModel, 1.0, currentTime);
		brakingDistance.addNumber(-vehPos.getData());
		diffBrakingDistance = InaccuracyOperators.minuscvLowersState(refSpeed, 0.0, refPos, refModel, vehSpeed, 0.0,
				vehPos, vehModel, 1.0, currentTime);
		diffBrakingDistance.addNumber(-refLength);
		
		double dis = vehDiseredDistance;
//		if(diffBrakingDistance.getDataTimeStamp().getData() < vehDiseredDistance)
//			dis = Math.abs(vehDiseredDistance - diffBrakingDistance.getDataTimeStamp().getData()) + vehDiseredDistance;
		
		
		actualPos = vehPos.getData();
		actualSpeed = vehSpeed.getData();
		targetPos = vehPos.getData() + distance.getData() - dis;
		targetSpeed = refSpeed.getData();

		if ((targetPos - vehPos.getData()) < 0 && vehPos.getData() == 0.0) {
			actualPos = vehPos.getData();
			actualSpeed = vehSpeed.getData();
			targetPos = vehPos.getData();
			targetSpeed = vehSpeed.getData();
		}

		System.out.println("Vehicle3 : " + actualSpeed + " " + targetSpeed + " " + actualPos + " " + targetPos);
		double[] out = controller.getActuators(actualSpeed, targetSpeed, actualPos, targetPos);
		vehGas.setData(new DataTimeStamp<Double>(out[0], currentTime));
		vehBrake.setData(new DataTimeStamp<Double>(out[1], currentTime));
		pidPosOutput = controller.getPidPosOutput();
		pidSpeedOutput = controller.getPidSpeedOutput();


		String str2 = String.format(
				"%s,%s,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f",
				"Vehicle3", "Vehicle2", diffBrakingDistance.getMin().getData(), diffBrakingDistance.getMax().getData(),
				diffBrakingDistance.getDataTimeStamp().getData(), brakingDistance.getMin().getData(),
				brakingDistance.getMax().getData(), brakingDistance.getDataTimeStamp().getData(), distance.getMinBound(),
				distance.getMaxBound(), distance.getData(), distance.getTimeStamp(), vehPos.getMinBound(),
				vehPos.getMaxBound(), vehPos.getData(), vehPos.getTimeStamp(), vehSpeed.getMinBound(),
				vehSpeed.getMaxBound(), vehSpeed.getData(), vehSpeed.getTimeStamp(), refPos.getMinBound(),
				refPos.getMaxBound(), refPos.getData(), refPos.getTimeStamp(), refSpeed.getMinBound(),
				refSpeed.getMaxBound(), refSpeed.getData(), refSpeed.getTimeStamp(), vehDiseredDistance, refLength,
				currentTime);
		LOGGER.info(str2);

		System.err.println(distance.getData() + "  " + brakingDistance.getDataTimeStamp().getData() + "  "
				+ diffBrakingDistance.getDataTimeStamp().getData()+"  "+(targetPos - actualPos));

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Double getAcceleration(Double speed, Double pos, LookupTable torques, Double gas, Double brake,
			Double mass) {
		double FEng = gas * torques.get(speed) / 0.005;
		double FResistance = brake * 10000;
		double FEngResistance = 0.0005 * speed;
		double FHill = Math.sin(sc.getRouteSlops(pos)) * sc.getG() * sc.getMass();
		double FFinal = FEng - FResistance - FEngResistance - FHill;
		double Acceleration = FFinal / mass;
		return Acceleration;
	}

	private static void setLogger() {
		boolean append = true;
		try {
			FileHandler handler = new FileHandler("./logs/baseline.csv", append);
//			FileHandler handler = new FileHandler("./logs/distance_est.csv", append);
//			FileHandler handler = new FileHandler("./logs/distance_est_unc.csv", append);
			LOGGER.addHandler(handler);
			LOGGER.setUseParentHandlers(false);
			Formatter simpleFormatter = new LoggerFormatter();
			handler.setFormatter(simpleFormatter);
			LOGGER.info("vehicleID,vehicleLID,diffBrakingDistanceMin,diffBrakingDistanceMax,diffBrakingDistanceValue,"
					+ "brakingDistanceMin,brakingDistanceMax,brakingDistanceValue,"
					+ "distanceMin,distanceMax,distanceValue,distanceTimestamp,"
					+ "vehPosMin,vehPosMax,vehPosValue,vehPosTimestamp,"
					+ "vehSpeedMin,vehSpeedMax,vehSpeedValue,vehSpeedTimestamp,"
					+ "refPosMin,refPosMax,refPosValue,refPosTimestamp,"
					+ "refSpeedMin,refSpeedMax,refSpeedValue,refSpeedTimestamp,"
					+ "vehDiseredDistance,refLength,currentTime");

		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
