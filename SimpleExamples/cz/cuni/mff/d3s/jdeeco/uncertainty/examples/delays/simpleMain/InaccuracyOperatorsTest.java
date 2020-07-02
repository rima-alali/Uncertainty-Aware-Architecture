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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.delays.simpleMain;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyEvaluation;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.ScenarioDataset;

/**
 * 
 * Simple example of calling the Inaccuracy API It is worth mentioning that the
 * 
 * time should be in millisecond.
 * 
 * 
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 * 
 * 
 */

public class InaccuracyOperatorsTest {

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;
	protected static final double KM_MILLI_FACTOR = 0.000000278; // m per millisec

	public static void main(String[] args) throws InterruptedException {
		double timestamp = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		ScenarioDataset sc = new ScenarioDataset();
		double scale = 1000;
		//--------------- Vehicle 1 -------------------------------
		State pos = new State("pos", 10.0, timestamp);
		State speed = new State("speed", 90.0, timestamp);
		VehicleStateSpaceModel model = new VehicleStateSpaceModel(KM_MILLI_FACTOR, 2, sc.getTorques(), sc.getRouteSlops(),
				sc.getMass(), timestamp, speed, pos);
		model.setParams(0.5, 0.0);
		model.createBasicDataRange(timestamp);
		//--------------- Vehicle 2 -------------------------------
		State pos2 = new State("pos2", 20.0, timestamp);
		State speed2 = new State("speed2", 90.0, timestamp);
		VehicleStateSpaceModel model2 = new VehicleStateSpaceModel(KM_MILLI_FACTOR, 2, sc.getTorques(), sc.getRouteSlops(),
				sc.getMass(), timestamp, speed2, pos2);
		model2.setParams(0.5, 0.0);
		model2.createBasicDataRange(timestamp);
		
		
		Thread.sleep(1000);
		double currenttime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		
		
		//------------- Methods ----------------------------------
		System.out.println("pos ... " + pos.toString() + " \n  speed ... " + speed.toString());
		System.out.println(pos.toString()+"  "+pos2.toString());
		System.out.println("######################################################################################");
		System.out.println("inaccuracy interval ... pos " + InaccuracyEvaluation.interval(pos, model) + "  ... speed "
				+ InaccuracyEvaluation.interval(speed, model));
		System.out.println("min... pos " + InaccuracyEvaluation.fmin(pos, model, 0.0, scale, currenttime)
				+ "  speed " + InaccuracyEvaluation.fmin(speed, model, 0.0, scale, currenttime));
		System.out.println(
				"max ... " + InaccuracyEvaluation.max(pos, model) + " speed  "
						+ InaccuracyEvaluation.max(speed, model));
		


		System.out.println(pos.toString()+"  "+speed.toString());

		
		System.out.println("######################################################################################");
		System.out.println("future pos ... " + pos.getDataRange() + "   speed ... " + speed.getDataRange());
		System.out.println("inaccuracy ... " + InaccuracyEvaluation.finterval(pos, model, 5) + "  ... "
				+ InaccuracyEvaluation.finterval(speed, model, 5));
		System.out.println("min ... " + InaccuracyEvaluation.fmin(pos, model, 5) + " speed  "
				+ InaccuracyEvaluation.fmin(speed, model, 5) + "   .... state "
				+ InaccuracyEvaluation.fstates(model, 5)[0].toString());
		System.out.println("max ... " + InaccuracyEvaluation.fmax(pos, model, 5) + " speed  "
				+ InaccuracyEvaluation.fmax(speed, model, 5));
		
		System.out.println(pos.toString()+"  "+speed.toString());
		
		
		System.out.println("######################################################################################");
//		System.out.println("Vehicle1 : when speed = 0 then stop pos is ... "
//				+ InaccuracyEvaluation.cvLower(speed, 0.0, pos, model) + "  and when speed = 200 the pos is ... "
//				+ InaccuracyEvaluation.cvUpper(speed, 200.0, pos, model));
//		System.out.println("Vehicle2: when speed = 0 then stop pos is ... "
//				+ InaccuracyEvaluation.cvLower(speed2, 0.0, pos2, model2) + "  and when speed = 200 the pos is ... "
//				+ InaccuracyEvaluation.cvUpper(speed2, 200.0, pos2, model2));
//		System.out.println("Distance between Vehicle2 and Vehicle1: when speed = 0 then stop pos is ... "
//				+ InaccuracyOperators.minuscvLowers(speed2, 0.0, pos2, model2, speed, 0.0, pos, model, scale, currenttime)
//				+ "  and when speed = 200 the pos is ... "
//				+ InaccuracyOperators.minuscvUppers(speed2, 200.0, pos2, model2, speed, 200.0, pos, model, scale,
//						currenttime));
//		System.out.println("Distance between Vehicle2 and Vehicle1 : "
//				+ InaccuracyOperators.fminusx(pos2, model2, pos, model, 0.0, scale, currenttime));
//		
//		System.out.println("Vehicle1 marches the time elapse for 10 m from Vehicle2"+InaccuracyEvaluation.matchIntervalImpact(model2, pos, model, currenttime));
//		
//		
//		speed.updateBounds(50, 120, timestamp);
//		model.setStates(speed, pos);
		
		System.out.println("Distance between Vehicle2 and Vehicle1: when speed = 0 then : "+InaccuracyOperators.minuscvLowersState(speed2, 0, pos2, model2, speed, 0, pos, model, scale, currenttime));
//		
//		System.out.println(pos.toString()+"  "+speed.toString());

	}

}
