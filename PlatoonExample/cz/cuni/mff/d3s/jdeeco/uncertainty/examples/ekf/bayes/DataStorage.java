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

import java.util.HashMap;
import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.learning.BayesianOperators;

public class DataStorage {
	public String vehicleID;
	public String vehicleLID;
	public TreeMap<Double, Double> pos = new TreeMap<>();
	public Double future = 0.0;
	public HashMap<String, TreeMap<Double, Double>> inputsCACC = new HashMap<String, TreeMap<Double, Double>>();
	public HashMap<String, TreeMap<Double, Double>> inputsACC = new HashMap<String, TreeMap<Double, Double>>();
	public TreeMap<Double, Double> outputsCACC = new TreeMap<Double, Double>();
	public TreeMap<Double, Double> outputsACC = new TreeMap<Double, Double>();
	public TreeMap<Double, Double> corrCACC = new TreeMap<Double, Double>();
	public TreeMap<Double, Double> corrACC = new TreeMap<Double, Double>();
	public TreeMap<Double, Boolean> condition = new TreeMap<Double, Boolean>();
	public TreeMap<Double, Boolean> conditionfuture = new TreeMap<Double, Boolean>();

	public DataStorage(String id) {
		vehicleID = id;
		setHistory();
	}

	public void setHistory() {
		inputsCACC.put("wifi", new TreeMap<Double, Double>());
		inputsCACC.put("radar", new TreeMap<Double, Double>());
		inputsCACC.put("lidar", new TreeMap<Double, Double>());
		inputsCACC.put("camera", new TreeMap<Double, Double>());

		inputsACC.put("radar", new TreeMap<Double, Double>());
		inputsACC.put("lidar", new TreeMap<Double, Double>());
		inputsACC.put("camera", new TreeMap<Double, Double>());

	}

	public void createData(String[] data) {
		String vehicleID = data[0];
		String vehicleLID = data[1];

//		System.out.println(data[12]);
		Double distanceCACCValue = Double.parseDouble(data[12]);
		Double distanceCACCTimestamp = Double.parseDouble(data[13]);
		Double distanceACCValue = Double.parseDouble(data[14]);
		Double distanceACCTimestamp = Double.parseDouble(data[15]);

		Double speedCACCValue = Double.parseDouble(data[16]);
		Double speedCACCTimestamp = Double.parseDouble(data[17]);
		Double speedACCValue = Double.parseDouble(data[18]);
		Double speedACCTimestamp = Double.parseDouble(data[19]);

		Double wifiDistanceValue = Double.parseDouble(data[20]);
		Double wifiDistanceTimestamp = Double.parseDouble(data[21]);
		Double wifiSpeedValue = Double.parseDouble(data[22]);
		Double wifiSpeedTimestamp = Double.parseDouble(data[23]);

		Double radarDistanceValue = Double.parseDouble(data[24]);
		Double radarDistanceTimestamp = Double.parseDouble(data[25]);
		Double radarSpeedValue = Double.parseDouble(data[26]);
		Double radarSpeedTimestamp = Double.parseDouble(data[27]);

		Double lidarDistanceValue = Double.parseDouble(data[28]);
		Double lidarDistanceTimestamp = Double.parseDouble(data[29]);
		Double lidarSpeedValue = Double.parseDouble(data[30]);
		Double lidarSpeedTimestamp = Double.parseDouble(data[31]);

		Double cameraDistanceValue = Double.parseDouble(data[32]);
		Double cameraDistanceTimestamp = Double.parseDouble(data[33]);
		Double cameraSpeedValue = Double.parseDouble(data[34]);
		Double cameraSpeedTimestamp = Double.parseDouble(data[35]);

		Double outsCACCValue = Double.parseDouble(data[36]);
		Double outsCACCTimestamp = Double.parseDouble(data[37]);
		Double outsACCValue = Double.parseDouble(data[38]);
		Double outsACCTimestamp = Double.parseDouble(data[39]);
		future = Double.parseDouble(data[40]);

		Double posValue = Double.parseDouble(data[43]);
		Double posTimestamp = Double.parseDouble(data[44]);
		
//		System.out.println(outsCACCValue+"  "+outsCACCTimestamp+"  "+outsACCValue+"  "+outsACCTimestamp);
		inputsCACC.get("wifi").put(wifiDistanceTimestamp, wifiDistanceValue);
		inputsCACC.get("radar").put(radarDistanceTimestamp, radarDistanceValue);
		inputsCACC.get("lidar").put(lidarDistanceTimestamp, lidarDistanceValue);
		inputsCACC.get("camera").put(cameraDistanceTimestamp, cameraDistanceValue);

		inputsACC.get("radar").put(radarDistanceTimestamp, radarDistanceValue);
		inputsACC.get("lidar").put(lidarDistanceTimestamp, lidarDistanceValue);
		inputsACC.get("camera").put(cameraDistanceTimestamp, cameraDistanceValue);

		outputsCACC.put(outsCACCTimestamp, outsCACCValue);
		outputsACC.put(outsACCTimestamp, outsACCValue);
		
		pos.put(posTimestamp, posValue);

	}

	public void conditionEvaluation() {
//		System.out.println(inputsACC);
//		System.err.println(outputsACC);

		int w = 10000;
//		System.err.println(corrCACC+" \n "+corrACC);
		for (Double time : pos.keySet()) {
			HashMap<String, TreeMap<Double, Double>> insCACC = new HashMap<>();
			HashMap<String, TreeMap<Double, Double>> insACC = new HashMap<>();
			for (String str : inputsCACC.keySet()) {
				insCACC.put(str, new TreeMap(inputsCACC.get(str).subMap(time-w, time)));
			}
			for (String str : inputsACC.keySet()) {
				insACC.put(str, new TreeMap(inputsACC.get(str).subMap(time-w, time)));
			}

			TreeMap<Double, Double> outsCACC = new TreeMap(outputsCACC.subMap(time-w, time));
			TreeMap<Double, Double> outsACC = new TreeMap(outputsACC.subMap(time-w, time));

			corrCACC.putAll(BayesianOperators.corr(insCACC, outsCACC, 5.0, w));
			corrACC.putAll(BayesianOperators.corr(insACC, outsACC, 7.0, w));
			condition.put(time,
					BayesianOperators.fbelowcorr(insCACC, outsCACC, 5.0, insACC, outsACC, 7.0, w, 0));
			conditionfuture.put(time,
					BayesianOperators.fbelowcorr(insCACC, outsCACC, 5.0, insACC, outsACC, 7.0, w, 10000));
		}
		System.out.println(vehicleID + " :\nCACC" + corrCACC.keySet() + "\nACC" + corrACC.keySet() + "\nCondition" + condition.keySet());
	}

}
