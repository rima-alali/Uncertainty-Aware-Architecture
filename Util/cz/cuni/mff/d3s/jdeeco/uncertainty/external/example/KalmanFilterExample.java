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
package cz.cuni.mff.d3s.jdeeco.uncertainty.external.example;

import cz.cuni.mff.d3s.jdeeco.uncertainty.external.KalmanFilter;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.Linear1dObservationModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.Linear1dProcessModel;

public class KalmanFilterExample {
	public static void main(String[] args) {
		Linear1dProcessModel model = new Linear1dProcessModel();
		Linear1dObservationModel obs = new Linear1dObservationModel();
		KalmanFilter filter = new KalmanFilter(model);

		for (int i = 0; i <= 10; ++i) {
			double time = i;
			obs.setPosition(i);
			filter.update(time, obs);
		}

		double x = model.getState()[0][0];
		double v = model.getState()[1][0];
		
		System.out.println(x+"   "+v);
	}
}
