package cz.cuni.mff.d3s.jdeeco.uncertainty.api.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.exponitial.IntervalModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;

/**
 * The default is "mode"
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class History implements Serializable {
	public static final long serialVersionUID = 92873L;

	protected HashMap<String, TreeMap<Double, IntervalModel>> inputsDelays = new HashMap<String, TreeMap<Double, IntervalModel>>();
	protected HashMap<String, TreeMap<Double, Double>> inputs = new HashMap<String, TreeMap<Double, Double>>();
	protected HashMap<String, Situation> situations = new HashMap<String, Situation>();
	
	private double limit = 100;

	public History() {
		this(new String[] {}, new String[] {});
	}

	public History(String[] shards, String[] sensors) {
		for (String string : sensors) {
			inputs.put(string, new TreeMap<Double, Double>());
		}
		for (String string : shards) {
			situations.put(string, new Situation());
		}
	}

	public void setInputs(ArrayList<String> sensors) {
		for (String string : sensors) {
			if (!inputs.containsKey(string))
				inputs.put(string, new TreeMap<Double, Double>());
		}
	}

	public void setInputsDelays(ArrayList<String> sensors) {
		for (String string : sensors) {
			if (!inputsDelays.containsKey(string))
				inputsDelays.put(string, new TreeMap<Double, IntervalModel>());
		}
	}

	public void setSituations(HashMap<String, ArrayList<String>> shards) {
		for (String string : shards.keySet()) {
			Situation situation = new Situation();
			situation.setShard(shards.get(string));
			setInputs(shards.get(string));
			situations.put(string, situation);
		}
	}

	public void addInput(String str, double val, double timestamp) {
		if (Double.isNaN(val))
			return;
		if (Double.isNaN(timestamp))
			return;
		inputs.get(str).put(timestamp, val);
		
		if(inputs.get(str).size() > limit)
			inputs.get(str).remove(inputs.get(str).firstKey());
	}

	public void addInputDelays(String str, double timestamp, double period, double interval) {
		if (Double.isNaN(timestamp))
			return;
		if (Double.isNaN(interval))
			return;

		if (inputsDelays.get(str).containsKey(timestamp)) {
			inputsDelays.get(str).get(timestamp).add(period, interval);
		} else {
			IntervalModel din = new IntervalModel();
			din.add(period, interval);
			inputsDelays.get(str).put(timestamp, din);
		}
		

		if(inputsDelays.get(str).size() > limit)
			inputsDelays.get(str).remove(inputsDelays.get(str).firstKey());
	}

	public void addSoftThreshold(String mode, double val, double timestamp) {
		if (Double.isNaN(val))
			return;
		if (Double.isNaN(timestamp))
			return;

		situations.get(mode).addSoftThreshold(val, timestamp);
		
		if(situations.get(mode).softThresholds.size() > limit)
			situations.get(mode).softThresholds.remove(situations.get(mode).softThresholds.firstKey());
	}

	public void addSoftThresholdState(String mode, State val, double timestamp) {
		if (Double.isNaN(timestamp))
			return;

		situations.get(mode).addSoftThresholdState(val, timestamp);
		
		if(situations.get(mode).softThresholdsState.size() > limit)
			situations.get(mode).softThresholdsState.remove(situations.get(mode).softThresholdsState.firstKey());
	}

	public void addOutput(String mode, double val, double timestamp) {
		if (Double.isNaN(val))
			return;
		if (Double.isNaN(timestamp))
			return;

		situations.get(mode).addOutput(val, timestamp);
		
		if(situations.get(mode).outputs.size() > limit)
			situations.get(mode).outputs.remove(situations.get(mode).outputs.firstKey());
	}

	public void setHardThreshold(String mode, double val) {
		if (Double.isNaN(val))
			return;
		situations.get(mode).setHardThreshold(val);
	}

	public HashMap<String, TreeMap<Double, Double>> getInputs() {
		return inputs;
	}

	public HashMap<String, TreeMap<Double, IntervalModel>> getInputsDelays() {
		return inputsDelays;
	}

	public HashMap<String, TreeMap<Double, Double>> getInput(String mode) {
		HashMap<String, TreeMap<Double, Double>> result = new HashMap<>();
		if (situations.get(mode) != null)
			for (String key : situations.get(mode).getShard()) {
				result.put(key, inputs.get(key));
			}
		return result;
	}

	public HashMap<String, TreeMap<Double, Double>> getInput(String mode, String stateName) {
		HashMap<String, TreeMap<Double, Double>> result = new HashMap<>();
		if (situations.get(mode) != null)
			for (String key : situations.get(mode).getShard()) {
				if (key.endsWith(stateName)) {
					result.put(key, inputs.get(key));
				}
			}
		return result;
	}

	public TreeMap<Double, IntervalModel> getInputDelays(String name) {
		for (String str : inputsDelays.keySet()) {
			if (str.equals(name))
				return inputsDelays.get(str);
		}
		return new TreeMap<>();
	}

	public TreeMap<Double, Double> getInputMerge(String mode, String stateName) {
		TreeMap<Double, Double> result = new TreeMap<>();
		if (situations.get(mode) != null)
			for (String key : situations.get(mode).getShard()) {
				if (key.endsWith(stateName)) {
					result.putAll(inputs.get(key));
				}
			}
		return result;
	}

	public TreeMap<Double, Double> getInputState(String sensorname, String stateName) {
		for (String str : inputs.keySet()) {
			if (str.startsWith(sensorname) && str.endsWith(stateName))
				return inputs.get(sensorname + stateName);
		}
		return null;
	}

	public TreeMap<Double, Double> getOutputs(String mode) {
		return situations.get(mode).getOutputs();
	}

	public HashMap<String, TreeMap<Double, Double>> getOutputs() {
		HashMap<String, TreeMap<Double, Double>> results = new HashMap<>();
		for (String key : situations.keySet()) {
			results.put(key, situations.get(key).getOutputs());
		}
		return results;
	}

	public HashMap<String, Situation> getSituations() {
		return situations;
	}

	public Set<String> getSituationsKeys() {
		return situations.keySet();
	}

	public TreeMap<Double, Double> getSoftThreshold(String mode) {
		return situations.get(mode).getSoftThreshold();
	}

	public TreeMap<Double, State> getSoftThresholdState(String mode) {
		return situations.get(mode).getSoftThresholdsState();
	}

	public Double getHardThreshold(String mode) {
		return situations.get(mode).getHardThreshold();
	}
}
