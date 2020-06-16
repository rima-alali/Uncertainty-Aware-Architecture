package cz.cuni.mff.d3s.jdeeco.uncertainty.api.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class StatisticalOperators {

	protected static final int NANO_MILI_FACTOR = 1000000; // NANO -> MILISEC
	protected static int windowCnt = 10; // SEC -> MILISEC
	protected static int windowSize = 1000; // SEC -> MILISEC

	public static boolean above(TreeMap<Double, Double> x, Double y, int w) {
		return more(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean above(TreeMap<Double, Double> x, Double y, int w, int windowCnt, int windowSize) {
		return more(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean below(TreeMap<Double, Double> x, Double y, int w) {
		return less(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean below(TreeMap<Double, Double> x, Double y, int w, int windowCnt, int windowSize) {
		return less(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean equals(TreeMap<Double, Double> x, Double y, int w) {
		return eq(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean equals(TreeMap<Double, Double> x, Double y, int w, int windowCnt, int windowSize) {
		return eq(x, y, w, 0, windowCnt, windowSize);
	}

	/*
	 * future
	 */

	public static boolean fabove(TreeMap<Double, Double> x, Double y, int w, int future) {
		return more(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fbelow(TreeMap<Double, Double> x, Double y, int w, int future) {
		return less(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fequals(TreeMap<Double, Double> x, Double y, int w, int future) {
		return eq(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fabove(TreeMap<Double, Double> x, Double y, int w, int future, int windowCnt,
			int windowSize) {
		return more(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fbelow(TreeMap<Double, Double> x, Double y, int w, int future, int windowCnt,
			int windowSize) {
		return less(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fequals(TreeMap<Double, Double> x, Double y, int w, int future, int windowCnt,
			int windowSize) {
		return eq(x, y, w, future, windowCnt, windowSize);
	}

	/*
	 * two distributions
	 */

	public static boolean above(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w) {
		return more(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean above(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int windowCnt,
			int windowSize) {
		return more(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean below(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w) {
		return less(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean below(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int windowCnt,
			int windowSize) {
		return less(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean equals(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w) {
		return eq(x, y, w, 0, windowCnt, windowSize);
	}

	public static boolean equals(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int windowCnt,
			int windowSize) {
		return eq(x, y, w, 0, windowCnt, windowSize);
	}

	// lr
	public static StudentsDistribution lr(TreeMap<Double, Double> x, int w) {
		return flr(x, w, 0, windowCnt, windowSize);
	}

	/*
	 * future ... two distributions
	 */
	public static boolean fabove(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future) {
		return more(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fbelow(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future) {
		return less(x, y, w, future, windowCnt, windowSize);
	}

	public static StudentsDistribution flr(TreeMap<Double, Double> x, int w, int future) {
		TimeSeries s = getTimeSeries(x, w, windowCnt, windowSize);
		if (x.size() > 0)
			return s.getLr(x.lastKey() + future);
		return s.getLr(0);
	}
	
	public static StudentsDistribution flr(TreeMap<Double, Double> x, Double y, int w, int future) {
		TimeSeries s = getTimeSeries(x, w, windowCnt, windowSize);
		if (x.size() > 0)
			return s.getLr(y + future);
		return s.getLr(0);
	}
	
	public static boolean fabove(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future, int windowCnt,
			int windowSize) {
		return more(x, y, w, future, windowCnt, windowSize);
	}

	public static boolean fbelow(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future, int windowCnt,
			int windowSize) {
		return less(x, y, w, future, windowCnt, windowSize);
	}

	public static StudentsDistribution flr(TreeMap<Double, Double> x, int w, int future, int windowCnt,
			int windowSize) {
		TimeSeries s = getTimeSeries(x, w, windowCnt, windowSize);
		if (x.size() > 0)
			return s.getLr(x.lastKey() + future);
		return s.getLr(0);
	}

	/*
	 * private ... check the transformation from double to int .... y threshold
	 */

	private static boolean more(TreeMap<Double, Double> x, Double y, int w, int future, int windowCnt, int windowSize) {
		TimeSeries s = getTimeSeries(x, w, windowCnt, windowSize);
		if (x.size() > 0)
			return s.getLr(x.lastKey() + future).isGreaterThan(y);
		return s.getLr(0).isGreaterThan(y);
	}

	private static boolean more(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future, int windowCnt,
			int windowSize) {
		TimeSeries xs = getTimeSeries(x, w, windowCnt, windowSize);
		TimeSeries ys = getTimeSeries(y, w, windowCnt, windowSize);
		if (x.size() > 0 && y.size() > 0)
			return xs.getLr(x.lastKey() + future).isGreaterThan(ys.getLr(y.lastKey() + future));
		return xs.getLr(0).isGreaterThan(ys.getLr(0));
	}

	private static boolean less(TreeMap<Double, Double> x, Double y, int w, int future, int windowCnt, int windowSize) {
		TimeSeries s = getTimeSeries(x, w, windowCnt, windowSize);
		if (x.size() > 0)
			return s.getLr(x.lastKey() + future).isLessThan(y);
		return s.getLr(0).isLessThan(y);
	}

	private static boolean less(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future, int windowCnt,
			int windowSize) {
		TimeSeries xs = getTimeSeries(x, w, windowCnt, windowSize);
		TimeSeries ys = getTimeSeries(y, w, windowCnt, windowSize);
		if (x.size() > 0 && y.size() > 0)
			return xs.getLr(x.lastKey() + future).isLessThan(ys.getLr(y.lastKey() + future));
		return xs.getLr(0).isLessThan(ys.getLr(0));
	}

	private static boolean eq(TreeMap<Double, Double> x, Double y, int w, int future, int windowCnt, int windowSize) {
		TimeSeries s = getTimeSeries(x, w, windowCnt, windowSize);
		if (x.size() > 0)
			return s.getLr(x.lastKey() + future).isLessOrEqualTo(y)
					&& s.getLr(x.lastKey() + future).isGreaterOrEqualTo(y);
		return s.getLr(0).isLessOrEqualTo(y) && s.getLr(0).isGreaterOrEqualTo(y);
	}

	private static boolean eq(TreeMap<Double, Double> x, TreeMap<Double, Double> y, int w, int future, int windowCnt,
			int windowSize) {
		TimeSeries xs = getTimeSeries(x, w, windowCnt, windowSize);
		TimeSeries ys = getTimeSeries(y, w, windowCnt, windowSize);
		if (x.size() > 0 && y.size() > 0)
			return xs.getLr(x.lastKey() + future).isLessOrEqualTo(ys.getLr(y.lastKey() + future))
					&& xs.getLr(x.lastKey() + future).isGreaterOrEqualTo(ys.getLr(y.lastKey() + future));
		return xs.getLr(0).isLessOrEqualTo(ys.getLr(0)) && xs.getLr(0).isGreaterOrEqualTo(ys.getLr(0));
	}

	private static int startIndex(List<Integer> times, int w) {
		if (times.size() == 0)
			return 0;
		int last = times.get(times.size() - 1);
		int start = last - w;
		int index = times.size() - 1;
		for (int i = times.size() - 1; i >= 0; i--) {
			if (start < times.get(i))
				index--;
			else
				return index;
		}
		return index < 0 ? 0 : index;
	}

	private static List<Integer> getTime(TreeMap<Double, Double> x) {
		List<Integer> times = new ArrayList<Integer>();
		new ArrayList<>();
		for (Double dataTimeStamp : x.keySet()) {
			times.add(dataTimeStamp.intValue());
		}
		return times;
	}

	private static List<Double> getValues(TreeMap<Double, Double> x) {
		List<Double> values = new ArrayList<>();
		for (Double dataTimeStamp : x.keySet()) {
			values.add(x.get(dataTimeStamp));
		}
		return values;
	}

	private static TimeSeries getTimeSeries(TreeMap<Double, Double> x, int w, int windowCnt, int windowSize) {
		List<Integer> times = getTime(x);
		List<Double> values = getValues(x);

		TimeSeries s = new TimeSeries(windowCnt, windowSize);
		s.setStartTime(w);
		for (int i = startIndex(times, w); i < times.size(); i++) {
			s.addSample(values.get(i), times.get(i));
		}
		return s;
	}
}
