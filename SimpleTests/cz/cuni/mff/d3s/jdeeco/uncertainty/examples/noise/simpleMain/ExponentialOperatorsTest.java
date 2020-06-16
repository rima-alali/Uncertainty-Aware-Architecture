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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.noise.simpleMain;

import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.exponitial.IntervalModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.exponitial.ExponentialOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyEvaluation;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.ScenarioDataset;

public class ExponentialOperatorsTest {

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;
	protected static final double KM_MILLI_FACTOR = 0.000000278; // m per millisec

	public static void main(String[] args) {
		double timestamp = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		ScenarioDataset sc = new ScenarioDataset();
		double scale = 1000;
		TreeMap<Double, IntervalModel> tree = new TreeMap<Double, IntervalModel>();
		IntervalModel din = new IntervalModel();
		// --------------- Vehicle 1 -------------------------------
		State pos = new State("pos", 10.0, timestamp);
		State speed = new State("speed", 90.0, timestamp);
		VehicleStateSpaceModel model = new VehicleStateSpaceModel(KM_MILLI_FACTOR, 2, sc.getTorques(),
				sc.getRouteSlops(), sc.getMass(), timestamp, speed, pos);
		model.setParams(0.5, 0.0);
		model.createBasicDataRange(timestamp);

		// ------------- Methods ----------------------------------
		double currentTimestamp = timestamp;
		din.add(0, pos.getInterval());

		State[] states = InaccuracyEvaluation.fstates(model, 0, scale, currentTimestamp + 50);
//		model.updateStates(states[0],states[1]);
		din.add(50, states[1].getInterval());

		states = InaccuracyEvaluation.fstates(model, 0, scale, currentTimestamp + 100);
//		model.updateStates(states[0],states[1]);
		din.add(100, states[1].getInterval());
		tree.put(currentTimestamp, din);

//		currentTimestamp = currentTimestamp + 200;
		din = new IntervalModel();
		states = InaccuracyEvaluation.fstates(model, 0, scale, currentTimestamp + 200);
		model.initiateStates(currentTimestamp + 200, states[0], states[1]);
		din.add(0, model.getState(1).getInterval());

//		model.updateStates(speed, pos);
		states = InaccuracyEvaluation.fstates(model, 0, scale, currentTimestamp + 250);
		din.add(50, states[1].getInterval());

//		model.updateStates(speed, pos);
		states = InaccuracyEvaluation.fstates(model, 0, scale, currentTimestamp + 300);
//		model.updateStates(states[0],states[1]);
		din.add(100, states[1].getInterval());
		tree.put(currentTimestamp + 200, din);

		for (Double key : tree.keySet()) {
			System.out.println("key: " + key + " , values: \n " + tree.get(key).toString());
		}

		System.out.println("time ... " + (tree.lastKey() - currentTimestamp + 100));
		System.out.println("mean future 0: "+ExponentialOperators.fexpMean(tree, (int) (tree.lastKey() - tree.firstKey()), 0) + " \n mean future 50:"
				+ ExponentialOperators.fexpMean(tree, (int) (tree.lastKey() - tree.firstKey()), 50) + " \n mean future 100:"
				+ ExponentialOperators.fexpMean(tree, (int) (tree.lastKey() - tree.firstKey()), 100));

	}

}
