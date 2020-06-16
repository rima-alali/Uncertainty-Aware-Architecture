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

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataRange;

/**
 * The abstract class captures the structure of the state space model. Also, it
 * allows to calculate the minimum and maximum value for the current belief
 * about each state through Ordinary Differential Equations (ODE).
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public abstract class StateSpaceModel implements FirstOrderDifferentialEquations {

	protected State[] states;
	protected int dataLength;
	protected Double dt;
	protected Double[] params;
	protected Double lastTimeInitiated;
	protected DataRange foundation;

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	/**
	 * The constructor initiates the states that are involved in the state space
	 * model.
	 * 
	 * @param states the physical attributes of the entity
	 */
	public StateSpaceModel(State... states) {
		this(0.5, 1, states);
	}

	/**
	 * The constructor initiates the states that are involved in the state space
	 * model and the time steps period of the ODE. It is worth mentioning that this
	 * value will also determines the number of samples considered in the
	 * calculation during a specific time period.
	 * 
	 * @param dt     is the time steps in milli-sec
	 * @param params additional inputs to the physical model
	 * @param states is the physical attributes of the entity
	 */
	public StateSpaceModel(double dt, int paramsNum, State... states) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		this.states = states;
		this.dt = dt;
		this.dataLength = states.length * 3;
		this.foundation = new DataRange();
		this.lastTimeInitiated = currentTime;
	}

	/**
	 * The constructor initiates the states that are involved in the state space
	 * model and the time steps period of the ODE. It is worth mentioning that this
	 * value will also determines the number of samples considered in the
	 * calculation during a specific time period.
	 * 
	 * @param dt        is the time steps in milli-sec
	 * @param paramsNum is the number of parameters for the inputs to the physical
	 *                  model
	 * @param timestamp is the time stamp
	 * @param states    is the physical attributes of the entity
	 */
	public StateSpaceModel(double dt, int paramsNum, double timestamp, State... states) {
		this.states = states;
		this.dt = dt;
		this.dataLength = states.length * 3;
		this.foundation = new DataRange();
		this.lastTimeInitiated = timestamp;
	}

	/**
	 * The method calculates the original value of a specific attribute of the
	 * physical entity. It is expected here to call the actual physical model (i.e.
	 * plant) to calculate the required attribute.
	 * 
	 * @return original value
	 */
	public abstract double value();

	/**
	 * The method calculates the minimum value of a specific attribute of the
	 * physical entity. It is expected here to call the actual physical model (i.e.
	 * plant) to calculate the required attribute. Also, the values of other
	 * parameters involved should be taken into account. They must be chosen to have
	 * the most possible minimum value of the physical attribute.
	 * 
	 * @return the minimum value of the physical attribute
	 */
	public abstract double minValue();

	/**
	 * The method calculates the maximum value of a specific attribute of the
	 * physical entity. It is expected here to call the actual physical model (i.e.
	 * plant) to calculate the required attribute. Also, the values of other
	 * parameters involved should be taken into account. They must be chosen to have
	 * the most possible minimum value of the physical attribute.
	 * 
	 * @return the maximum value of the physical attribute
	 */
	public abstract double maxValue();

	/**
	 * This method represents the physical model of the entity.
	 * 
	 * @return the value of a physical attribute that other states depend on
	 */
	public abstract double getValueFromPlant(Double... vals);

	/**
	 * This method allows for processing the parameters to a specific form
	 * 
	 * @return the physical model parameters
	 */
	public abstract Double[] getParams();

	/**
	 * This method sets the parameter values
	 * 
	 * @param params values
	 */
	public abstract void setParams(Double... params);

	/**
	 * The method returns the value of the time interval for calculating the ODE
	 * 
	 * @return time interval
	 */
	public Double getDt() {
		return dt;
	}

	/**
	 * The method returns the values of first block to calculate the state space
	 * model. For instance, in case of a vehicle, the basic block is acceleration to
	 * calculate the speed and the position.
	 * 
	 * @return basic state
	 */
	public DataRange getBasicDataRange() {
		return foundation;
	}

	/**
	 * The method sets the first block to calculate the state space model.
	 * 
	 * @param dr is the data range value for the first block
	 */
	public void setBasicDataRange(DataRange dr) {
		foundation = dr;

	}

	/**
	 * The method creates the initial values for calculating the state space model
	 * 
	 */
	public void createBasicDataRange() {
		foundation = new DataRange(value(), minValue(), maxValue(), System.nanoTime() / MILLISEC_NANOSEC_FACTOR);
	}

	/**
	 * The method creates the initial values for calculating the state space model
	 * with specific creation time
	 * 
	 * @param timestamp is the creation time
	 */
	public void createBasicDataRange(double timestamp) {
		foundation = new DataRange(value(), minValue(), maxValue(), timestamp);
	}

	/**
	 * The method retrieves the minimum and maximum bound of all the states in the
	 * equation. The order follows the same order of the entered states and starts
	 * with all the minimum values followed by all the maximum values.
	 * 
	 * @return min and max of all states (i.e. all mins first, then all maxs)
	 */
	public double[] getBounds() {
		double[] bounds = new double[states.length * 2];
		int index = 0;
		for (State state : states) {
			bounds[index] = state.getMinBound();
			bounds[states.length + index] = state.getMaxBound();
			index++;
		}
		return bounds;
	}

	/**
	 * The method updates the minimum and maximum values of the states and their
	 * states. All minimum values must be entered first, then all maximum values.
	 * 
	 * @param vals      new mins and max for the all states
	 * @param timestamp is the creation time of bounds
	 */
	public void setState(double[] vals, double timestamp) {
		for (int i = 0; i < states.length; i++) {
			State v = states[i];
//			System.out.println(v.getName()+"  ____ bounds ... "+vals[i]+"  "+vals[i + states.length]+" , val "+vals[i + 2 * states.length]);
			v.setData(vals[i + 2 * states.length], vals[i], vals[i + states.length], timestamp);
			states[i] = v;
		}
	}

	/**
	 * The method resets the minimum and maximum values of the states to equal the
	 * data.
	 * 
	 */
	public void resetBounds() {
		for (int i = 0; i < states.length; i++) {
			State v = states[i];
			v.updateBounds(v.getData(), v.getData(), v.getTimeStamp());
			states[i] = v;
		}
	}

	/**
	 * The method retrieves the timestamps of all states.
	 * 
	 * @return states timestamp
	 */
	public double[] getStatesTimeStamps() {
		double[] bounds = new double[states.length];
		int index = 0;
		for (State state : states) {
			bounds[index++] = state.getTimeStamp();
		}
		return bounds;
	}

	/**
	 * The method retrieves the names of all states.
	 * 
	 * @return states name
	 */
	public String[] getNames() {
		String[] names = new String[states.length];
		for (int i = 0; i < states.length; i++) {
			names[i] = states[i].getName();
		}
		return names;
	}

	/**
	 * The method returns the number of the states in the equation.
	 * 
	 * @return number of states
	 */
	public int getStatesCount() {
		return states.length;
	}

	/**
	 * The method returns all the states in the equation.
	 * 
	 * @return states
	 */
	public State[] getStates() {
		return states;
	}

	/**
	 * The method returns a specific state from the state space model.
	 * 
	 * @param i is the index of the state
	 * @return the state with index i
	 */
	public State getState(int i) {
		return states[i];
	}

	/**
	 * The method returns the state of a specific name
	 * 
	 * @param name of the state
	 * @return the state with a specific name
	 */
	public State getState(String name) {
		for (State state : states) {
			if (state.getName().equals(name))
				return state;
		}
		return null;
	}

	/**
	 * The method sets the name and the values of the state
	 * 
	 * @param name of the state
	 * @param the  state with a specific name
	 */
	public void setState(String name, State s) {
		for (State state : states) {
			if (state.getName().equals(name)) {
				state.setData(s.getDataRange());
			}
		}
	}

	/**
	 * The method sets the values for state with index i
	 * 
	 * @param i is the state index
	 * @param s is the state values
	 */
	public void setState(int i, State s) {
		states[i].name = s.getName();
		states[i].setData(s.getDataRange());
	}

	/**
	 * The method returns the state index with a specific name
	 * 
	 * @param name of the state
	 * @return the index of state with a specific name
	 */
	public int getStateIndex(String name) {
		for (int i = 0; i < states.length; i++) {
			if (states[i].getName().equals(name))
				return i;
		}
		return -1;
	}

	/**
	 * The method returns the states including values and boundaries.
	 * 
	 * @return states values
	 */
	public double[] getStateValuesAndBounds() {
		double[] svals = new double[3 * states.length];
		int index = 0;
		for (State state : states) {
			svals[index] = state.getMinBound();
			svals[states.length + index] = state.getMaxBound();
			svals[2 * states.length + index] = state.getData();
			index++;
		}
		return svals;
	}

	/**
	 * The methods sets the states in the model.
	 * 
	 * @param states of the model
	 */
	public void setStates(State... states) {
		for (int i = 0; i < states.length; i++) {
			this.states[i] = new State(states[i].getName());
			this.states[i].setData(states[i].getDataRange());
		}
		this.states = states;
	}

	/**
	 * The method updates the states in the equation. This method initiates the min
	 * and max to the passed value for each passed state.
	 * 
	 * @param time is the time stamp  
	 * @param newStates is the new states
	 */
	public void initiateStates(double time, State... newStates) {
		for (int i = 0; i < this.states.length; i++) {
			for (int j = 0; j < newStates.length; j++) {
				if (this.states[i].getName().equals(newStates[j].getName()))
					this.states[i].setData(newStates[j].getDataTimeStamp());
			}
		}
		setLastTimeInitiated(time);
	}

	/**
	 * The method updates all the states in the state space model equation including the bounds.
	 * 
	 * @param newStates are the new states
	 */
	public void updateStates(State... newStates) {
		for (int i = 0; i < this.states.length; i++) {
			for (int j = 0; j < newStates.length; j++) {
				if (this.states[i].getName().equals(newStates[j].getName()))
					this.states[i].setData(newStates[j].getDataRange());
			}
		}
	}

	/**
	 * The method updates all state space model boundaries
	 * 
	 * @param newStates are the new states
	 */
	public void updateStatesBoundaries(State... newStates) {
		for (int i = 0; i < this.states.length; i++) {
			for (int j = 0; j < newStates.length; j++) {
				if (this.states[i].getName().equals(newStates[j].getName()))
					this.states[i].updateBounds(newStates[j].getDataRange());
			}
		}
	}

	/**
	 * The method returns the number of parameter passed to the plant. This number
	 * helps in case the output is presented as multiple values. In such a case, the
	 * processing happen in getParams() method.
	 * 
	 * @return the number of parameters passer to the plant
	 */
	public int getParamsNum() {
		return params.length;
	}

	/**
	 * The method sets the time period for calculating the ODE
	 * 
	 * @param dt time in milli-sec
	 */
	public void setDt(Double dt) {
		this.dt = dt;
	}

	/**
	 * The method sets the time stamp of the last update for the state values.
	 * 
	 * @param lastTime for updating the state space model
	 */
	public void setLastTimeInitiated(Double lastTime) {
		this.lastTimeInitiated = lastTime;
	}

	/**
	 * The method returns the time stamp of the last update for the state values.
	 * 
	 * @return state space model time stamp
	 */
	public Double getLastTimeInitiated() {
		return lastTimeInitiated;
	}

	/**
	 * The method contains the number of minimum and maximum values for all states.
	 * It is used in the computeDerivatives method. We define it to be twice the
	 * length of the number of states.
	 */
	@Override
	public int getDimension() {
		return dataLength;
	}

	/**
	 * The method is overridden to calculate the following equation: x_i = x_i-1 +
	 * y_i * dt This equation is there for each minimum and maximum value of each
	 * state. The calculation starts with calling the minValue() and calculates all
	 * the other related minimum values for each states by entered order in the
	 * equation. Then, the calculation continues for the maximum values of the
	 * states by calling maxValue() first.
	 * 
	 */
	@Override
	public void computeDerivatives(double t, double[] y, double[] yDot)
			throws MaxCountExceededException, DimensionMismatchException {

		double temp = foundation.getMin();
		for (int i = 0; i < yDot.length / 3; i++) {
			yDot[i] = temp * dt;
			temp = y[i];
		}

		temp = foundation.getMax();
		for (int i = yDot.length / 3; i < (2 * yDot.length / 3); i++) {
			yDot[i] = temp * dt;
			temp = y[i];
		}

		temp = foundation.getData();
		for (int i = 2 * yDot.length / 3; i < yDot.length; i++) {
			yDot[i] = temp * dt;
			temp = y[i];
		}

	}

}
