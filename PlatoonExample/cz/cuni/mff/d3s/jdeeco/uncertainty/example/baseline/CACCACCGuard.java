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

import cz.cuni.mff.d3s.deeco.modes.DEECoModeGuard;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
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
		return new String[] { "mode", "brakingDistance", "distance", "id", "lid", "vehPos", "vehSpeed", "refPos", "refSpeed","reactionDistance","refLength"};
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSatisfied(Object[] knowledgeValues) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;

		Double brakingDistance = ((Double) knowledgeValues[1]);
		State distance = ((State) knowledgeValues[2]);
		String id = ((String) knowledgeValues[3]);
	    
//		System.err.println("cond ... "+brakingDistance+" > "+distance.getMinBound()+"  "+(brakingDistance > distance.getMinBound()));
		//time is in second
	    String str = String.format("%s,%f,%f,%f,%f,%f,%f",id,brakingDistance,distance.getMinBound(), distance.getMaxBound(), distance.getData(), distance.getTimeStamp(), currentTime);
		LOGGER.info(str);

		return false;
	}

	private void setLogger() {
		boolean append = true;
		try {
			FileHandler handler = new FileHandler("./logs/distancebasic.csv", append);
			LOGGER.addHandler(handler);
			LOGGER.setUseParentHandlers(false);
			Formatter simpleFormatter = new LoggerFormatter();
			handler.setFormatter(simpleFormatter);
			LOGGER.info("name,brakingDistance,distanceMin,distanceMax,distanceValue, currentTime,timeStamp");

		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}