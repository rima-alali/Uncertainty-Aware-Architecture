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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.peaks.simpleMain;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.learning.TreeMapOperations;

public class TreeMapOperationsTest {
	public static void main(String[] args) {
		TreeMap<Double, Double> x = new TreeMap<>();
		TreeMap<Double, Double> y = new TreeMap<>();
		x.put(1.0, 10.0);
//		x.put(2.0, 20.0);
		x.put(3.0, 50.0);

		y.put(3.0, 10.0);
		y.put(4.0, 20.0);
		y.put(5.0, 12.0);

		TreeSet<Double> ts = new TreeSet<>();
		ts.add(1.0);
		ts.add(2.0);
		ts.add(3.0);
		ts.add(4.0);
		ts.add(5.0);

		HashMap<String, TreeMap<Double, Double>> ins = new HashMap<>();
		ins.put("A", x);
		ins.put("B", y);

		TreeMap<Double, Double> xfilled = TreeMapOperations.fill(x, ts);
		TreeMap<Double, Double> yfilled = TreeMapOperations.fill(y, ts); 
		ins.clear();
		ins.put("A", xfilled);
		ins.put("B", yfilled);
		
		System.out.println("before filling : x = "+x+" , y = "+y);
		System.out.println("after filling : x = "+xfilled+" , y = "+yfilled);
		System.out.println("inputs: "+ins+" means:  "+TreeMapOperations.mean(ins,ts));
	}

}
