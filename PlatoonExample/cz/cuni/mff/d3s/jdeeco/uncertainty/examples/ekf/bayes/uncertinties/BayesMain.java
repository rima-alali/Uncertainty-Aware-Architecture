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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.bayes.DataStorage;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.logger.LoggerFormatter;

public class BayesMain {

	protected ArrayList<DataStorage> data = new ArrayList<DataStorage>();
	private static final Logger LOGGER = Logger.getLogger(CACCACCGuard.class.getName());

	
	public static void main(String[] args) {
		BayesMain m = new BayesMain();
		m.setLogger();
		m.init();
		m.read();
		m.condition();
	}

	public void init() {
		for (int i = 1; i <= 3; i++) {
			data.add(new DataStorage("Vehicle"+i));
		}
	}
	
	public void read() {
		String csvFile = "./logs/bayes_unc.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			int lineNum = 0;
			while ((line = br.readLine()) != null) {
				lineNum++;
				if (lineNum < 4)
					continue;
				String[] data = line.split(cvsSplitBy);
				addRow(data);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addRow(String[] d) {
		int j = 0;
		String vehicleID = d[0];
		if(vehicleID.equals("Vehicle1"))
			j = 0;
		else if(vehicleID.equals("Vehicle2"))
			j = 1;
		else if(vehicleID.equals("Vehicle3"))
			j = 2;
		data.get(j).createData(d);
//		System.out.println("add "+vehicleID+"  "+j+"   "+data.get(j).vehicleID+"  "+d);
	}	

	public void condition() {
		for (DataStorage dataStorage : data) {
			dataStorage.conditionEvaluation();
			String str = "";
			for (Double time : dataStorage.corrCACC.keySet()) {
				str = dataStorage.vehicleID+","+dataStorage.pos.ceilingEntry(time).getValue()+","+dataStorage.pos.ceilingEntry(time).getKey()+",CACC,"+dataStorage.corrCACC.get(time)+","+time;
//				System.out.println(str);
				LOGGER.info(str);
			}
			for (Double time : dataStorage.corrACC.keySet()) {
				str = dataStorage.vehicleID+","+dataStorage.pos.ceilingEntry(time).getValue()+","+dataStorage.pos.ceilingEntry(time).getKey()+",ACC,"+dataStorage.corrACC.get(time)+","+time;
//				System.out.println(str);
				LOGGER.info(str);
			}
			for (Double time : dataStorage.condition.keySet()) {
				str = dataStorage.vehicleID+","+dataStorage.pos.ceilingEntry(time).getValue()+","+dataStorage.pos.ceilingEntry(time).getKey()+",Condition,"+dataStorage.condition.get(time)+","+time;
//				System.out.println(str);
				LOGGER.info(str);
			}
			for (Double time : dataStorage.conditionfuture.keySet()) {
				str = dataStorage.vehicleID+","+dataStorage.pos.ceilingEntry(time).getValue()+","+dataStorage.pos.ceilingEntry(time).getKey()+",ConditionFuture,"+dataStorage.conditionfuture.get(time)+","+time;
//				System.out.println(str);
				LOGGER.info(str);
			}
		}
	}
	
	
	public void setLogger() {
		boolean append = true;
		try {
			FileHandler handler = new FileHandler("./logs/bayes_corr_unc.csv", append);
			LOGGER.addHandler(handler);
			LOGGER.setUseParentHandlers(false);
			Formatter simpleFormatter = new LoggerFormatter();
			handler.setFormatter(simpleFormatter);
			LOGGER.info("vehicleID,posValue,posTimestamp,state,value,timestamp");

		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}