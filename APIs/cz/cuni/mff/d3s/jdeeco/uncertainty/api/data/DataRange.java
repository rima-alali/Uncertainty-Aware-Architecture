package cz.cuni.mff.d3s.jdeeco.uncertainty.api.data;

import java.io.Serializable;

/**
 * This type of data contains a timestamped data and the minimum and maximum
 * bounds of the original value.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class DataRange implements Serializable {
	public static final long serialVersionUID = 92L;

	protected Double min;
	protected Double max;
	protected DataTimeStamp<Double> value;

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	/**
	 * The constructor initiates the data with zero and its creation time with
	 * current system time.
	 */
	public DataRange() {
		this(new DataTimeStamp<Double>(0.0, System.nanoTime() / MILLISEC_NANOSEC_FACTOR));
	}

	/**
	 * The constructor initiates the data and its creation time.
	 * 
	 * @param decoratedData is the data the min and max bound is added to
	 */
	public DataRange(DataTimeStamp<Double> decoratedData) {
		value = new DataTimeStamp<Double>();
		this.min = 0.0;
		this.max = 0.0;
		setDataRange(decoratedData);
	}

	/**
	 * The constructor initiates the data, its creation time, its min and max
	 * bounds based on the passed values.
	 * 
	 * @param val is the data
	 * @param min is the minimum bound
	 * @param max is the maximum bound
	 * @param timestamp is the creation time of the data
	 */
	public DataRange(double val, double min, double max, double timestamp) {
		value = new DataTimeStamp<Double>(val, timestamp);
		this.min = min;
		this.max = max;
	}

	/**
	 * The method returns the minimum bound of the data.
	 * 
	 * @return minimum bound
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * The method returns the maximum bound of the data.
	 * 
	 * @return maximum bound
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * The method returns the creation time of the data.
	 * 
	 * @return data creation time
	 */
	public Double getTimeStamp() {
		return value.getTimestamp();
	}

	/**
	 * The method returns the data.
	 * 
	 * @return data
	 */
	public Double getData() {
		return value.getData();
	}

	/**
	 * The method returns the data and its creation time.
	 * 
	 * @return timestamped data
	 */
	public DataTimeStamp<Double> getDataTimeStamp() {
		return value;
	}

	/**
	 * The method sets the minimum bound of the data.
	 * 
	 * @param min is the minimum bound
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * The method sets the maximum bound of the data.
	 * 
	 * @param max is the maximum bound
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * The method sets a timestampe of the data.
	 * 
	 * @param time is the creation time
	 */
	public void setTimeStamp(Double time) {
		this.value.setTimeStamp(time);
	}
	
	/**
	 * The method sets a timestamped data.
	 * 
	 * @param value is the timestamped data
	 */
	public void setDataTimeStamp(DataTimeStamp<Double> value) {
		this.value = value;
	}

	/**
	 * The method sets a timestamped data.
	 * 
	 * @param val       is the data
	 * @param timestamp is the creation time of data
	 */
	public void setDataTimeStamp(double val, double timestamp) {
		this.value.setData(val);
		this.value.setTimeStamp(timestamp);
	}

	/**
	 * The method sets the timestamped data and initiates its bounds with the data
	 * itself.
	 * 
	 * @param value is the timestamped data with its bounds
	 */
	public void setDataRange(DataTimeStamp<Double> value) {
		setDataRange(value.getData(), value.getData(), value.getData(), value.getTimestamp());
	}

	/**
	 * The method sets the timestamped data and initiates its bounds with the min
	 * and max passed from the parameters.
	 * 
	 * @param val       is the data
	 * @param min       is the minimum bound
	 * @param max       is the maximum bound
	 * @param timestamp is the creation time of the data
	 */
	public void setDataRange(double val, double min, double max, double timestamp) {
		setDataTimeStamp(val, timestamp);
		setMin(min);
		setMax(max);
	}

	/**
	 * The method sets the timestamped data and initiates its bound with the min and
	 * max passed from the parameters.
	 * 
	 * @param value is the timestamped data with its min and max bounds
	 */
	public void setDataRange(DataRange value) {
		setDataRange(value.getData(), value.getMin(), value.getMax(), value.getTimeStamp());
	}

	/**
	 * The method returns a string with the related data, its timestamp, and min and
	 * max bounds
	 * 
	 */
	@Override
	public String toString() {
		return " val: " + value.toString() + " , min " + min.toString() + " , max " + max.toString();
	}
}
