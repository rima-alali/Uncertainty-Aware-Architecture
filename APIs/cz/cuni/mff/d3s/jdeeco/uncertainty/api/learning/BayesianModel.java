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
package cz.cuni.mff.d3s.jdeeco.uncertainty.api.learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import gov.sandia.cognition.evaluator.Evaluator;
import gov.sandia.cognition.learning.algorithm.IncrementalLearner;
import gov.sandia.cognition.learning.data.DefaultInputOutputPair;
import gov.sandia.cognition.learning.data.InputOutputPair;
import gov.sandia.cognition.math.matrix.Matrix;
import gov.sandia.cognition.math.matrix.MatrixFactory;
import gov.sandia.cognition.math.matrix.Vectorizable;
import gov.sandia.cognition.statistics.Distribution;
import gov.sandia.cognition.statistics.bayesian.BayesianCredibleInterval;
import gov.sandia.cognition.statistics.bayesian.BayesianLinearRegression;
import gov.sandia.cognition.statistics.bayesian.BayesianLinearRegression.IncrementalEstimator.SufficientStatistic;
import gov.sandia.cognition.statistics.distribution.MultivariateGaussian;
import gov.sandia.cognition.statistics.distribution.UnivariateGaussian;

public class BayesianModel {

	protected HashMap<String, TreeMap<Double, Double>> inputs;
	protected TreeMap<Double, Double> outputs;
	protected TreeSet<Double> timestamps;
	protected TreeMap<Double, BayesianCredibleInterval> intervals;
	protected double confidence;
	protected int dim;
	protected final double NANO_MILI_FACTOR = 1000000;
	
	private double limit = 100;

	public BayesianModel() {
		this(3, 0.95);
	}

	public BayesianModel(int dim, double confidence) {
		this.dim = dim;
		this.confidence = confidence;
		this.intervals = new TreeMap<>();
	}

	public double getConfidence() {
		return confidence;
	}

	public int getDim() {
		return dim;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}

	public void setTimestamps(TreeSet<Double> timestamps) {
		this.timestamps = timestamps;
	}

	public TreeSet<Double> getTimestamps() {
		return timestamps;
	}

	public HashMap<String, TreeMap<Double, Double>> getInputs() {
		return inputs;
	}

	public TreeMap<Double, Double> getOutputs() {
		return outputs;
	}

	public void setIntervals(TreeMap<Double, BayesianCredibleInterval> intervals) {
		this.intervals = intervals;
	}

	public void addInterval(BayesianCredibleInterval interval, Double timestamp) {
		this.intervals.put(timestamp, interval);
	}

	public void setInputs(HashMap<String, TreeMap<Double, Double>> inputs) {
		this.inputs = inputs;
	}

	public void setOutputs(TreeMap<Double, Double> outputs) {
		this.outputs = outputs;
	}

	public TreeMap<Double, BayesianCredibleInterval> getIntervalsTree() {
		return intervals;
	}

	public TreeMap<Double, Double> getIntervalLowerBounds() {
		TreeMap<Double, Double> lower = new TreeMap<>();
		for (Double key : intervals.keySet()) {
			lower.put(key, intervals.get(key).getLowerBound());
		}
		return lower;
	}

	public TreeMap<Double, Double> getIntervalUpperBounds() {
		TreeMap<Double, Double> upper = new TreeMap<>();
		for (Double key : intervals.keySet()) {
			upper.put(key, intervals.get(key).getUpperBound());
		}
		return upper;
	}

	public TreeMap<Double, Double> getIntervalCentralBounds() {
		TreeMap<Double, Double> central = new TreeMap<>();
		for (Double key : intervals.keySet()) {
			central.put(key, intervals.get(key).getCentralValue());
		}
		return central;
	}

	public TreeMap<Double, Double> getIntervalsIntervals() {
		TreeMap<Double, Double> central = new TreeMap<>();
		for (Double key : intervals.keySet()) {
			central.put(key, intervals.get(key).getUpperBound() - intervals.get(key).getLowerBound());
		}
		return central;
	}

	public BayesianCredibleInterval getInterval(Double key) {
		return intervals.get(key);
	}

	/*
	 * train
	 */

	public void train(HashMap<String, TreeMap<Double, Double>> ins, TreeMap<Double, Double> outs, int w) {
		if (ins == null)
			return;

		this.inputs = ins;
		this.outputs = outs;
		this.intervals = new TreeMap<>();
		this.timestamps = createTimestamps(ins, w);

		this.inputs = TreeMapOperations.fill(inputs, timestamps);
		this.outputs = TreeMapOperations.resampleShift(outputs, timestamps);
//		System.out.println("ts "+timestamps.size() +" \n ins "+inputs.get("radar").keySet()+" \n outs "+outputs.keySet()+"  "+w);
		trainSample();
	}

	private void trainSample() {

//		System.out.println(inputs+"  "+outputVals+"  "+historyPeriod);
		if (getInputs() == null || getTimestamps() == null || getTimestamps().size() < 2)
			return;
		BayesianLinearRegression instance = getInstance();
		ArrayList<InputOutputPair<? extends Vectorizable, Double>> data = makeInputOutputPairs(getInputs(),
				getOutputs());
//		System.out.println(" /////////  "+data.size()+"   "+timestamps.size());
		if (data == null)
			return;
		Evaluator<? super Vectorizable, ? extends Distribution<Double>> incrementalPredictive = getIncrementalPredictive(
				data, instance, getInputs().size());
		calculateObservations(data, incrementalPredictive, getTimestamps());
	}

	/*
	 * learning
	 */

	public TreeMap<Double, Double> corr(Double y, int w, int windowCnt, int windowSize) {
		TreeMap<Double, Double> lowerbci = getIntervalLowerBounds();
		TreeMap<Double, Double> uppearbci = getIntervalUpperBounds();
		TreeMap<Double, Double> centralbci = getIntervalCentralBounds();
//		System.err.println("inputs "+inputs+" \n outputs "+outputs+" \n interval "+intervals);
		return getCorrelations(y, w, lowerbci, uppearbci, centralbci, windowCnt, windowSize);
	}

	/*
	 * *************************************************************
	 * ***************** private ***********************************
	 * *************************************************************
	 */

	private TreeMap<Double, Double> getCorrelations(Double y, int w, TreeMap<Double, Double> lowerbci,
			TreeMap<Double, Double> uppearbci, TreeMap<Double, Double> centralbci, int windowCnt, int windowSize) {
		if (lowerbci == null || uppearbci == null || centralbci == null || inputs == null)
			return new TreeMap<>();
		if (lowerbci.size() == 0 || uppearbci.size() == 0 || centralbci.size() == 0 || inputs.size() == 0)
			return new TreeMap<>();

		TreeMap<Double, Double> x1Min = TreeMapOperations.minus(lowerbci, centralbci); // mean
		TreeMap<Double, Double> x1Max = TreeMapOperations.minus(uppearbci, centralbci); // mean

		TreeMap<Double, Double> nmin = new TreeMap<>();
		TreeMap<Double, Double> nmax = new TreeMap<>();
		TreeMap<Double, Double> npart = new TreeMap<>();
		TreeMap<Double, Double> n = new TreeMap<>();

		TreeMap<Double, Double> var2 = new TreeMap<>();
		TreeMap<Double, Double> means = TreeMapOperations.mean(inputs, getTimestamps());

		for (String in : inputs.keySet()) {
			TreeMap<Double, Double> x2 = TreeMapOperations.minus(inputs.get(in), means); // mean
			if (x2 == null)
				return new TreeMap<>();
			x2 = TreeMapOperations.minus(x2, y);
//			System.out.println("x2 : "+x2+" x1min "+x1Min+"  x2max "+x1Max);
			nmin = TreeMapOperations.multiply(x1Min, x2);
			nmax = TreeMapOperations.multiply(x1Max, x2);
//			System.out.println("nmin "+nmin+" nmax "+nmax);
			npart = TreeMapOperations.plus(nmax, nmin);
			n = TreeMapOperations.plus(npart, n);
			var2 = TreeMapOperations.plus(TreeMapOperations.pow(x2, 2), var2);
//			System.out.println(" \n means ..."+StatisticalOperators.fmean(inputs.get(in), w, 0, windowCnt, windowSize)+"  \n vars : "+var2);
		}

		TreeMap<Double, Double> var1Min = TreeMapOperations.pow(x1Min, 2); // variance
		TreeMap<Double, Double> var1Max = TreeMapOperations.pow(x1Max, 2); // variance
		TreeMap<Double, Double> varSum = TreeMapOperations.plus(var1Min, var1Max); // variance
		TreeMap<Double, Double> sqrtVar1 = TreeMapOperations.sqrt(varSum);

		TreeMap<Double, Double> sqrtVar2 = TreeMapOperations.sqrt(var2); // calculate the sum of ()
//		System.out.println("var1 "+sqrtVar1+"  var2 "+var2);
		TreeMap<Double, Double> d = TreeMapOperations.multiply(sqrtVar1, sqrtVar2);
//		System.err.println("n "+n+" d "+d);
		return TreeMapOperations.divide(n, d);// correlations
	}

	private Evaluator<? super Vectorizable, ? extends Distribution<Double>> getIncrementalPredictive(
			ArrayList<InputOutputPair<? extends Vectorizable, Double>> data, BayesianLinearRegression instance,
			int size) {
		// learn (number of parameters)
		IncrementalLearner<InputOutputPair<? extends Vectorizable, Double>, SufficientStatistic> incremental = new BayesianLinearRegression.IncrementalEstimator(
				size);
		SufficientStatistic posterior = incremental.createInitialLearnedObject();

		// estimate/predict
		for (InputOutputPair<? extends Vectorizable, Double> pair : data) {
			incremental.update(posterior, pair);
		}

//		System.out.println(inputs+"  "+outputs+"  "+data);
		return instance.createPredictiveDistribution(posterior.create());
	}

	// TODO: add the prior to be uniform in range
	private BayesianLinearRegression getInstance() {
		BayesianLinearRegression instance = new BayesianLinearRegression(dim);
//		UniformDistribution outputDist = new UniformDistribution(-1,1);
		MultivariateGaussian prior = new MultivariateGaussian(1); //
		instance.setWeightPrior(prior);
//		instance.setOutputVariance(1);
		return instance;
	}

	// TODO: delete the NaN rows
	private ArrayList<InputOutputPair<? extends Vectorizable, Double>> makeInputOutputPairs(
			HashMap<String, TreeMap<Double, Double>> inputs, TreeMap<Double, Double> outputs) {

		ArrayList<InputOutputPair<? extends Vectorizable, Double>> results = new ArrayList<InputOutputPair<? extends Vectorizable, Double>>();
		MatrixFactory<? extends Matrix> mf = MatrixFactory.getDefault();
		Double[] ts = new Double[getTimestamps().size()];
		ts = getTimestamps().toArray(ts);
		Matrix m = mf.createMatrix(inputs.size(), getTimestamps().size());
		int i = 0;
		for (String elem : inputs.keySet()) {
			TreeMap<Double, Double> val = TreeMapOperations.resampleInterpolation(inputs.get(elem), getTimestamps());
			for (int j = 0; j < ts.length; j++) {
				m.set(i, j, val.get(ts[j]));
			}
			i++;
		}

		TreeMap<Double, Double> out = TreeMapOperations.resampleInterpolation(outputs, getTimestamps());
//		System.out.println("matrix  "+m+" \n "+outputs+" \n "+out);
		for (i = 0; i < ts.length; i++) {
//			System.out.println("---------------  "+m.getColumn(i)+"   "+val.get(ts[i]));
			results.add(new DefaultInputOutputPair<>(m.getColumn(i), out.get(ts[i])));
		}
		return results;
	}

	private void calculateObservations(ArrayList<InputOutputPair<? extends Vectorizable, Double>> data,
			Evaluator<? super Vectorizable, ? extends Distribution<Double>> incrementalPredictive,
			TreeSet<Double> timestamps) {
		int index = 0;
		Double[] ts = new Double[timestamps.size()];
		ts = timestamps.toArray(ts);
//		System.err.println("size .... "+data.size());
		for (InputOutputPair<? extends Vectorizable, Double> pair : data) {
//			System.out.println(pair.getInput());
			Distribution<Double> i = incrementalPredictive.evaluate(pair.getInput());
			UnivariateGaussian.CDF cdf = ((UnivariateGaussian) i).getCDF(); // set the mean and the variance from i
			BayesianCredibleInterval result = BayesianCredibleInterval.compute(cdf, confidence);
			addInterval(result, ts[index++]);
//			System.out.println("interval .... "+result+"  **** "+ts[index-1]);
		}
//		System.err.println("intervals .... "+getIntervalsTree().keySet()+"  \n "+timestamps);
	}

	public <T> ArrayList<DataTimeStamp<T>> getData(TreeMap<Double, T> data) {
		ArrayList<DataTimeStamp<T>> vals = new ArrayList<>();
		for (Double key : data.keySet()) {
			vals.add(new DataTimeStamp(data.get(key), key));
		}
		return vals;
	}

	public <T> TreeMap<Double, T> getTreeMap(ArrayList<DataTimeStamp<T>> data) {
		TreeMap<Double, T> vals = new TreeMap();
		for (DataTimeStamp<T> key : data) {
			vals.put(key.getTimestamp(), key.getData());
		}
		return vals;
	}

	private HashMap<String, TreeMap<Double, Double>> cleanData(HashMap<String, TreeMap<Double, Double>> tree) {
		for (String string : tree.keySet()) {
			tree.replace(string, cleanData(tree.get(string)));
		}
		return tree;
	}

	private TreeMap<Double, Double> cleanData(TreeMap<Double, Double> tree) {
		for (Double t : tree.keySet()) {
			if (Double.isNaN(tree.get(t)))
				tree.remove(t);
		}
//		System.out.println(tree);
		return tree;
	}

	private TreeSet<Double> createTimestamps(HashMap<String, TreeMap<Double, Double>> ins, int w) {		
		if (ins == null || ins.size() == 0)
			return new TreeSet<>();

		Double[] max = new Double[ins.size()];
		Integer[] size = new Integer[ins.size()];
		int i = 0;
		for (String key : ins.keySet()) {
			if(ins.get(key).size() == 0) return new TreeSet<Double>();
			max[i] = ins.get(key).lastKey();
			size[i] = ins.get(key).size();
			i++;
		}
//		System.out.println(Arrays.toString(max)+"   "+Arrays.toString(size));
		Double minAll = Collections.min(Arrays.asList(max)) - w;
		Double maxAll = Collections.max(Arrays.asList(max));
		Integer sizeAll = Collections.max(Arrays.asList(size));
		
//		System.err.println("train .... " + minAll+"   "+maxAll+"  "+sizeAll+"  "+(sizeAll > 0)+" && "+(maxAll > minAll));

		if (sizeAll > 0 && maxAll > minAll) {
			double steps = (maxAll - minAll) / sizeAll;
			TreeSet<Double> result = TreeMapOperations.createUnifiedTimeStamps(minAll, maxAll, steps);
			return result;
		} else
			return new TreeSet<>();
	}
	
//	private TreeSet<Double> createTimestamps(int w) {
//		if (getOutputs() == null || getOutputs().size() == 0)
//			return new TreeSet<>();
//
//		double steps = w / getOutputs().size();
//		TreeSet<Double> result = BayesianOperations.createUnifiedTimeStamps(getOutputs().lastKey() - w,
//				getOutputs().lastKey(), steps);
//		return result;
//
//	}

	private Double getLastKey(TreeMap<Double, Double> data) {
		if (data.size() > 0)
			while (data.lastEntry() == null) {
				data.remove(data.lastKey());
			}
		return data.lastKey();
	}
}
