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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;

/**
 * The class provide a set of operators over TreeMap structure.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class TreeMapOperations {

	/**
	 * The method select a set of elements from TreeMap structure starting from
	 * startIndex to endIndex
	 * 
	 * @param x          is the data collection
	 * @param startIndex is the index of the first element
	 * @param endIndex   is the index of the end elements
	 * @return sub-set data collection
	 */
	public static <T> TreeMap<Double, T> select(TreeMap<Double, T> x, int startIndex, int endIndex) {
		TreeMap<Double, T> result = new TreeMap<Double, T>();
		if (startIndex < 0)
			startIndex = 0;
		if (endIndex >= x.size())
			endIndex = x.size() - 1;
		for (Double time : x.keySet()) {
			if (time >= startIndex && time <= endIndex)
				result.put(time, x.get(time));
		}
		return result;
	}

	/**
	 * The method creates a new data collection based on the passed timestamps and
	 * the data collection by considering the new timestamps.
	 * 
	 * @param x          is the data collection
	 * @param timestamps is the new timestamps
	 * @return sub-set of the data collection
	 */
	public static TreeMap<Double, Double> fill(TreeMap<Double, Double> x, TreeSet<Double> timestamps) {
		TreeMap<Double, Double> result = new TreeMap<>();
		for (Double time : timestamps) {
			if (time < x.firstKey()) {
				result.put(time, x.firstEntry().getValue());
			} else if (x.lastKey() < time) {
				int period = (int) (x.lastKey() - x.firstKey());
				int future = (int) (time - x.lastKey());
				Double val = StatisticalOperators.flr(x, x.lastKey(), period, future).getMean();
				if (Double.isNaN(val))
					val = x.lastKey();
				result.put(time, val);
			} else {
				Double val = interpolate(x, time);
				result.put(time, val);
			}
		}
		return result;
	}

	/**
	 * The method creates a new data collections based on the passed timestamps and
	 * the data collections by considering the new timestamps.
	 * 
	 * @param x          is the data collection
	 * @param timestamps is the new timestamps
	 * @return sub-set of the data collection
	 */
	public static HashMap<String, TreeMap<Double, Double>> fill(HashMap<String, TreeMap<Double, Double>> x,
			TreeSet<Double> timestamps) {
		HashMap<String, TreeMap<Double, Double>> result = new HashMap<>();
		for (String string : x.keySet()) {
			TreeMap<Double, Double> xrow = fill(x.get(string), timestamps);
			result.put(string, xrow);
		}
		return result;
	}

	/**
	 * The method select part of the data collection starting and ending with passed
	 * time stamps.
	 * 
	 * @param x              is the data collection
	 * @param startTimeStamp is the first timestamp
	 * @param endTimeStamp   is the end timestamp
	 * @return sub-set of the data collection in a specific timestamp range
	 */
	public static <T> SortedMap<Double, T> select(TreeMap<Double, T> x, double startTimeStamp, double endTimeStamp) {
		return x.subMap(startTimeStamp, true, endTimeStamp, true);
	}

	/**
	 * The method calculates sqrt value of each element in the data collection (i.e.
	 * timeseries).
	 * 
	 * @param x is the data collection
	 * @return the sqrt of data collection elements
	 */
	public static TreeMap<Double, Double> sqrt(TreeMap<Double, Double> x) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, Math.sqrt(x.get(key)));
		}
		return result;
	}

	/**
	 * The method calculates the power of each element in the data collection (i.e.
	 * timeseries).
	 * 
	 * @param x   is the base as data collection
	 * @param num is the power
	 * @return the power of data collection elements
	 */
	public static TreeMap<Double, Double> pow(TreeMap<Double, Double> x, double num) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, Math.pow(x.get(key), num));
		}
		return result;
	}

	/**
	 * The method calculates the mean of the value of a data collections
	 * 
	 * @param x is the data collections
	 * @return the mean for each data collection
	 */
	public static TreeMap<Double, Double> mean(HashMap<String, TreeMap<Double, Double>> x) {
		TreeMap<Double, Double> sum = new TreeMap<>();
		for (String str : x.keySet()) {
			for (Double time : x.get(str).keySet()) {

				if (sum.get(time) == null)
					sum.put(time, x.get(str).get(time));
				else
					sum.replace(time, sum.get(time) + x.get(str).get(time));
			}
		}
		return divide(sum, new Double(x.size()));
	}

	/**
	 * The method calculates the means of data collections 
	 * 
	 * @param x is a data collections
	 * @param timestamps is the new timestamps
	 * @return means of data collections
	 */
	public static TreeMap<Double, Double> mean(HashMap<String, TreeMap<Double, Double>> x, TreeSet<Double> timestamps) {
		TreeMap<Double, Double> sum = new TreeMap<>();
		for (String str : x.keySet()) {
			for (Double time : x.get(str).keySet()) {
				if (time >= timestamps.first() && time <= timestamps.last()) {
					if (sum.get(time) == null)
						sum.put(time, x.get(str).get(time));
					else
						sum.replace(time, sum.get(time) + x.get(str).get(time));
				}
			}
		}
		return divide(sum, new Double(x.size()));
	}

	/**
	 * The method calculates the difference between a data collection and a value  
	 * 
	 * @param x is a data collection
	 * @param y is the subtracted value
	 * @return the subtract of a value from a data collection
	 */
	public static TreeMap<Double, Double> minus(TreeMap<Double, Double> x, Double y) {
		if (y == 0)
			return x;

		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) - y);
		}
		return result;
	}

	/**
	 * The method calculates the difference between two data collections
	 * 
	 * @param x is the 
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> minus(TreeMap<Double, Double> x, TreeMap<Double, Double> y) {
		if (y.size() == 0)
			return x;
		if (x.size() == 0)
			return multiply(y, -1.0);

		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) - y.get(key));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param times
	 * @return
	 */
	public static TreeMap<Double, Double> minus(TreeMap<Double, Double> x, TreeMap<Double, Double> y,
			TreeSet<Double> times) {
		if (y.size() == 0)
			return x;
		if (x.size() == 0)
			return multiply(y, -1.0);

		x = resampleShift(x, times);
		y = resampleShift(y, times);
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : times) {
			result.put(key, x.get(key) - y.get(key));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> plus(TreeMap<Double, Double> x, Double y) {
		if (y == 0)
			return x;

		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) + y);
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> plus(TreeMap<Double, Double> x, TreeMap<Double, Double> y) {
		if (y.size() == 0)
			return x;
		if (x.size() == 0)
			return y;

		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) + y.get(key));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param times
	 * @return
	 */
	public static TreeMap<Double, Double> plus(TreeMap<Double, Double> x, TreeMap<Double, Double> y,
			TreeSet<Double> times) {
		if (y.size() == 0)
			return x;
		if (x.size() == 0)
			return y;

		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : times) {
			result.put(key, x.get(key) + y.get(key));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> multiply(TreeMap<Double, Double> x, Double y) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) * y);
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> multiply(TreeMap<Double, Double> x, TreeMap<Double, Double> y) {
		if (y.size() == 0)
			return x;
		if (x.size() == 0)
			return y;

		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) * y.get(key));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param times
	 * @return
	 */
	public static TreeMap<Double, Double> multiply(TreeMap<Double, Double> x, TreeMap<Double, Double> y,
			TreeSet<Double> times) {
		if (y.size() == 0)
			return x;
		if (x.size() == 0)
			return y;

		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			result.put(key, x.get(key) * y.get(key));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> divide(TreeMap<Double, Double> x, Double y) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			if (y != 0)
				result.put(key, x.get(key) / y);
			else
				result.put(key, Double.NaN);
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static TreeMap<Double, Double> divide(TreeMap<Double, Double> x, TreeMap<Double, Double> y) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			if (y.get(key) != 0)
				result.put(key, x.get(key) / y.get(key));
			else
				result.put(key, Double.NaN);
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param times
	 * @return
	 */
	public static TreeMap<Double, Double> divide(TreeMap<Double, Double> x, TreeMap<Double, Double> y,
			TreeSet<Double> times) {
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double key : x.keySet()) {
			if (y.get(key) != 0)
				result.put(key, x.get(key) / y.get(key));
			else
				result.put(key, Double.NaN);
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param timestamps
	 * @return
	 */
	public static TreeMap<Double, Double> resampleShift(TreeMap<Double, Double> x, TreeSet<Double> timestamps) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		for (Double time : timestamps) {
			Double val = 0.0;
			if (x.ceilingEntry(time) != null) {
				val = x.ceilingEntry(time).getValue();
			}
			result.put(time, val);
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param timestamps
	 * @return
	 */
	public static TreeMap<Double, Double> resampleInterpolation(TreeMap<Double, Double> x, TreeSet<Double> timestamps) {
		TreeMap<Double, Double> result = new TreeMap<Double, Double>();
		Double[] ts = new Double[timestamps.size()];
		ts = timestamps.toArray(ts);
		for (int i = 0; i < timestamps.size(); i++) {
			result.put(ts[i], interpolate(x, ts[i]));
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param timestamps
	 * @return
	 */
	public static HashMap<String, TreeMap<Double, Double>> resampleInterpolation(
			HashMap<String, TreeMap<Double, Double>> x, TreeSet<Double> timestamps) {
		HashMap<String, TreeMap<Double, Double>> result = new HashMap<String, TreeMap<Double, Double>>();
		if (x == null)
			return result;
		for (String key : x.keySet()) {
			result.put(key, resampleInterpolation(x.get(key), timestamps));
		}
		return result;
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @param step
	 * @return
	 */
	public static TreeSet<Double> createUnifiedTimeStamps(Double min, Double max, Double step) {
		TreeSet<Double> result = new TreeSet<Double>();
		Double i = min;
		while (i <= max) {
			result.add(i);
			i += step;
		}
		return result;
	}

	/*
	 * private
	 */

	/**
	 * 
	 * @param x
	 * @param keyTime
	 * @return
	 */
	private static Double interpolate(TreeMap<Double, Double> x, double keyTime) {
		double[] vals = interpolationValues(x, keyTime);

		if ((vals[2] - vals[0]) == 0)
			return vals[2];
		double m = (vals[3] - vals[1]) / (vals[2] - vals[0]);
		double c = vals[1] - m * vals[0];

		if (keyTime < 0.0)
			return 0.0;
		double value = (keyTime - c) / m;
		return value;
	}

	/**
	 * 
	 * @param x
	 * @param j
	 * @return
	 */
	private static double[] interpolationValues(TreeMap<Double, Double> x, double j) {
		double[] vals = new double[4];
		if (x.size() >= 2) {
			if (x.floorKey(j) != null) {
				vals[0] = x.floorEntry(j).getValue();
				vals[1] = x.floorKey(j);
			} else {
				vals[0] = 0;
				vals[1] = 0;
			}
			if (x.ceilingEntry(j) != null) {
				vals[2] = x.ceilingEntry(j).getValue();
				vals[3] = x.ceilingKey(j);
			} else {
				vals[2] = 0;
				vals[3] = 0;
			}
		}
		return vals;
	}
}
