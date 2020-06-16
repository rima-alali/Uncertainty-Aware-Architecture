/*******************************************************************************
 * Copyright 2017 Charles University in Prague
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

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.modes.Transition;

/**
 * TODO: develop farther depending on the paper??? (wait till all the processes finish the work and then switch???)
 *       maybe also the weight of the modes here???
 *
 */
public class VehicleTransitionListener implements DEECoTransitionListener {

	private final DEECoMode fromMode;
	private final DEECoMode toMode;
	
	public VehicleTransitionListener(Transition transition){
		this.fromMode = transition.getFrom();
		this.toMode = transition.getTo();
	}
	
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener#getKnowledgeNames()
	 */
	@Override
	public String[] getKnowledgeNames() {
		return new String[]{"id", "canTransit", "mode"};
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener#transitionTaken(cz.cuni.mff.d3s.deeco.task.ParamHolder[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void transitionTaken(ParamHolder<?>[] knowledgeValues) {
		// Forbid next transit
		ParamHolder<Boolean> canTransit = (ParamHolder<Boolean>) knowledgeValues[1];
		canTransit.value = false;
		
		// Assign current mode
		ParamHolder<VehicleModeEnum> inMode = (ParamHolder<VehicleModeEnum>) knowledgeValues[2];
		if(toMode.equals(new CACC())){
			inMode.value = VehicleModeEnum.CACC;
		} else if(toMode.equals(new ACC())){
			inMode.value = VehicleModeEnum.ACC;
		} else if(toMode.equals(new AWS())){
			inMode.value = VehicleModeEnum.AWS;
		} 
	}

}
