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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy;

import java.io.Serializable;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataRange;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;

/**
 * The class captures the state structure, which consists of: name and data. The
 * data has the original value, the creation time of the data, the minimum and
 * maximum values.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class State implements Serializable {
	public static final long serialVersionUID = 9411L;

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	protected String name;
	protected DataRange data;

	/**
	 * The constructor initiates the state and sets its name.
	 * 
	 * @param name is the state name
	 */
	public State(String name) {
		this(name, 0.0);
	}

	/**
	 * The constructor initiates the state name and its data. The timestamp is the
	 * current time, and the bounds of the data is set to be the same as the data.
	 * 
	 * @param name is the state name
	 * @param val  is the state data
	 */
	public State(String name, double val) {
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
	public State(String name, double val, double timestamp) {
		this.name = name;
		data = new DataRange(new DataTimeStamp<Double>(val, timestamp));
	}

	/**
	 * The method sets the data, its creation time, and min and max bounds
	 * 
	 * @param data is timedstamp and its bounds
	 */
	public void setData(DataRange data) {
		this.data = data;
	}

	/**
	 * The method sets the name of the state
	 * 
	 * @param name of the state
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The method sets the data and its timestamp
	 * 
	 * @param data is timestamped data
	 */
	public void setData(DataTimeStamp<Double> data) {
		this.data = new DataRange(data);
	}

	/**
	 * The method sets the data, its creation time, and min and max bounds
	 * 
	 * @param val       is the data
	 * @param min       is the minimum bound
	 * @param max       is the maximum bound
	 * @param timestamp is the creation time
	 */
	public void setData(double val, double min, double max, double timestamp) {
		data = new DataRange(val, min, max, timestamp);
	}

	/**
	 * The method updates the data and its creation time
	 * 
	 * @param val       is the data
	 * @param timestamp is the creation time of data
	 */
	public void setData(double val, double timestamp) {
		data.setDataTimeStamp(val, timestamp);
	}

	/**
	 * The method updates the data and its time stamp
	 * 
	 * @param val       is the data
	 * @param timestamp is the creation time
	 */
	public void setValue(double val, double timestamp) {
		data.setDataTimeStamp(val, timestamp);
	}

	public void addNumber(double num) {
		data.setDataTimeStamp(data.getData() + num, data.getTimeStamp());
		data.setMin(data.getMin() + num);
		data.setMax(data.getMax() + num);
	}

	/**
	 * The method updates the bounds of the data
	 * 
	 * @param min       is the minimum bound
	 * @param max       is the maximum bound
	 * @param timestamp is the creation time of bounds
	 */
	public void updateBounds(double min, double max, double timestamp) {
		data.setMax(max);
		data.setMin(min);
		data.setTimeStamp(timestamp);
	}

	/**
	 * The method updates the bounds of the data with the creation time
	 * 
	 * @param data is the data range
	 */
	public void updateBounds(DataRange data) {
		data.setMax(data.getMax());
		data.setMin(data.getMin());
		data.setTimeStamp(data.getTimeStamp());
	}

	/**
	 * The method returns the name of the state
	 * 
	 * @return state name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The method returns the timestamped data and its bounds
	 * 
	 * @return timestamped data and its bounds
	 */
	public DataRange getDataRange() {
		return data;
	}

	/**
	 * The method returns the timestamped data
	 * 
	 * @return timestamped data
	 */
	public DataTimeStamp<Double> getDataTimeStamp() {
		return data.getDataTimeStamp();
	}

	/**
	 * The method returns the data
	 * 
	 * @return data
	 */
	public Double getData() {
		return data.getData();
	}

	/**
	 * The method returns the creation time of the data
	 * 
	 * @return timestamp of the data
	 */
	public Double getTimeStamp() {
		return data.getTimeStamp();
	}

	/**
	 * The method returns the minimum bound of the data
	 * 
	 * @return data minimum bound
	 */
	public Double getMinBound() {
		return data.getMin();
	}

	/**
	 * The method returns the maximum bound of the data
	 * 
	 * @return data maximum bound
	 */
	public Double getMaxBound() {
		return data.getMax();
	}

	public Double getInterval() {
		return getMaxBound() - getMinBound();
	}

	/**
	 * The method returns a string with the name and the related data of the state
	 */
	@Override
	public String toString() {
		return name + "  " + data.toString();
	}
}
