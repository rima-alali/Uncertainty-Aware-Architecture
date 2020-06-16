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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.StatisticalOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.TimeSeries;

public class StatisticalOperatorsTest {

	public static void main(String[] args) {
		
		TreeMap<Double, Double> x = new TreeMap<Double, Double>();
		x.put(0.0, -1.959963947166891);
		x.put(1.0, 96.9140205533189);
		x.put(2.0, 198.01029503469817);
		x.put(3.0, 297.6424387513975);
		
		System.out.println("small time series ... 297.6 ");
		System.out.println("400 fbelow : "+StatisticalOperators.fbelow(x, 400.0, 4, 0, 1, 2));
		System.out.println("100 fbelow : "+StatisticalOperators.fbelow(x, 100.0, 4, 0, 1, 2));
		System.out.println("400 fabove : "+StatisticalOperators.fabove(x, 400.0, 4, 0, 1, 2));
		System.out.println("100 fabove : "+StatisticalOperators.fabove(x, 100.0, 4, 0, 1, 2));
		
		//try 297.6 instead of 300.0 all return false since the confidence level is 95%
		System.out.println("297 equals : "+StatisticalOperators.equals(x, 297.0, 4, 1, 2));
		System.out.println("300 equals : "+StatisticalOperators.equals(x, 300.0, 4, 1, 2));
		
		List<Integer> times = new ArrayList<>();
		List<Double> values = new ArrayList<>();
		CsvLoader.loadValues("LrTest.csv", times, values);
		TimeSeries s = new TimeSeries(10, 3000);
		for (int i = 10; i < times.size(); i++) {
			s.addSample(values.get(i), times.get(i));
		}
		
		System.out.println("big time series ... ");
		System.out.println(s.getLr(times.get(times.size()-1).doubleValue()).getMean()+"  ,  value: "+values.get(values.size()-1)+", time: "+times.get(times.size()-1).doubleValue());
		
		TreeMap<Double, Double> arr = new TreeMap<Double, Double>();
		for (int i = 0; i < values.size(); i++) {
			arr.put(times.get(i).doubleValue(), values.get(i));
		}
		int w = 59978; //milli-sec ... start with index 10		
		System.out.println("below : "+StatisticalOperators.below(arr, values.get(values.size()-1), w, 10, 3000));
		System.out.println("fbelow : "+StatisticalOperators.fbelow(arr, values.get(values.size()-1), w, 100, 10, 3000));
		System.out.println("above : "+StatisticalOperators.above(arr, values.get(values.size()-1), w, 10, 3000));
		System.out.println("fabove : "+StatisticalOperators.fabove(arr, values.get(values.size()-1), w, 100, 10 , 3000));
		
	}
}
