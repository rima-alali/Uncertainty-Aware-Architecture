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

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
/**
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class StatisticalModel {
	//mode -> data in one row
	protected HashMap<String, TreeMap<Double, Double>> inputs = new HashMap<String, TreeMap<Double, Double>>();
	protected HashMap<String, TreeMap<Double, Double>> outputs = new HashMap<String, TreeMap<Double, Double>>();
	protected HashMap<String, TreeMap<Double, Double>> softThresholds = new HashMap<String, TreeMap<Double, Double>>();
	protected HashMap<String, Double> hardThresholds = new HashMap<String, Double>();

	public StatisticalModel() {

	}

	public void setInput(String str) {
		inputs.put(str, new TreeMap<Double, Double>());
	}

	public void addInput(String str, Double val, Double timestamp) {
		inputs.get(str).put(timestamp,val);
	}

	public void addInput(String str, DataTimeStamp<Double> vts) {
		if (vts != null || str != null) {
			inputs.get(str).put(vts.getTimestamp(), vts.getData());
		}
	}

	public void addInput(String str, State state) {
		if (state != null) {
//			inputs.get(str).addValue(state.getData(), state.getTimeStamp());
			inputs.get(str).put(state.getTimeStamp(), state.getMinBound());
			inputs.get(str).put(state.getTimeStamp(), state.getMaxBound());
		}
	}

	public Double getInput(String str, int i) {
		if (i < inputs.get(str).size())
			return inputs.get(str).get(i);
		return null;
	}

	public TreeMap<Double, Double> getInput(String str) {
		return inputs.get(str);
	}

	public Double getLastInput(String str) {
		if (inputs.get(str) != null && inputs.get(str).size() > 0)
			return inputs.get(str).get(inputs.get(str).size() - 1);
		return null;
	}

	public Double getLastSoftThresholdValue(String str) {
		if (softThresholds.get(str) != null && softThresholds.get(str) != null
				&& softThresholds.get(str).size() > 0)
			return softThresholds.get(str).get(softThresholds.get(str).size() - 1);
		return 0.0;
	}

	public Set<String> getInputNames() {
		return inputs.keySet();
	}

	public void setOutput(String str) {
		outputs.put(str, new TreeMap<Double, Double>());
	}

	public void addOutput(String str, Double val, Double timestamp) {
		outputs.get(str).put(timestamp, val);
	}

	public void addOutput(String str, DataTimeStamp<Double> vts) {
		outputs.get(str).put(vts.getTimestamp(), vts.getData());
	}

	public void addOutput(String str, State state) {
		if (state != null) {
			outputs.get(str).put(state.getTimeStamp(), state.getMinBound());
			outputs.get(str).put(state.getTimeStamp(), state.getMaxBound());
		}
	}

	public Double getOutput(String str, int i) {
		if (i < outputs.get(str).size())
			return outputs.get(str).get(i);
		return null;
	}

	public TreeMap<Double, Double> getOutput(String str) {
		return outputs.get(str);
	}

	public void setSoftThreshold(String str) {
		softThresholds.put(str, new TreeMap<Double, Double>());
	}

	public void addSoftThreshold(String str, Double val, Double timestamp) {
		softThresholds.get(str).put(timestamp, val);
	}

	public void addSoftThreshold(String str, DataTimeStamp<Double> vts) {
		softThresholds.get(str).put(vts.getTimestamp(), vts.getData());
	}

	public void addSoftThreshold(String str, State state) {
		if (state != null) {
//			softThresholds.get(str).addValue(state.getData(), state.getTimeStamp());
			softThresholds.get(str).put(state.getTimeStamp(), state.getMaxBound());
			softThresholds.get(str).put(state.getTimeStamp(), state.getMinBound());
		}
	}

	public Double getSoftThreshold(String str, int i) {
		if (i < softThresholds.get(str).size())
			return softThresholds.get(str).get(i);
		return null;
	}

	public TreeMap<Double, Double> getSoftThreshold(String str) {
		return softThresholds.get(str);
	}

	public void setHardThreshold(String str) {
		hardThresholds.put(str, 0.0);
	}

	public void addHardThreshold(String str, Double val) {
		hardThresholds.put(str, val);
	}

	public Double getHardThreshold(String str) {
		return hardThresholds.get(str);
	}
}
