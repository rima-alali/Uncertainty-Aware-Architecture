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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.inaccuracy.uncertainty;

import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import cz.cuni.mff.d3s.deeco.modes.DEECoModeGuard;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.History;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.exponitial.ExponentialOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.logger.LoggerFormatter;

public class CACCACCGuard extends DEECoModeGuard {

	private static final Logger LOGGER = Logger.getLogger(CACCACCGuard.class.getName());

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	public CACCACCGuard() {
		setLogger();
	}

	@Override
	protected void specifyParameters() {
	}

	@Override
	public String[] getKnowledgeNames() {
		return new String[] { "mode", "brakingDistance", "distance", "id", "lid", "vehPos", "vehSpeed", "vehrefPos",
				"vehrefSpeed", "vehDiseredDistance", "refLength", "history", "diffbrakingDistance", "speed" };
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSatisfied(Object[] knowledgeValues) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;

		State brakingDistance = ((State) knowledgeValues[1]);
		State distance = ((State) knowledgeValues[2]);
		String id = ((String) knowledgeValues[3]);
		String lid = ((String) knowledgeValues[4]);
		State vehPos = ((State) knowledgeValues[5]);
		State vehSpeed = ((State) knowledgeValues[6]);
		State vehrefPos = ((State) knowledgeValues[7]);
		State vehrefSpeed = ((State) knowledgeValues[8]);
		Double vehDiseredDistance = ((Double) knowledgeValues[9]);
		Double refLength = ((Double) knowledgeValues[10]);
		History history = ((History) knowledgeValues[11]);
		State diffbrakingDistance = ((State) knowledgeValues[12]);
		State speed = ((State) knowledgeValues[13]);
		
		TreeMap<Double, Double> distree = history.getInputMerge("CACC", "Distance");
		TreeMap<Double, Double> bdistree = history.getSoftThreshold("CACC");
		Double delayDistanceMean = ExponentialOperators.fexpMean(history.getInputDelays("wifiDistance"), 500, 0);

		Boolean cond = distance.getData() < vehDiseredDistance;
		Boolean cond1 = diffbrakingDistance.getMinBound() <= 0.0;
		Boolean cond2 = StatisticalOperators.below(bdistree, 0.0, 2000);
		Boolean cond3 = StatisticalOperators.fbelow(bdistree, 0.0, 2000,2000);

		
//		System.err.println("cond ... "+brakingDistance+" > "+distance.getMinBound()+"  "+(brakingDistance > distance.getMinBound()));
		// time is in second
		String str = String.format("%s,%s,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%s,%s,%s,%s,%f,%f", 
				id,	lid, 
				diffbrakingDistance.getMinBound(), diffbrakingDistance.getMaxBound(),
				diffbrakingDistance.getData(), 
				brakingDistance.getMinBound(), 
				brakingDistance.getMaxBound(), brakingDistance.getData(),
				distance.getMinBound(), distance.getMaxBound(),
				distance.getData(), distance.getTimeStamp(), 
				vehPos.getMinBound(), vehPos.getMaxBound(), vehPos.getData(), vehPos.getTimeStamp(), 
				vehSpeed.getMinBound(), vehSpeed.getMaxBound(), vehSpeed.getData(), vehSpeed.getTimeStamp(), 
				vehrefPos.getMinBound(), vehrefPos.getMaxBound(), vehrefPos.getData(), vehrefPos.getTimeStamp(), 
				vehrefSpeed.getMinBound(), vehrefSpeed.getMaxBound(), vehrefSpeed.getData(), vehrefSpeed.getTimeStamp(), 
				vehDiseredDistance, refLength, currentTime, cond, cond1, cond2, cond3, speed.getData(), speed.getTimeStamp());
		LOGGER.info(str);

		return false;
	}

	private void setLogger() {
		boolean append = true;
		try {
			FileHandler handler = new FileHandler("./logs/inaccuracy_unc.csv", append);
			LOGGER.addHandler(handler);
			LOGGER.setUseParentHandlers(false);
			Formatter simpleFormatter = new LoggerFormatter();
			handler.setFormatter(simpleFormatter);
			LOGGER.info("vehicleID,vehicleLID,"
					+ "diffBrakingDistanceMin,diffBrakingDistanceMax,diffBrakingDistanceValue,"
					+ "brakingDistanceMin,brakingDistanceMax,brakingDistanceValue,"
					+ "distanceMin,distanceMax,distanceValue,distanceTimestamp,"
					+ "vehPosMin,vehPosMax,vehPosValue,vehPosTimestamp,"
					+ "vehSpeedMin,vehSpeedMax,vehSpeedValue,vehSpeedTimestamp,"
					+ "refPosMin,refPosMax,refPosValue,refPosTimestamp,"
					+ "refSpeedMin,refSpeedMax,refSpeedValue,refSpeedTimestamp,"
					+ "vehDiseredDistance,refLength,currentTime,"
					+ "normalCondition,minCondition,statisticalCondition,predictionCondition,speed,speedTimestamp");

		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}