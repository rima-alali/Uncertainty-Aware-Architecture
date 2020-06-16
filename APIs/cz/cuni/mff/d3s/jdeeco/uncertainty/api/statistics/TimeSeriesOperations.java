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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics;

import java.util.ArrayList;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;

/**
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class TimeSeriesOperations {

	public static <T> ArrayList<DataTimeStamp<T>> select(ArrayList<DataTimeStamp<T>> x, int startIndex,
			int endIndex) {
		ArrayList<DataTimeStamp<T>> result = new ArrayList<DataTimeStamp<T>>();
		if (startIndex < 0)
			startIndex = 0;
		if (endIndex >= x.size())
			endIndex = x.size() - 1;
		for (int i = startIndex; i <= endIndex; i++) {
			result.add(x.get(i));
		}
		return result;
	}

	public static <T> ArrayList<DataTimeStamp<T>> select(ArrayList<DataTimeStamp<T>> x, double startTimeStamp,
			double endTimeStamp) {
		ArrayList<DataTimeStamp<T>> result = new ArrayList<DataTimeStamp<T>>();
		for (int i = 0; i < x.size(); i++) {
			if (startTimeStamp <= x.get(i).getTimestamp() && endTimeStamp >= x.get(i).getTimestamp())
				result.add(x.get(i));
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> minus(ArrayList<DataTimeStamp<Double>> x, Double y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		for (DataTimeStamp<Double> data : x) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(data.getData() - y, data.getTimestamp());
			result.add(newData);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> sqrt(ArrayList<DataTimeStamp<Double>> x) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(Math.sqrt(x.get(i).getData()),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> pow(ArrayList<DataTimeStamp<Double>> x, double num) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(Math.pow(x.get(i).getData(), num),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	// assuming the timestamps are the same
	//TODO: add the sampling from outside
	public static ArrayList<DataTimeStamp<Double>> minus(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		ArrayList<Double> times = resampleTimeStamps(x, y);
		x = resampleShift(x, times);
		y = resampleShift(y, times);
//		System.out.println("times : " + times);
//		System.out.println("x : " + x);
//		System.out.println("y : " + y);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(x.get(i).getData() - y.get(i).getData(),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	public static ArrayList<DataTimeStamp<Double>> minus(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y, ArrayList<Double> times) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		x = resampleShift(x, times);
		y = resampleShift(y, times);
//		System.out.println("times : " + times);
//		System.out.println("x : " + x);
//		System.out.println("y : " + y);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(x.get(i).getData() - y.get(i).getData(),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> plus(ArrayList<DataTimeStamp<Double>> x, Double y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		for (DataTimeStamp<Double> data : x) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(data.getData() + y, data.getTimestamp());
			result.add(newData);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> plus(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		ArrayList<Double> times = resampleTimeStamps(x, y);
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(x.get(i).getData() + y.get(i).getData(),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	public static ArrayList<DataTimeStamp<Double>> plus(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y, ArrayList<Double> times) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(x.get(i).getData() + y.get(i).getData(),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> multiply(ArrayList<DataTimeStamp<Double>> x, Double y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		for (DataTimeStamp<Double> data : x) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(data.getData() * y, data.getTimestamp());
			result.add(newData);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> multiply(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		ArrayList<Double> times = resampleTimeStamps(x, y);
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(x.get(i).getData() * y.get(i).getData(),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	public static ArrayList<DataTimeStamp<Double>> multiply(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y, ArrayList<Double> times) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData = new DataTimeStamp<Double>(x.get(i).getData() * y.get(i).getData(),
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}

	//should add NaN instead of zero 
	public static ArrayList<DataTimeStamp<Double>> divide(ArrayList<DataTimeStamp<Double>> x, Double y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		for (DataTimeStamp<Double> data : x) {
			DataTimeStamp<Double> newData;
			if(y != 0)
				newData = new DataTimeStamp<Double>(data.getData() / y,
						data.getTimestamp());
			else newData = new DataTimeStamp<Double>(0.0,
						data.getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	//should add NaN instead of zero 
	public static ArrayList<DataTimeStamp<Double>> divide(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		ArrayList<Double> times = resampleTimeStamps(x, y);
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData;
			if(y.get(i).getData() != 0)
			newData = new DataTimeStamp<Double>(x.get(i).getData() / y.get(i).getData(),
					x.get(i).getTimestamp());
			else newData = new DataTimeStamp<Double>(0.0,
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	public static ArrayList<DataTimeStamp<Double>> divide(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<DataTimeStamp<Double>> y, ArrayList<Double> times) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		x = resampleInterpolation(x, times);
		y = resampleInterpolation(y, times);
		for (int i = 0; i < x.size(); i++) {
			DataTimeStamp<Double> newData;
			if(y.get(i).getData() != 0)
			newData = new DataTimeStamp<Double>(x.get(i).getData() / y.get(i).getData(),
					x.get(i).getTimestamp());
			else newData = new DataTimeStamp<Double>(0.0,
					x.get(i).getTimestamp());
			result.add(newData);
		}
		return result;
	}
	
	//they fill the empty values with previous future or previous values ...
	public static ArrayList<DataTimeStamp<Double>> resampleShift(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<Double> timestamps) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		int j = 0;
		for (int i = 0; i < timestamps.size(); i++) {
			if (j == 0 && j < x.size()) {
				result = startArray(result, x, timestamps, j, i);
				j++;
			}
			result = addShiftValues(result, x, timestamps, i);
		}
		return result;
	}

	public static ArrayList<DataTimeStamp<Double>> resampleInterpolation(ArrayList<DataTimeStamp<Double>> x,
			ArrayList<Double> timestamps) {
		ArrayList<DataTimeStamp<Double>> result = new ArrayList<DataTimeStamp<Double>>();
		int j = 0;
		for (int i = 0; i < timestamps.size(); i++) {
			if (j == 0 && j < x.size()) {
				result = startArray(result, x, timestamps, j, i);
				j++;
			}
			result = addInterpolationValues(result, x, timestamps, i);
		}
		return result;
	}

//	public static boolean equals(ArrayList<DataTimeStamp<Double>> x, ArrayList<DataTimeStamp<Double>> y) {
//		KolmogorovSmirnovConfidence ksc = KolmogorovSmirnovConfidence.INSTANCE;
//		KolmogorovSmirnovConfidence.Statistic kstest = ksc.evaluateNullHypothesis(HelperOperations.getValues(x), HelperOperations.getValues(y));
//		System.out.println("p-value "+kstest.getNullHypothesisProbability()+" , F "+kstest.getTestStatistic()+" , D "+kstest.getD());
//		return kstest.getNullHypothesisProbability() >= 0.95; 
//	}

	public static ArrayList<Double> createUnifiedTimeStamps(double min, double max, double step) {
		ArrayList<Double> result = new ArrayList<Double>();
		double i = min;
		while (i <= max) {
			result.add(i);
			i += step;
		}
		return result;
	}
	
	/*
	 * private
	 */

	private static ArrayList<DataTimeStamp<Double>> startArray(ArrayList<DataTimeStamp<Double>> result,
			ArrayList<DataTimeStamp<Double>> x, ArrayList<Double> timestamps, int j, int i) {
			double value = x.get(j).getData();
			result.add(new DataTimeStamp<Double>(value, timestamps.get(i)));
			return result;
	}

	//TODO: change to T .. it should be for anytype while interpolation for double only
	private static ArrayList<DataTimeStamp<Double>> addShiftValues(ArrayList<DataTimeStamp<Double>> result,
			ArrayList<DataTimeStamp<Double>> x, ArrayList<Double> timestamps, int i) {
		double value = 0;
		for (int j = 1; j < x.size(); j++) {
			double[] vals = interpolationValues(x, j);
			if (timestamps.get(i) < vals[3] && timestamps.get(i) > vals[1]) {
				result.add(new DataTimeStamp<Double>(vals[0], timestamps.get(i)));
				break;
			} else if(timestamps.get(i) == vals[3]) {
				result.add(new DataTimeStamp<Double>(vals[2], timestamps.get(i)));
				break;	
			}		
		}
		return result;
	}
	
	
	private static ArrayList<DataTimeStamp<Double>> addInterpolationValues(ArrayList<DataTimeStamp<Double>> result,
			ArrayList<DataTimeStamp<Double>> x, ArrayList<Double> timestamps, int i) {
		double value = 0;
		for (int j = 1; j < x.size(); j++) {
			double[] vals = interpolationValues(x, j);
			if (timestamps.get(i) <= vals[3] && timestamps.get(i) > vals[1]) {
				value = interpolate(vals[0], vals[1], vals[2], vals[3], timestamps.get(i));
				result.add(new DataTimeStamp<Double>(value, timestamps.get(i)));
				break;
			}
		}
		return result;
	}

	private static double[] interpolationValues(ArrayList<DataTimeStamp<Double>> x, int j) {
		double[] vals = new double[4];
		vals[0] = x.get(j - 1).getData();
		vals[1] = x.get(j - 1).getTimestamp();
		vals[2] = x.get(j).getData();
		vals[3] = x.get(j).getTimestamp();
		return vals;
	}

	private static Double interpolate(Double val1, Double time1, Double val2, Double time2, Double keyTime) {
		double m = (time2 - time1) / (val2 - val1);
		double c = time1 - m * val1;

		if (keyTime < 0.0)
			return 0.0;
		double value = (keyTime - c) / m;
		return value;
	}

	private static ArrayList<Double> resampleTimeStamps(ArrayList<DataTimeStamp<Double>>... arrays) {
		double sampling = avgSamples(arrays);
		double min = 0;
		double max = 0;
		for (int i = 0; i < arrays.length; i++) {
			ArrayList<Double> timestamps = getTimeStamps(arrays[i]);
			for (Double timestamp : timestamps) {
				if (min > timestamp)
					min = timestamp;
				if (max < timestamp)
					max = timestamp;
			}
		}
		double step = (max - min) / sampling;
		return createUnifiedTimeStamps(min, max, step);
	}

	private static int avgSamples(ArrayList<DataTimeStamp<Double>>... arrays) {
		double sum = 0;
		for (int i = 0; i < arrays.length; i++) {
			sum += arrays[i].size();
		}
		return (int) (sum / arrays.length);
	}

	
	public static ArrayList<Double> getTimeStamps(ArrayList<DataTimeStamp<Double>> arr) {
		ArrayList<Double> times = new ArrayList<Double>();
		for (DataTimeStamp<Double> data : arr) {
			times.add(data.getTimestamp());
		}
		return times;
	}
	// check how to order the ArrayList depending on the timestamp ...

}
