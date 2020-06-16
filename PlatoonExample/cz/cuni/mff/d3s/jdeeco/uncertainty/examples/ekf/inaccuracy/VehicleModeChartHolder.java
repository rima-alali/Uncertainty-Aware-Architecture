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

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.jdeeco.modes.ModeChartHolder;
import cz.cuni.mff.d3s.jdeeco.modes.Transition;

public class VehicleModeChartHolder extends ModeChartHolder {

	public VehicleModeChartHolder(){
//		System.out.println("holder ..... ");
		// Modes
		DEECoMode cacc = new CACC();
		DEECoMode acc = new ACC();
		DEECoMode aws = new AWS();
		Transition caccaccTrans = addTransition(cacc, acc, new CACCACCGuard());
		addTransitionListener(caccaccTrans, new VehicleTransitionListener(caccaccTrans));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.modes.ModeChartHolder#getInitialMode()
	 */
	@Override
	public DEECoMode getInitialMode() {
		
		return new CACC();
	}
}
