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

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.TimeState;

/**
 * This class provides a number of operations to calculate the real values of
 * stale data of physical entity.
 * 
 * The developer should provide a state-space model and the values in ORDER. The
 * class allows to call the minimum and maximum values as well as the interval
 * between. Additionally, it is possible to calculate the value of one state
 * depending on a specific future value for other state.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class InaccuracyOperators {

	private static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	
	public static double minusMins(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusMins(state1, model1, state2, model2, 0.0);
	}

	public static double minusMaxs(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusMaxs(state1, model1, state2, model2, 0.0);
	}

	public static double minusMaxMin(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusMaxMin(state1, model1, state2, model2, 0.0);
	}
	
	public static double minusMinMax(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusMinMax(state1, model1, state2, model2, 0.0);
	}
	
	public static double minusValues(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusValues(state1, model1, state2, model2, 0.0);
	}

	public static double minusInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusInterval(state1, model1, state2, model2, 0.0);
	}
	
	public static double minusxInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusxInterval(state1, model1, state2, model2, 0.0);
	}

	public static State minus(State state1, State state2, double timestamp) {
		State s = new State("Difference", 0.0);
		double val = state1.getData() - state2.getData();
		double min = state1.getMinBound() - state2.getMinBound();
		double max = state1.getMaxBound() - state2.getMaxBound();
		s.setData(val, min, max, timestamp);
		return s;
	}
	
	public static State minus(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminus(state1, model1, state2, model2, 0.0);
	}

	public static State minusx(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fminusx(state1, model1, state2, model2, 0.0);
	}
	
	
	public static double plusMins(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fplusMins(state1, model1, state2, model2, 0.0);
	}

	public static double plusMaxs(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fplusMaxs(state1, model1, state2, model2, 0.0);
	}

	public static double plusMaxMin(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fplusMaxMin(state1, model1, state2, model2, 0.0);
	}
	
	public static double plusValues(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fplusValues(state1, model1, state2, model2, 0.0);
	}
	
	public static double plusInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fplusInterval(state1, model1, state2, model2, 0.0);
	}

	public static State plus(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2) {
		return fplus(state1, model1, state2, model2, 0.0);
	}

	/*
	 * future
	 */

	public static double fminusMins(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusMins(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static double fminusMaxs(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusMaxs(state1, model1, state2, model2, future, 1.0, currentTime);
	}
	
	public static double fminusMinMax(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusMinMax(state1, model1, state2, model2, future, 1.0, currentTime);
	}
	
	public static double fminusMaxMin(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusMaxMin(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static double fminusValues(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusValues(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static double fminusInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusInterval(state1, model1, state2, model2, future, 1.0, currentTime);
	}
	
	public static double fminusxInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusxInterval(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static State fminus(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminus(state1, model1, state2, model2, future, 1.0, currentTime);
	}
	
	public static State fminusx(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fminusx(state1, model1, state2, model2, future, 1.0, currentTime);
	}
	

	
	public static double fplusMins(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fplusMins(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static double fplusMaxs(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fplusMaxs(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static double fplusMaxMin(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fplusMaxMin(state1, model1, state2, model2, future, 1.0, currentTime);
	}
	
	public static double fplusValues(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fplusValues(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static double fplusInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fplusInterval(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	public static State fplus(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fplus(state1, model1, state2, model2, future, 1.0, currentTime);
	}

	
	/*
	 * time and scale
	 */

	public static double fminusMinMax(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmin(state1, model1, future, scale, timestamp)
				- InaccuracyEvaluation.fmax(state2, model2, future, scale, timestamp);
	}

	public static double fminusMaxMin(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmax(state1, model1, future, scale, timestamp)
				- InaccuracyEvaluation.fmin(state2, model2, future, scale, timestamp);
	}

	public static double fminusMins(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmin(state1, model1, future, scale, timestamp)
				- InaccuracyEvaluation.fmin(state2, model2, future, scale, timestamp);
	}

	public static double fminusMaxs(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmax(state1, model1, future, scale, timestamp)
				- InaccuracyEvaluation.fmax(state2, model2, future, scale, timestamp);
	}
	
	public static double fminusValues(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fvalue(state1, model1, future, scale, timestamp)
				- InaccuracyEvaluation.fvalue(state2, model2, future, scale, timestamp);
	}

	public static double fminusInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return fminusMaxs(state1, model1, state2, model2, future, scale, timestamp)
				- fminusMins(state1, model1, state2, model2, future, scale, timestamp);
	}

	public static double fminusxInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return fminusMaxMin(state1, model1, state2, model2, future, scale, timestamp)
				- fminusMinMax(state1, model1, state2, model2, future, scale, timestamp);
	}
	
	public static State fminus(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		State s = new State("Difference", 0.0);
//		System.out.println(state1+" "+model1+" "+state2+" "+model2+" "+future+" "+scale+" "+timestamp);
		double val = fminusValues(state1, model1, state2, model2, future, scale, timestamp);
		double min = fminusMins(state1, model1, state2, model2, future, scale, timestamp);
		double max = fminusMaxs(state1, model1, state2, model2, future, scale, timestamp);
		System.out.println(val+"  "+min+"  "+max);
		s.setData(val, min, max, timestamp);
		return s;
	}

	
	public static State fminusx(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		State s = new State("Difference", 0.0);
		double val = fminusValues(state1, model1, state2, model2, future, scale, timestamp);
		double min = fminusMinMax(state1, model1, state2, model2, future, scale, timestamp);
		double max = fminusMaxMin(state1, model1, state2, model2, future, scale, timestamp);
		s.setData(val, min, max, timestamp);
		return s;
	}
	
	
	
	public static double fplusMins(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmin(state1, model1, future, scale, timestamp)
				+ InaccuracyEvaluation.fmin(state2, model2, future, scale, timestamp);
	}

	public static double fplusMaxs(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmax(state1, model1, future, scale, timestamp)
				+ InaccuracyEvaluation.fmax(state2, model2, future, scale, timestamp);
	}

	public static double fplusMaxMin(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fmax(state1, model1, future, scale, timestamp)
				+ InaccuracyEvaluation.fmin(state2, model2, future, scale, timestamp);
	}
	
	public static double fplusValues(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return InaccuracyEvaluation.fvalue(state1, model1, future, scale, timestamp)
				+ InaccuracyEvaluation.fvalue(state2, model2, future, scale, timestamp);
	}

	public static double fplusInterval(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2,
			double future, double scale, double timestamp) {
		return fplusMaxs(state1, model1, state2, model2, future, scale, timestamp)
				- fplusMins(state1, model1, state2, model2, future, scale, timestamp);
	}
	
	public static State fplus(State state1, StateSpaceModel model1, State state2, StateSpaceModel model2, double future,
			double scale, double timestamp) {
		State s = new State("Sum", 0.0);
		double val = fplusValues(state1, model1, state2, model2, future, scale, timestamp);
		double min = fplusMins(state1, model1, state2, model2, future, scale, timestamp);
		double max = fplusMaxs(state1, model1, state2, model2, future, scale, timestamp);
		s.setData(val, min, max, timestamp);
		return s;
	}	
	
	/*
	 * corresponding state value
	 */

	public static double minuscvLowers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return minuscvLowers(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}

	public static double minuscvUppers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return minuscvUppers(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}
	
	public static double minuscvUL(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return minuscvUL(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}
	
	public static double minuscvLU(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return minuscvLU(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}

	
	public static double minuscvLowers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvLower(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				- InaccuracyEvaluation.cvLower(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}

	public static TimeState minuscvLowersState(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		
		TimeState s1 = InaccuracyEvaluation.cvLowerState(state1, requiredVal1, targetState1, model1, scale,
				timestamp);
//		System.out.println();
		TimeState s2 = InaccuracyEvaluation.cvLowerState(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
//		System.err.println(s1.toString()+"   "+s2.toString());
		TimeState result = new TimeState("Minus");
		result.setDataTimeStamp(s1.getDataTimeStamp().getData() - s2.getDataTimeStamp().getData(), s1.getDataTimeStamp().getTimestamp() - s2.getDataTimeStamp().getTimestamp());
		result.setMin(s1.getMin().getData() - s2.getMin().getData(), s1.getMin().getTimestamp() - s2.getMin().getTimestamp());
		result.setMax(s1.getMax().getData() - s2.getMax().getData(), s1.getMax().getTimestamp() - s2.getMax().getTimestamp());
		result.setTimestamp(timestamp);
		return result;
	}
	
	
	public static double minuscvUppers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvUpper(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				- InaccuracyEvaluation.cvUpper(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}

	public static double minuscvUL(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvUpper(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				- InaccuracyEvaluation.cvLower(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}
	
	public static double minuscvLU(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvLower(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				- InaccuracyEvaluation.cvUpper(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}
	
	
	
	
	public static double pluscvLowers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return pluscvLowers(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}

	public static double pluscvUppers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return pluscvUppers(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}

	public static double pluscvUL(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return pluscvUL(state1, requiredVal1, targetState1, model1, state2, requiredVal2,
				targetState2, model2, 1.0, currentTime);
	}
	
	
	public static double pluscvLowers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvLower(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				+ InaccuracyEvaluation.cvLower(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}

	public static double pluscvUppers(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvUpper(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				+ InaccuracyEvaluation.cvUpper(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}
	
	public static double pluscvUL(State state1, double requiredVal1, State targetState1,
			StateSpaceModel model1, State state2, double requiredVal2, State targetState2, StateSpaceModel model2,
			double scale, double timestamp) {
		return InaccuracyEvaluation.cvUpper(state1, requiredVal1, targetState1, model1, scale,
				timestamp)
				+ InaccuracyEvaluation.cvLower(state2, requiredVal2, targetState2, model2, scale,
						timestamp);
	}

}
