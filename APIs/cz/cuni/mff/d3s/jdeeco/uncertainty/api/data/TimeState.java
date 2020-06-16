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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.data;

import java.io.Serializable;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;

/**
 * The class captures the state structure, which consists of: name and data. The
 * data has the original value, the creation time of the data, the minimum and
 * maximum values.
 * 
 * The difference from State class is the time stamps associated to the data and
 * its bounds.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class TimeState implements Serializable {
	public static final long serialVersionUID = 9411L;

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	protected String name;
	protected DataTimeStamp<Double> min;
	protected DataTimeStamp<Double> max;
	protected DataTimeStamp<Double> value;
	protected Double timestamp;

	/**
	 * The constructor initiates the state and sets its name.
	 * 
	 * @param name is the state name
	 */
	public TimeState(String name) {
		this(name, 0.0);
	}

	/**
	 * The constructor initiates the state name and its data. The timestamp is the
	 * current time, and the bounds of the data is set to be the same as the data.
	 * 
	 * @param name is the state name
	 * @param val  is the state data
	 */
	public TimeState(String name, double val) {
		this(name, 0.0, System.nanoTime() / MILLISEC_NANOSEC_FACTOR);
	}

	/**
	 * The constructor initiates the state name and its timed data. The bounds are
	 * set to have the same passed value.
	 * 
	 * @param name      is the state name
	 * @param val       is the state data
	 * @param timestamp is the state data creation time
	 */
	public TimeState(String name, double val, double timestamp) {
		this.name = name;
		this.value = new DataTimeStamp<Double>(val, timestamp);
		this.min = new DataTimeStamp<Double>(val, timestamp);
		this.max = new DataTimeStamp<Double>(val, timestamp);
	}

	/**
	 * The method sets the data and its time stamp
	 * 
	 * @param val is the state data
	 * @param timestamp is the state data creation time
	 */
	public void setDataTimeStamp(double val, double timestamp) {
		value.setData(val);
		value.setTimeStamp(timestamp);
	}
	
	/**
	 * The method passed the time state
	 * 
	 * @param state is the new time state
	 */
	public void setData(TimeState state) {
		min = state.getMin();
		max = state.getMax();
		value = state.getDataTimeStamp();
	}

	/**
	 * The method sets the minimum bound of data
	 * 
	 * @param val is minimum data
	 * @param timestamp is creation time of data
	 */
	public void setMin(double val, double timestamp) {
		min.setData(val);
		value.setTimeStamp(timestamp);
	}

	/**
	 * The method sets the maximum bound of data
	 * 
	 * @param val is maximum data
	 * @param timestamp is creation time of data
	 */
	public void setMax(double val, double timestamp) {
		max.setData(val);
		max.setTimeStamp(timestamp);
	}
	
	/**
	 * The method sets creation time of the time state
	 * 
	 * @param timestamp is the creation time
	 */
	public void setTimestamp(Double timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * The method returns the data and its time stamp
	 * 
	 * @return timestamped data
	 */
	public DataTimeStamp<Double> getDataTimeStamp() {
		return value;
	}

	/**
	 * The method returns minimum data with its time stamp
	 * 
	 * @return timestamped minimum bound
	 */
	public DataTimeStamp<Double> getMin() {
		return min;
	}
	
	/**
	 *  The method returns maximum data with its time stamp
	 * 
	 * @return timestamped maximum bound
	 */
	public DataTimeStamp<Double> getMax() {
		return max;
	}
	
	/**
	 * The method returns interval with its time stamp
	 * 
	 * @return timestamped maximum bound
	 */
	public Double getInterval() {
		return max.getData() - min.getData();
	}

	/**
	 * The method returns the creation time of the time state
	 * 
	 * @return the creation time
	 */
	public Double getTimestamp() {
		return timestamp;
	}

	/**
	 * The method returns a state from the time state
	 * 
	 * @return state
	 */
	public State getState() {
		double val = value.getData();
		double i = min.getData();
		double j = max.getData();
		State s = new State(name);
		s.setData(val, i, j, timestamp);
		return s;
	}
	
	/**
	 * The method adds a number to the data and both bounds
	 * 
	 * @param num is the value to be added
	 */
	public void addNumber(double num) {
		value.data += num;
		min.data += num;
		max.data += num;
	}

	/**
	 * The method returns a string that contains all the information
	 *  
	 */
	@Override
	public String toString() {
		return value.toString() + " min: " + min.toString() + " max: " + max.toString();
	}
}
