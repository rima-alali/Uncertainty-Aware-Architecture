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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.bayes.uncertinties;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import cz.cuni.mff.d3s.deeco.modes.DEECoModeGuard;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.History;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.exponitial.ExponentialOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.learning.BayesianOperators;
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
				"vehrefSpeed", "vehDiseredDistance", "refLength", "history", "diffbrakingDistance",  "distanceCACC", "distanceACC",  "speedCACC", "speedACC" };
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
		State distanceCACC = ((State) knowledgeValues[13]);
		State distanceACC = ((State) knowledgeValues[14]);
		State speedCACC = ((State) knowledgeValues[15]);
		State speedACC = ((State) knowledgeValues[16]);
		
		HashMap<String, TreeMap<Double, Double>> insDist = history.getInput("CACC", "Distance");
		HashMap<String, TreeMap<Double, Double>> insSpeed = history.getInput("CACC", "Speed");
		
//		System.out.println(insDist.keySet());
		
		TreeMap<Double, Double> wifiDistance = insDist.get("wifiDistance");
		TreeMap<Double, Double> wifiSpeed = insSpeed.get("wifiSpeed");
		TreeMap<Double, Double> radarDistance = insDist.get("RadarDistance");
		TreeMap<Double, Double> radarSpeed = insSpeed.get("RadarSpeed");
		TreeMap<Double, Double> lidarDistance = insDist.get("LidarDistance");
		TreeMap<Double, Double> lidarSpeed = insSpeed.get("LidarSpeed");
		TreeMap<Double, Double> cameraDistance = insDist.get("CameraDistance");
		TreeMap<Double, Double> cameraSpeed = insSpeed.get("CameraSpeed");
		
		Double wifiDistanceValue = 0.0;
		Double wifiDistanceTimestamp = 0.0;
		if(wifiDistance != null && wifiDistance.size() > 0) {
			wifiDistanceValue = wifiDistance.lastEntry().getValue();
			wifiDistanceTimestamp = wifiDistance.lastEntry().getKey();
		}
		
		Double wifiSpeedValue = 0.0;
		Double wifiSpeedTimestamp = 0.0;
		if(wifiSpeed != null && wifiSpeed.size() > 0) {
			wifiSpeedValue = wifiSpeed.lastEntry().getValue();
   			wifiSpeedTimestamp = wifiSpeed.lastEntry().getKey();
		}
		
		Double radarDistanceValue = 0.0;
		Double radarDistanceTimestamp = 0.0;
		if(radarDistance != null && radarDistance.size() > 0) {
			radarDistanceValue = radarDistance.lastEntry().getValue();
			radarDistanceTimestamp = radarDistance.lastEntry().getKey();
		}
		
		Double radarSpeedValue = 0.0;
		Double radarSpeedTimestamp = 0.0;
		if(radarSpeed != null && radarSpeed.size() > 0) {
			radarSpeedValue = radarSpeed.lastEntry().getValue();
			radarSpeedTimestamp = radarSpeed.lastEntry().getKey();
		}
		
		Double lidarDistanceValue = 0.0;
		Double lidarDistanceTimestamp = 0.0;
		if(lidarDistance != null && lidarDistance.size() > 0) {
			lidarDistanceValue = lidarDistance.lastEntry().getValue();
			lidarDistanceTimestamp = lidarDistance.lastEntry().getKey();
		}
		
		Double lidarSpeedValue = 0.0;
		Double lidarSpeedTimestamp = 0.0;
		if(lidarSpeed != null && lidarSpeed.size() > 0) {
			lidarSpeedValue = lidarSpeed.lastEntry().getValue();
			lidarSpeedTimestamp = lidarSpeed.lastEntry().getKey();
		}
		
		Double cameraDistanceValue = 0.0;
		Double cameraDistanceTimestamp = 0.0;
		if(cameraDistance != null && cameraDistance.size() > 0) {
			cameraDistanceValue = cameraDistance.lastEntry().getValue();
			cameraDistanceTimestamp = cameraDistance.lastEntry().getKey();
		}
		
		Double cameraSpeedValue = 0.0;
		Double cameraSpeedTimestamp = 0.0;
		if(cameraSpeed != null && cameraSpeed.size() > 0) {
			cameraSpeedValue = cameraSpeed.lastEntry().getValue();
			cameraSpeedTimestamp = cameraSpeed.lastEntry().getKey();
		}
		
		TreeMap<Double, Double> outsCACC = history.getOutputs("CACC");
		TreeMap<Double, Double> outsACC = history.getOutputs("ACC");
		
		Double outsCACCValue = 0.0;
		Double outsCACCTimestamp = 0.0;
		if(outsCACC != null && outsCACC.size() > 0) {
			outsCACCValue = outsCACC.lastEntry().getValue();
			outsCACCTimestamp = outsCACC.lastEntry().getKey();	
		}
		
		Double outsACCValue = 0.0;
		Double outsACCTimestamp = 0.0;
		if(outsACC != null && outsACC.size() > 0) {
			outsACCValue = outsACC.lastEntry().getValue();
			outsACCTimestamp = outsACC.lastEntry().getKey();	
		}
		
		
		TreeMap<Double, Double> distree = history.getInputMerge("CACC", "Distance");
		HashMap<String, TreeMap<Double, Double>> distreeCACC = history.getInput("CACC", "Distance");
		HashMap<String, TreeMap<Double, Double>> distreeACC = history.getInput("ACC", "Distance");
		TreeMap<Double, Double> bdistree = history.getSoftThreshold("CACC");
		Double delayDistanceMean = ExponentialOperators.fexpMean(history.getInputDelays("wifiDistance"), 500, 0);

		Boolean cond = distance.getData() < vehDiseredDistance;
		Boolean cond1 = diffbrakingDistance.getMinBound() <= 0.0;
		Boolean cond2 = StatisticalOperators.below(bdistree, 0.0, 2000);
		Boolean cond3 = StatisticalOperators.fbelow(bdistree, 0.0, 2000,2000);
		
//		int w = 10000;
//		TreeMap<Double, Double> corrCACC = BayesianOperators.corr(distreeCACC, outsCACC, 5.0, w);
		
//		Double c_cacc = 0.0;
//		if(corrCACC.size() > 0 ) corrCACC.lastEntry().getValue();


//		TreeMap<Double, Double> corrACC = BayesianOperators.corr(distreeACC, outsACC, 7.0, w);
//		Double c_acc = 0.0;
//		if(corrACC.size() > 0) c_acc = corrACC.lastEntry().getValue();

		Boolean cond4 = false;
//		System.out.println(distreeCACC.keySet());
//		if(distreeCACC.get("wifiDistance").size() > 10)
//			cond4 = BayesianOperators.fbelowcorr(distreeCACC, outsCACC, 5.0, distreeACC, outsACC, 7.0, 10, 0);
		

		
//		System.err.println("cond ... "+brakingDistance+" > "+distance.getMinBound()+"  "+(brakingDistance > distance.getMinBound()));
		// time is in second
		String str = String.format("%s,%s,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%s,%s,%s,%s,%s", 
				id,	lid, 
				diffbrakingDistance.getMinBound(), diffbrakingDistance.getMaxBound(),
				diffbrakingDistance.getData(), 
				brakingDistance.getMinBound(), 
				brakingDistance.getMaxBound(), brakingDistance.getData(),
				distance.getMinBound(), distance.getMaxBound(),
				distance.getData(), distance.getTimeStamp(), 
				distanceCACC.getData(), distanceCACC.getTimeStamp(), 
				distanceACC.getData(), distanceACC.getTimeStamp(),
				speedCACC.getData(), speedCACC.getTimeStamp(), 
				speedACC.getData(), speedACC.getTimeStamp(), 
				wifiDistanceValue, wifiDistanceTimestamp, 
				wifiSpeedValue, wifiSpeedTimestamp,
				radarDistanceValue, radarDistanceTimestamp, 
				radarSpeedValue, radarSpeedTimestamp,
				lidarDistanceValue, lidarDistanceTimestamp, 
				lidarSpeedValue, lidarSpeedTimestamp,
				cameraDistanceValue, cameraDistanceTimestamp, 
				cameraSpeedValue, cameraSpeedTimestamp,
				outsCACCValue, outsCACCTimestamp,
				outsACCValue, outsACCTimestamp,
				delayDistanceMean,
				vehPos.getMinBound(), vehPos.getMaxBound(), vehPos.getData(), vehPos.getTimeStamp(), 
				vehSpeed.getMinBound(), vehSpeed.getMaxBound(), vehSpeed.getData(), vehSpeed.getTimeStamp(), 
				vehrefPos.getMinBound(), vehrefPos.getMaxBound(), vehrefPos.getData(), vehrefPos.getTimeStamp(), 
				vehrefSpeed.getMinBound(), vehrefSpeed.getMaxBound(), vehrefSpeed.getData(), vehrefSpeed.getTimeStamp(), 
				vehDiseredDistance, refLength, currentTime, cond, cond1, cond2, cond3, cond4);
		LOGGER.info(str);

		return false;
	}

	private void setLogger() {
		boolean append = true;
		try {
			FileHandler handler = new FileHandler("./logs/bayes_unc.csv", append);
			LOGGER.addHandler(handler);
			LOGGER.setUseParentHandlers(false);
			Formatter simpleFormatter = new LoggerFormatter();
			handler.setFormatter(simpleFormatter);
			LOGGER.info("vehicleID,vehicleLID,"
					+ "diffBrakingDistanceMin,diffBrakingDistanceMax,diffBrakingDistanceValue,"
					+ "brakingDistanceMin,brakingDistanceMax,brakingDistanceValue,"
					+ "distanceMin,distanceMax,distanceValue,distanceTimestamp,"
					+ "distanceCACCValue,distanceCACCTimestamp,"
					+ "distanceACCValue,distanceACCTimestamp,"
					+ "speedCACCValue,speedCACCTimestamp,"
					+ "speedACCValue,speedACCTimestamp,"
					+ "wifiDistanceValue,wifiDistanceTimestamp," 
					+ "wifiSpeedValue,wifiSpeedTimestamp,"
					+ "radarDistanceValue,radarDistanceTimestamp,"
					+ "radarSpeedValue,radarSpeedTimestamp,"
					+ "lidarDistanceValue,lidarDistanceTimestamp,"
					+ "lidarSpeedValue,lidarSpeedTimestamp,"
					+ "cameraDistanceValue,cameraDistanceTimestamp,"
					+ "cameraSpeedValue,cameraSpeedTimestamp,"
					+ "outsCACCValue,outsCACCTimestamp,"
					+ "outsACCValue,outsACCTimestamp,delayDistanceMean,"
					+ "vehPosMin,vehPosMax,vehPosValue,vehPosTimestamp,"
					+ "vehSpeedMin,vehSpeedMax,vehSpeedValue,vehSpeedTimestamp,"
					+ "refPosMin,refPosMax,refPosValue,refPosTimestamp,"
					+ "refSpeedMin,refSpeedMax,refSpeedValue,refSpeedTimestamp,"
					+ "vehDiseredDistance,refLength,currentTime,"
					+ "normalCondition,minCondition,statisticalCondition,predictionCondition,bayesCondition");

		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private void conditionEvaluation() {
////		System.out.println(inputsACC);
////		System.err.println(outputsACC);
//
//		int w = 2000;
////		System.err.println(corrCACC+" \n "+corrACC);
//		for (Double time : pos.keySet()) {
//			HashMap<String, TreeMap<Double, Double>> insCACC = new HashMap<>();
//			HashMap<String, TreeMap<Double, Double>> insACC = new HashMap<>();
//			for (String str : inputsCACC.keySet()) {
//				insCACC.put(str, new TreeMap(inputsCACC.get(str).subMap(time-w, time)));
//			}
//			for (String str : inputsACC.keySet()) {
//				insACC.put(str, new TreeMap(inputsACC.get(str).subMap(time-w, time)));
//			}
//
//			TreeMap<Double, Double> outsCACC = new TreeMap(outputsCACC.subMap(time-w, time));
//			TreeMap<Double, Double> outsACC = new TreeMap(outputsACC.subMap(time-w, time));
//
//			corrCACC.putAll(BayesianOperators.fcorr(insCACC, outsCACC, 5.0, w, 0));
//			corrACC.putAll(BayesianOperators.fcorr(insACC, outsACC, 7.0, w, 0));
//			condition.put(time,
//					BayesianOperators.fbelowcorr(insCACC, outsCACC, 5.0, insACC, outsACC, 7.0, w, 0));
//		}
//		System.out.println(vehicleID + " :\nCACC" + corrCACC.keySet() + "\nACC" + corrACC.keySet() + "\nCondition" + condition.keySet());
//	}

}