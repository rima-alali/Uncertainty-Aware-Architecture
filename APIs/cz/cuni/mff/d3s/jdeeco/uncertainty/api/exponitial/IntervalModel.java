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

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class present a model to capture a set of values that are repeated in cyclic way.
 *   
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class IntervalModel {

	protected ArrayList<Interval> intervals;

	public IntervalModel() {
		intervals = new ArrayList<Interval>();
	}
	
	public void setDelays(ArrayList<Interval> d) {
		intervals = d;
	}
	
	public ArrayList<Interval> getDelays() {
		return intervals;
	}
	
	public void add(double period, double interval) {
		intervals.add(new Interval(period, interval));
//		System.err.println("delays ... "+delays.size());
	}
	
	public void addAll(TreeMap<Double, Double> tree) {
		for (Double period : tree.keySet()) {
			intervals.add(new Interval(period, tree.get(period)));
		}
	}
	
	public void addAll(ArrayList<Interval> arr) {
		for (Interval d : arr) {
			intervals.add(d);
		}
	}
	
	public Double sum() {
		Double sum = 0.0;
		for (Interval delay : intervals) {
			if (delay.getPeriod() != 0)
				sum += ((double) delay.getInterval() / delay.getPeriod());
//			System.out.println("sum ... " + sum + "  interval " + delay.getInterval() + " / delays " + delay.getPeriod());
		}
		return sum;
	}

	// lambda is the average time/space between events (successes) that follow a
	// Poisson Distribution
	// rate of delays in wifi communication the data arrives each 10 millisec =>
	// lambda = 1/10
	public Double lambda() {
//		System.out.println("avg ... " + (sum() / delays.size()) + "  = " + sum() + " / " + delays.size());
		return sum() / intervals.size();
	}

	public double mean() {
//		System.out.println("mean ... "+(1 / lambda()));
		return 1 / lambda();
	}

	public double variance() {
		return 1 / Math.pow(lambda(), 2);
	}

	public Double probability(double x) {
		Double lambda = lambda();
		if (x < 0)
			return 0.0;
//		System.out.println(
//				"inside ... x: " + (double) x + " lambda " + (double) lambda + " exp " + Math.exp(-lambda * x));
		return lambda * Math.exp(-lambda * x);
	}

	public Double cdf(double x) {
		Double lambda = lambda();
		if (x < 0)
			return 0.0;
		return 1 - Math.exp(-lambda * x);
	}
	
	public int size() {
		return intervals.size();
	}
	
	@Override
	public String toString() {
		String str = new String();
		for (Interval d : intervals) {
			str += " ... period: "+d.getPeriod()+" , interval: "+d.getInterval()+"  \n ";
		}
		return str;
	}
	
	
	/**
	 * 
	 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
	 *
	 */
	protected class Interval{
		protected Double period;
		protected Double interval;
		
		public Interval(Double period, Double interval) {
			this.period = period;
			this.interval = interval;
		}
		
		public void setInterval(Double interval) {
			this.interval = interval;
		}
		
		public void setPeriod(Double period) {
			this.period = period;
		}
		
		public Double getInterval() {
			return interval;
		}
		
		public Double getPeriod() {
			return period;
		}
	}
}
