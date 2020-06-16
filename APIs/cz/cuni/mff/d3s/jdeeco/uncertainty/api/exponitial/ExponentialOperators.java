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

/**
 * The class presents a exponential distribution parameters i.e. its mean and
 * variance.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class ExponentialOperators {
	
	/**
	 * The method calculates the mean of set of time stamped values that follows
	 * exponential distribution
	 * 
	 * @param x is the time stamped values
	 * @param w is the historical period
	 * @return the mean of the distribution 
	 */
	public static double expMean(TreeMap<Double, IntervalModel> x, int w) {
		ExponentialModel e = new ExponentialModel();
		e.addAll(x, w);
		double meanDelays = e.computeLambdaMean(w, 0);
		if (meanDelays > 0 && !Double.isInfinite(meanDelays) && !Double.isNaN(meanDelays))
			return meanDelays;
		else
			return 0.0;
	}

	/**
	 * The method calculates the variance of set of time stamped values that follows
	 * exponential distribution
	 * 
	 * @param x is the time stamped values
	 * @param w is the historical period
	 * @return the variance of the distribution
	 */
	public static double expVariance(TreeMap<Double, IntervalModel> x, int w) {
		ExponentialModel e = new ExponentialModel();
		e.addAll(x, w);
		double varianceDelays = e.computeLambdaVariance(w, 0);
		if (varianceDelays > 0 && !Double.isInfinite(varianceDelays) && !Double.isNaN(varianceDelays))
			return varianceDelays;
		else
			return 0.0;
	}

	/**
	 * The method calculates the mean of set of time stamped values that follows
	 * exponential distribution
	 * 
	 * @param x is the time stamped values
	 * @param w is the historical period
	 * @param future is the future period
	 * @return the mean of the distribution 
	 */
	public static double fexpMean(TreeMap<Double, IntervalModel> x, int w, int future) {
		ExponentialModel e = new ExponentialModel();
		e.addAll(x, w);
		double meanDelays = e.computeLambdaMean(w, future);
		if (meanDelays > 0 && !Double.isInfinite(meanDelays) && !Double.isNaN(meanDelays))
			return meanDelays;
		else
			return 0.0;
	}

	/**
	 * The method calculates the variance of set of time stamped values that follows
	 * exponential distribution
	 * 
	 * @param x is the time stamped values
	 * @param w is the historical period
	 * @param future is the future period
	 * @return the variance of the distribution
	 */
	public static double fexpVariance(TreeMap<Double, IntervalModel> x, int w, int future) {
		ExponentialModel e = new ExponentialModel();
		e.addAll(x, w);
		double varianceDelays = e.computeLambdaVariance(w, future);
		if (varianceDelays > 0 && !Double.isInfinite(varianceDelays) && !Double.isNaN(varianceDelays))
			return varianceDelays;
		else
			return 0.0;
	}

	/**
	 * The method calculates the mean of set of time stamped values that follows
	 * exponential distribution
	 * 
	 * @param x is the time stamped values
	 * @param w is the historical period
	 * @param future is the future period
	 * @param windowCnt is the window count
	 * @param windowsSize is the window size
	 * @return the mean of the distribution 
	 */
	public static double fexpMean(TreeMap<Double, IntervalModel> x, int w, int future, int windowCnt, int windowsSize) {
		ExponentialModel e = new ExponentialModel();
		e.addAll(x, w);
		double meanDelays = e.computeLambdaMean(w, future, windowCnt, windowsSize);
		if (meanDelays > 0 && !Double.isInfinite(meanDelays) && !Double.isNaN(meanDelays))
			return meanDelays;
		else
			return 0.0;
	}

	/**
	 * The method calculates the variance of set of time stamped values that follows
	 * exponential distribution
	 * 
	 * @param x is the time stamped values
	 * @param w is the historical period
	 * @param future is the future period
	 * @param windowCnt is the window count
	 * @param windowsSize is the window size
	 * @return the variance of the distribution
	 */
	public static double fexpVariance(TreeMap<Double, IntervalModel> x, int w, int future, int windowCnt, int windowsSize) {
		ExponentialModel e = new ExponentialModel();
		e.addAll(x, w);
		double varianceDelays = e.computeLambdaVariance(w, future, windowCnt, windowsSize);
		if (varianceDelays > 0 && !Double.isInfinite(varianceDelays) && !Double.isNaN(varianceDelays))
			return varianceDelays;
		else
			return 0.0;
	}
}
