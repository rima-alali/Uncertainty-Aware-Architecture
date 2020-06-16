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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataRange;

public class StateSpaceModelOperations {
	
	protected static State[] states;
//	protected static Double[] params;
	protected static Double lastTimeInitiated;
	protected static DataRange foundation;
	
	public StateSpaceModelOperations(int stateslength) {
		states = new State[stateslength];
		lastTimeInitiated = 0.0;
		foundation = new DataRange();
		
	}
	
	
	public void copyModelInfo(StateSpaceModel model) {
		for (int i = 0; i < model.getStatesCount(); i++) {
			states[i] = new State(model.getState(i).getName());
			states[i].setData(model.getState(i).getDataRange());
		}
//		params = model.getParams();
		foundation.setDataRange(model.getBasicDataRange());
		lastTimeInitiated = model.getLastTimeInitiated();
//		System.out.println("copy states ... "+foundation.toString());

	}
	
	public void pastModelInfo(StateSpaceModel model) {
		for (int i = 0; i < model.getStatesCount(); i++) {
			model.setState(i, states[i]);
		}
		model.setBasicDataRange(new DataRange(foundation.getData(), foundation.getMin(), foundation.getMax(), foundation.getTimeStamp()));
		model.setLastTimeInitiated(lastTimeInitiated);
//		System.out.println("past states ... "+model.getBasicDataRange().toString());
	}
}
