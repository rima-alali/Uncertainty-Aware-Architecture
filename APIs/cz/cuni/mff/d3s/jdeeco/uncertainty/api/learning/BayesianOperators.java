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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.learning;

import java.util.HashMap;
import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;

/**
 * 
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class BayesianOperators {

	protected static int windowCnt = 10; // SEC -> MILISEC
	protected static int windowSize = 1000; // SEC -> MILISEC
	protected static double confidence = 0.95;
	protected static int dim = 3;

	/**
	 * 
	 * 
	 * @param inputs1
	 * @param outputs1
	 * @param y1
	 * @param inputs2
	 * @param outputs2
	 * @param y2
	 * @param w
	 * @return
	 */
	public static boolean belowcorr(HashMap<String, TreeMap<Double, Double>> inputs1, TreeMap<Double, Double> outputs1,
			Double y1, HashMap<String, TreeMap<Double, Double>> inputs2, TreeMap<Double, Double> outputs2, Double y2,
			int w) {
		TreeMap<Double, Double> corr1 = corr(inputs1, outputs1, y1, w);
		TreeMap<Double, Double> corr2 = corr(inputs2, outputs2, y2, w);
//		System.err.println("corr1: "+corr1+" , corr2: "+corr2 +"  "+inputs1);
		return StatisticalOperators.fbelow(corr1, corr2, w, 0, windowCnt, windowSize);
//		return false;
	}
	
	/**
	 * 
	 * 
	 * @param inputs1
	 * @param outputs1
	 * @param y1
	 * @param inputs2
	 * @param outputs2
	 * @param y2
	 * @param w
	 * @return
	 */
	public static boolean fabovecorr(HashMap<String, TreeMap<Double, Double>> inputs1, TreeMap<Double, Double> outputs1,
			Double y1, HashMap<String, TreeMap<Double, Double>> inputs2, TreeMap<Double, Double> outputs2, Double y2,
			int w) {
		TreeMap<Double, Double> corr1 = corr(inputs1, outputs1, y1, w);
		TreeMap<Double, Double> corr2 = corr(inputs2, outputs2, y2, w);
//		System.err.println("corr1: "+corr1+" , corr2: "+corr2 +"  "+inputs1);
		return StatisticalOperators.fabove(corr1, corr2, w, 0, windowCnt, windowSize);
	}

	/**
	 * 
	 * 
	 * @param inputs
	 * @param outputs
	 * @param y
	 * @param w
	 * @return
	 */
	public static TreeMap<Double, Double> corr(HashMap<String, TreeMap<Double, Double>> inputs,
			TreeMap<Double, Double> outputs, Double y, int w) {
		BayesianModel bayesmodel = new BayesianModel();
		bayesmodel.train(inputs, outputs, w);
//		System.err.println(bayesmodel.intervals);
		TreeMap<Double, Double> corr = bayesmodel.corr(y, w, windowCnt, windowSize);
		return corr;
	}
	
	/*
	 * future
	 */

	
	/**
	 * 
	 * 
	 * @param inputs1
	 * @param outputs1
	 * @param y1
	 * @param inputs2
	 * @param outputs2
	 * @param y2
	 * @param w
	 * @param future
	 * @return
	 */
	public static boolean fbelowcorr(HashMap<String, TreeMap<Double, Double>> inputs1, TreeMap<Double, Double> outputs1,
			Double y1, HashMap<String, TreeMap<Double, Double>> inputs2, TreeMap<Double, Double> outputs2, Double y2,
			int w, int future) {
		TreeMap<Double, Double> corr1 = corr(inputs1, outputs1, y1, w);
		TreeMap<Double, Double> corr2 = corr(inputs2, outputs2, y2, w);
//		System.err.println("corr1: "+corr1+" , corr2: "+corr2 +"  "+inputs1);
		return StatisticalOperators.fbelow(corr1, corr2, w, future, windowCnt, windowSize);
//		return false;
	}

	/**
	 * 
	 * 
	 * @param inputs1
	 * @param outputs1
	 * @param y1
	 * @param inputs2
	 * @param outputs2
	 * @param y2
	 * @param w
	 * @param future
	 * @return
	 */
	public static boolean fabovecorr(HashMap<String, TreeMap<Double, Double>> inputs1, TreeMap<Double, Double> outputs1,
			Double y1, HashMap<String, TreeMap<Double, Double>> inputs2, TreeMap<Double, Double> outputs2, Double y2,
			int w, int future) {
		TreeMap<Double, Double> corr1 = corr(inputs1, outputs1, y1, w);
		TreeMap<Double, Double> corr2 = corr(inputs2, outputs2, y2, w);
//		System.err.println("corr1: "+corr1+" , corr2: "+corr2 +"  "+inputs1);
		return StatisticalOperators.fabove(corr1, corr2, w, future, windowCnt, windowSize);
	}
	

	/*
	 * time customization
	 */
	
	/**
	 * 
	 * 
	 * @param inputs1
	 * @param outputs1
	 * @param y1
	 * @param inputs2
	 * @param outputs2
	 * @param y2
	 * @param w
	 * @param future
	 * @param windowCnt
	 * @param windowSize
	 * @return
	 */
	public static boolean fbelowcorr(HashMap<String, TreeMap<Double, Double>> inputs1, TreeMap<Double, Double> outputs1,
			Double y1, HashMap<String, TreeMap<Double, Double>> inputs2, TreeMap<Double, Double> outputs2, Double y2,
			int w, int future, int windowCnt, int windowSize) {
		TreeMap<Double, Double> corr1 = corr(inputs1, outputs1, y1, w);
		TreeMap<Double, Double> corr2 = corr(inputs2, outputs2, y2, w);
//		System.err.println("corr1: "+corr1+" , corr2: "+corr2 +"  "+inputs1);
		return StatisticalOperators.fbelow(corr1, corr2, w, future, windowCnt, windowSize);
//		return false;
	}

	/**
	 * 
	 * 
	 * @param inputs1
	 * @param outputs1
	 * @param y1
	 * @param inputs2
	 * @param outputs2
	 * @param y2
	 * @param w
	 * @param future
	 * @param windowCnt
	 * @param windowSize
	 * @return
	 */
	public static boolean fabovecorr(HashMap<String, TreeMap<Double, Double>> inputs1, TreeMap<Double, Double> outputs1,
			Double y1, HashMap<String, TreeMap<Double, Double>> inputs2, TreeMap<Double, Double> outputs2, Double y2,
			int w, int future, int windowCnt, int windowSize) {
		TreeMap<Double, Double> corr1 = corr(inputs1, outputs1, y1, w);
		TreeMap<Double, Double> corr2 = corr(inputs2, outputs2, y2, w);
//		System.err.println("corr1: "+corr1+" , corr2: "+corr2 +"  "+inputs1);
		return StatisticalOperators.fabove(corr1, corr2, w, future, windowCnt, windowSize);
	}
	
	/**
	 * 
	 * 
	 * @param inputs
	 * @param outputs
	 * @param y
	 * @param w
	 * @param windowCnt
	 * @param windowSize
	 * @return
	 */
	public static TreeMap<Double, Double> corr(HashMap<String, TreeMap<Double, Double>> inputs,
			TreeMap<Double, Double> outputs, Double y, int w, int windowCnt, int windowSize) {
		BayesianModel bayesmodel = new BayesianModel();
		bayesmodel.train(inputs, outputs, w);
//		System.err.println(bayesmodel.intervals);
		TreeMap<Double, Double> corr = bayesmodel.corr(y, w, windowCnt, windowSize);
		return corr;
	}
	
}
