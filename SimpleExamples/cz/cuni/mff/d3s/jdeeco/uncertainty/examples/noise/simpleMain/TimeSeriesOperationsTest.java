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

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics.TimeSeriesOperations;

public class TimeSeriesOperationsTest {
	public static void main(String[] args) {
		ArrayList<DataTimeStamp<Double>> x = new ArrayList<DataTimeStamp<Double>>();
		x.add(new DataTimeStamp<Double>(-1.959963947166891, 0.0));
		x.add(new DataTimeStamp<Double>(96.9140205533189, 1.0));
		x.add(new DataTimeStamp<Double>(198.01029503469817, 2.0));
		x.add(new DataTimeStamp<Double>(297.6424387513975, 3.0));
		
		ArrayList<DataTimeStamp<Double>> y = new ArrayList<DataTimeStamp<Double>>();
		y.add(new DataTimeStamp<Double>(-1.959963947166891, 0.0));
		y.add(new DataTimeStamp<Double>(6.9140205533189, 1.0));
		y.add(new DataTimeStamp<Double>(198.01029503469817, 2.0));
		y.add(new DataTimeStamp<Double>(27.6424387513975, 3.0));
		
		System.out.println("x - 5 = "+TimeSeriesOperations.minus(x, 5.0));
		System.out.println("x - y = "+TimeSeriesOperations.minus(x, y));
		System.out.println("x + 5 = "+TimeSeriesOperations.plus(x, 5.0));
		System.out.println("x + y = "+TimeSeriesOperations.plus(x, y));
		System.out.println("x * 5 = "+TimeSeriesOperations.multiply(x, 5.0));
		System.out.println("x * y = "+TimeSeriesOperations.multiply(x, y));
	}
}
