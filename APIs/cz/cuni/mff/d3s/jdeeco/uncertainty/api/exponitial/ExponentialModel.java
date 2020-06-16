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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.exponitial;

import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;

/**
 * The class stores historical data of interval values that have cyclic nature.
 * In other words, the interval values are seasonal, where each period of time
 * the interval values reset and start to increase again. The historical periods
 * follow exponential distribution.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class ExponentialModel {

	protected TreeMap<Double, IntervalModel> delaysIntervals;

	/**
	 * The constructor initiates exponential model of intervals cycles.
	 */
	public ExponentialModel() {
		delaysIntervals = new TreeMap<>();
	}
	
	
	/**
	 * Add a set of intervals to the historical data 
	 * 
	 * @param x is the set of intervals
	 * @param w is the historical period
	 */
	public void addAll(TreeMap<Double, IntervalModel> x, int w) {
		for (Double key : x.keySet()) {
			if (delaysIntervals.get(key) == null) {
				IntervalModel din = new IntervalModel();
				din.setDelays(x.get(key).getDelays());
				delaysIntervals.put(key, din);
			} else {
				delaysIntervals.get(key).setDelays(x.get(key).getDelays());
			}
		}
	}

	
	/**
	 * The method returns the mean distribution of linear regression 
	 * 
	 * @param w is the historical period
	 * @param future is the prediction period
	 * @return the mean of the exponential distribution
	 */
	public TreeMap<Double, Double> computeMean(int w, int future) {
		TreeMap<Double, Double> means = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			Double mean = delaysIntervals.get(startTimestamp).mean();
			if (Double.isInfinite(mean) || Double.isNaN(mean))
				continue;
			means.put(startTimestamp, mean);
		}
		return means;
	}
	
	
	/**
	 * The method returns the mean at a specific future time
	 * 
	 * @param w is the historical period
	 * @param future is the prediction period
	 * @return the mean of the exponential distribution at point of future
	 */
	public double computeLambdaMean(int w, int future) {
		TreeMap<Double, Double> means = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			Double mean = delaysIntervals.get(startTimestamp).mean();
			if (Double.isInfinite(mean) || Double.isNaN(mean))
				continue;
			means.put(startTimestamp, mean);
		}
		return StatisticalOperators.flr(means, w, future).getMean();
	}
	
	
	/**
	 * 
	 * 
	 * @param y
	 * @param w
	 * @param future
	 * @return
	 */
	public double computeLambdaMean(Double y, int w, int future) {
		TreeMap<Double, Double> means = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			means.put(startTimestamp, delaysIntervals.get(startTimestamp).mean());
		}
		return StatisticalOperators.flr(means, y, w, future).getMean();
	}

	/**
	 * 
	 * 
	 * @param w
	 * @param future
	 * @param windowCnt
	 * @param windowsSize
	 * @return
	 */
	public double computeLambdaMean(int w, int future, int windowCnt, int windowsSize) {
		TreeMap<Double, Double> means = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			means.put(startTimestamp, delaysIntervals.get(startTimestamp).mean());
		}
		return StatisticalOperators.flr(means, w, future, windowCnt, windowsSize).getMean();
	}


	/**
	 * 
	 * 
	 * @param w
	 * @param future
	 * @return
	 */
	public double computeLambdaVariance(int w, int future) {
		TreeMap<Double, Double> variances = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			Double variance = delaysIntervals.get(startTimestamp).variance();
			if (Double.isInfinite(variance) || Double.isNaN(variance))
				continue;
			variances.put(startTimestamp, variance);
		}
		return StatisticalOperators.flr(variances, w, future).getVariance();
	}

	
	/**
	 * 
	 * 
	 * @param y
	 * @param w
	 * @param future
	 * @return
	 */
	public double computeLambdaVariance(Double y, int w, int future) {
		TreeMap<Double, Double> variances = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			variances.put(startTimestamp, delaysIntervals.get(startTimestamp).variance());
		}
		return StatisticalOperators.flr(variances, y, w, future).getVariance();
	}
	
		
	/**
	 * 
	 * 
	 * @param w
	 * @param future
	 * @return
	 */
	public double computeLambdaVariance(int w, int future, int windowCnt, int windowsSize) {
		TreeMap<Double, Double> variances = new TreeMap<>();
		for (Double startTimestamp : delaysIntervals.keySet()) {
			variances.put(startTimestamp, delaysIntervals.get(startTimestamp).variance());
		}
		return StatisticalOperators.flr(variances, w, future, windowCnt, windowsSize).getVariance();
	}
}
