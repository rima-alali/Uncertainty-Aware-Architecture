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

/**
 * The class adds time stamps to any kind of data passed to it.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 * @param <T> data type that needs time stamp.
 */
public class DataTimeStamp<T> implements Serializable, Comparable<DataTimeStamp<Double>> {
	public static final long serialVersionUID = 94L;

	protected T data;
	protected Double timestamp = 0.0;
	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	/**
	 * The constructor creates data with null value and timestamp zero
	 */
	public DataTimeStamp() {
		this(null, System.nanoTime() / MILLISEC_NANOSEC_FACTOR);
	}

	/**
	 * The constructor store the passed data and creates a current timestamp.
	 * 
	 * @param data is the data to be timestamped
	 */
	public DataTimeStamp(T data) {
		this(data, System.nanoTime() / MILLISEC_NANOSEC_FACTOR);
	}

	/**
	 * Constructor of the data with its time stamp value passed in.
	 * 
	 * @param data      is the data to be timestamped
	 * @param timestamp is the creation time of the data
	 */
	public DataTimeStamp(T data, Double timestamp) {
		this.data = data;
		this.timestamp = timestamp;
	}

	/**
	 * The method sets the value of the data from type T
	 * 
	 * @param data to be time stamped
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * The methods sets the creation time of the data
	 * 
	 * @param timestamp is the creation time of the data
	 */
	public void setTimeStamp(Double timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * The method sets the new value of data and its timestamp.
	 * 
	 * @param data with its creation time
	 */
	public void setDataTimeStamp(DataTimeStamp<T> data) {
		setData(data.getData());
		setTimeStamp(data.getTimestamp());
	}

	/**
	 * The method returns the data stored
	 * 
	 * @return data to be time stamped
	 */
	public T getData() {
		return data;
	}

	/**
	 * The method returns the creation time for the data
	 * 
	 * @return time is the creation timestamp of the data
	 */
	public Double getTimestamp() {
		return timestamp;
	}

	/**
	 * The method returns a string with the related data and its timestamp
	 */
	@Override
	public String toString() {
		return "Data: " + data + ", Timestamp: " + timestamp;
	}
	
	/**
	 * The method compares between the timestamps of data
	 */
	@Override
	public int compareTo(DataTimeStamp<Double> o) {
		// TODO Auto-generated method stub
		return timestamp > o.getTimestamp()? 1 : (timestamp == o.getTimestamp()? 0 : -1);
	}

}
