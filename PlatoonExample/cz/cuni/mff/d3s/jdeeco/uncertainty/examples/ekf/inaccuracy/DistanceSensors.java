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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.inaccuracy;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;

@Component
public class DistanceSensors {

	public String id;
	public String sid;
	public String sname;
	public String slid;

	// ################ internal knowledge #####################

	public State svehPos = new State("vehPos", 0.0);
	public State svehSpeed = new State("vehSpeed", 0.0);

	// ############ reference vehicle knowledge ################
	public State srefPos = new State("refPos", 0.0);
	public State srefSpeed = new State("refSpeed", 0.0);

	public State radarsrefSpeed = new State("refSpeed", 0.0);
	public State lidarsrefSpeed = new State("refSpeed", 0.0);
	public State camerasrefSpeed = new State("refSpeed", 0.0);

	// ############ distance ################
	public State radarDistance = new State("RadarDistance", 0.0);
	public State lidarDistance = new State("LidarDistance", 0.0);
	public State cameraDistance = new State("CameraDistance", 0.0);

	protected static final double MILLI_NANO_FACTOR = 1000000;
	
	

	public DistanceSensors(String id, String name, String sid, String slid, double pos, double speed, double refpos,
			double refspeed) {
		this.id = id;
		this.sid = sid;
		this.sname = name;
		this.slid = slid;

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		setVehicleKnowledge(speed, pos, currentTime);
		setRefVehicleKnowledge(refspeed, refpos, currentTime);
	}


	@Process
	@PeriodicScheduling(period = 2000)
	public static void calculateRadarDistances(@In("sid") String sid, @In("slid") String slid,
			@In("svehPos") State svehPos, 
			@In("srefPos") State srefPos, 
			@In("srefSpeed") State srefSpeed,
			@InOut("radarDistance") ParamHolder<State> radarDistance,
			@InOut("radarsrefSpeed") ParamHolder<State> radarsrefSpeed) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * ================= distances =================
		 */

		if (slid != "") {
			State distance = new State("distance", 0.0);
			distance.setData(srefPos.getData() - svehPos.getData(),
					srefPos.getMinBound() - svehPos.getMaxBound(),
					srefPos.getMaxBound() - svehPos.getMinBound(),
					currentTime);
			if (radarDistance.value.getData() <= 250) {
				radarDistance.value.setData(distance.getDataRange());
				radarsrefSpeed.value.setData(srefSpeed.getDataRange());
			}
		}
	}

	@Process
	@PeriodicScheduling(period = 2000)
	public static void calculateLidarDistances(@In("sid") String sid, @In("slid") String slid,
			@In("svehPos") State svehPos, 
			@In("srefPos") State srefPos, 
			@In("srefSpeed") State srefSpeed,
			@InOut("lidarDistance") ParamHolder<State> lidarDistance,
			@InOut("lidarsrefSpeed") ParamHolder<State> lidarsrefSpeed) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * ================= distances =================
		 */

		if (slid != "") {
			State distance = new State("distance", 0.0);
			distance.setData(srefPos.getData() - svehPos.getData(),
					srefPos.getMinBound() - svehPos.getMaxBound(),
					srefPos.getMaxBound() - svehPos.getMinBound(),
					currentTime);
			if (lidarDistance.value.getData() <= 100) {
				lidarDistance.value.setData(distance.getDataRange());
				lidarsrefSpeed.value.setData(srefSpeed.getDataRange());
			}
		}
	}

	@Process
	@PeriodicScheduling(period = 2000)
	public static void calculateCameraDistances(@In("sid") String sid, @In("slid") String slid,
			@In("svehPos") State svehPos, 
			@In("srefPos") State srefPos, @In("srefSpeed") State srefSpeed,
			@InOut("cameraDistance") ParamHolder<State> cameraDistance,
			@InOut("camerasrefSpeed") ParamHolder<State> camerasrefSpeed) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * ================= distances =================
		 */

		if (slid != "") {
			State distance = new State("distance", 0.0);
			distance.setData(srefPos.getData() - svehPos.getData(),
					srefPos.getMinBound() - svehPos.getMaxBound(),
					srefPos.getMaxBound() - svehPos.getMinBound(),
					currentTime);
			if (cameraDistance.value.getData() < 250) {
				cameraDistance.value.setData(distance.getDataRange());
				camerasrefSpeed.value.setData(srefSpeed.getDataRange());
			}
		}
	}

	/*
	 * private
	 */

	private void setVehicleKnowledge(double speed, double pos, double currentTime) {
		svehPos.setData(new DataTimeStamp<Double>(pos, currentTime));
		svehSpeed.setData(new DataTimeStamp<Double>(speed, currentTime));
	}

	private void setRefVehicleKnowledge(double refspeed, double refpos, double currentTime) {
		srefPos.setData(new DataTimeStamp<Double>(refpos, currentTime));
		srefSpeed.setData(new DataTimeStamp<Double>(refspeed, currentTime));
	}
}
