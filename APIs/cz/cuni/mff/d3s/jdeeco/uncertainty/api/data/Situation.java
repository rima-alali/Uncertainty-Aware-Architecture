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
import java.util.ArrayList;
import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;

/**
 * The class proposes a number of related information associated to a specific
 * situation. The information includes the name of the inputs, the outputs, the
 * soft threshold, and the hard threshold.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class Situation implements Serializable {
	public static final long serialVersionUID = 87883L;

	protected ArrayList<String> shard; // name of the field
	protected TreeMap<Double, Double> outputs; // name of the mode
	protected TreeMap<Double, State> softThresholdsState; // name of the mode
	protected TreeMap<Double, Double> softThresholds; // name of the mode
	protected Double hardThreshold; // name of the mode

	/**
	 * The constructor initiates the information associated to the situation
	 * 
	 */
	public Situation() {
		this.shard = new ArrayList<String>();
		this.outputs = new TreeMap<Double, Double>();
		this.softThresholds = new TreeMap<Double, Double>();
		this.softThresholdsState = new TreeMap<Double, State>();
		this.hardThreshold = 0.0;
	}

	/**
	 * The method sets the information passed about the input names
	 * 
	 * @param shard is the set of input names
	 */
	public void setShard(ArrayList<String> shard) {
		this.shard = shard;
	}

	/**
	 * The method adds a time stamped value to the outputs 
	 * 
	 * @param val is the output data
	 * @param timestamp is the creation time of the output data
	 */
	public void addOutput(Double val, Double timestamp) {
		this.outputs.put(timestamp, val);
	}

	/**
	 * The method sets the outputs values 
	 * 
	 * @param outputs are a collection of values
	 */
	public void setOutputs(TreeMap<Double, Double> outputs) {
		this.outputs = outputs;
	}

	/**
	 * The method sets the soft thresholds values
	 * 
	 * @param softThresholds are a collection of values
	 */
	public void setSoftThresholds(TreeMap<Double, Double> softThresholds) {
		this.softThresholds = softThresholds;
	}

	/**
	 * The method sets the states of soft thresholds
	 * 
	 * @param softThresholdsState is the state of the soft thresholds
	 */
	public void setSoftThresholdsState(TreeMap<Double, State> softThresholdsState) {
		this.softThresholdsState = softThresholdsState;
	}

	/**
	 * The method adds a value to the soft thresholds
	 * 
	 * @param val is the soft threshold value
	 * @param timestamp is the creation time
	 */
	public void addSoftThreshold(Double val, Double timestamp) {
		this.softThresholds.put(timestamp, val);
	}

	/**
	 * The method adds a new value for the state of soft threshold
	 * 
	 * @param val is the state
	 * @param timestamp is the creation time
	 */
	public void addSoftThresholdState(State val, Double timestamp) {
		this.softThresholdsState.put(timestamp, val);
	}

	/**
	 * The method sets the hard threshold of the situation
	 * 
	 * @param threshold is a single value
	 */
	public void setHardThreshold(Double threshold) {
		this.hardThreshold = threshold;
	}

	/**
	 * The method returns the names of the inputs
	 * 
	 * @return input names
	 */
	public ArrayList<String> getShard() {
		return shard;
	}

	/**
	 * The method returns the outputs of the situation
	 * 
	 * @return time stamped outputs
	 */
	public TreeMap<Double, Double> getOutputs() {
		return outputs;
	}

	/**
	 * The method returns the soft thresholds
	 * 
	 * @return soft thresholds
	 */
	public TreeMap<Double, Double> getSoftThreshold() {
		return softThresholds;
	}

	/**
	 * The method returns states of soft thresholds for the situation
	 * 
	 * @return states of soft thresholds
	 */
	public TreeMap<Double, State> getSoftThresholdsState() {
		return softThresholdsState;
	}

	/**
	 * The method returns the hard threshold of the situation
	 * 
	 * @return hard threshold value
	 */
	public Double getHardThreshold() {
		return hardThreshold;
	}
}
