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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.bayes;

import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.History;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.LookupTable;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.example.StateEstimator;


@Role
public class VehicleRole {

	public String id;
	public String name;
	public String lid;
	public Double offsetTime = 0.0;
	public Double timeElapse = 0.0;
	
	//################ modes #####################
	public Boolean canTransit;
	public VehicleModeEnum mode;
	public VehicleModeEnum oldMode;
	
	//################ internal knowledge #####################
	
	public State vehPos = new State("vehPos", 0.0);
	public State vehSpeed = new State("vehSpeed", 0.0);
	public State vehAccel = new State("vehAccel", 0.0);
	public State vehGas = new State("vehGas",0.0);
	public State vehBrake = new State("vehBrake",0.0);
	
	public LookupTable vehTorques = new LookupTable();
	public LookupTable vehRouteSlopes = new LookupTable();
	public Double vehMass = 1000.0;
	public Double vehLength = 3.0;
	public VehicleStateSpaceModel vehModel;
	
	public Double vehDiseredDistance = 5.0;
	public Double vehDiseredSpeed = 90.0;
	
	public Double vehLastTime = 0.0;
	public Double vehLastTimeUpdate = 0.0;
	
	public Double actualPos = 0.0;
	public Double actualSpeed = 0.0;
	public Double targetPos = 0.0;
	public Double targetSpeed = 0.0;

	public Controller controller;
	public Double pidPosOutput = 0.0;
	public Double pidSpeedOutput = 0.0;
	
	
	
	public State vehPosACC = new State("vehPos", 0.0);
	public State vehSpeedACC = new State("vehSpeed", 0.0);
	public State vehAccelACC = new State("vehAccel", 0.0);
	public State vehGasACC = new State("vehGas",0.0);
	public State vehBrakeACC = new State("vehBrake",0.0);

	public VehicleStateSpaceModel vehModelACC;

	public Double actualPosACC = 0.0;
	public Double actualSpeedACC = 0.0;
	public Double targetPosACC = 0.0;
	public Double targetSpeedACC = 0.0;
	
	public Controller controllerACC;
	public Double pidPosOutputACC = 0.0;
	public Double pidSpeedOutputACC = 0.0;
	
	//############ reference vehicle knowledge ################
	public State refPos = new State("refPos", 0.0);
	public State refSpeed = new State("refSpeed", 0.0);
	public State refAccel = new State("refAccel", 0.0);
	public State refGas = new State("refGas",0.0);
	public State refBrake = new State("refBrake",0.0);
	
	public LookupTable refTorques = new LookupTable();
	public LookupTable refRouteSlopes = new LookupTable();
	public Double refMass = 1000.0;
	public Double refLength = 3.0;
	public VehicleStateSpaceModel refModel;
	
	public Double refLastTime = 0.0;
	public Double refLastTimeUpdate = 0.0;
	public Double refCreationTime = 0.0;
	
	public State vehrefPos = new State("refPos", 0.0);
	public State vehrefSpeed = new State("refSpeed", 0.0);
	public State vehrefAccel = new State("refAccel", 0.0);

	//############ distance ################
	public State speed = new State("speed",0.0);
	public State distance = new State("distance",0.0);
	public State brakingDistance = new State("BrakingDistance",0.0);
	public State diffbrakingDistance = new State("diffBrakingDistance",0.0);

	public State distanceCACC = new State("distance",0.0);
	public State distanceACC = new State("distance",0.0);

	public State speedCACC = new State("speed",0.0);
	public State speedACC = new State("speed",0.0);

	//############ history ################
	public History history = new History();
	public StateEstimator est;
	public StateEstimator estACC;
	

}