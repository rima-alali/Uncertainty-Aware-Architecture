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

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;

/**
 * This class provides an API to perform the calculation of inaccuracy caused by
 * delays (i.e. stale data) of physical entity. The developer should provide a
 * state-space model and the values in ORDER. The class allows to call the
 * minimum and maximum values as well as the interval between.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class InaccuracyModel {

	/*
	 * The variables are required from the developer
	 */
	protected StateSpaceModel model;
	/*
	 * The variables are possible to be set by the developer
	 */
	protected Double scale;
	protected Double currentTime;
	protected Double timeperiodCVState;
	private Boolean isInternalCurrentTime;

	private static final double TIMEPERIOD = 1000000;
	private static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	/**
	 * The constructor initiates the state space model
	 * 
	 * @param model is the state space model
	 */
	public InaccuracyModel(StateSpaceModel model) {
		setStateSpaceData(0.0, model, true);
	}

	/**
	 * The constructor initiates the state space model with current time
	 * 
	 * @param currenttime is the current passed time
	 * @param model       is the state space model
	 */
	public InaccuracyModel(double currenttime, StateSpaceModel model) {
		setStateSpaceData(currenttime, model, false);
	}

	/**
	 * The method sets the scale of the integration periods.
	 * 
	 * @param i is the scale
	 */
	public void setScale(double i) {
		scale = i;
	}

	/**
	 * The method calculates the minimum inaccuracy of state value.
	 * 
	 * @param state is a physical attribute
	 * @return the minimum bound of the state
	 */
	public double inaccMin(State state) {
		compute(0.0);
		return minBound(state);
	}

	/**
	 * The method calculates the maximum inaccuracy of state value.
	 * 
	 * @param state is a physical attribute
	 * @return the maximum bound of the state
	 */
	public double inaccMax(State state) {
		compute(0.0);
		return maxBound(state);
	}

	/**
	 * The method calculates the interval between the maximum and the minimum
	 * inaccuracy of state value.
	 * 
	 * @param state is a physical attribute
	 * @return the interval between state bounds
	 */
	public double inaccInterval(State state) {
		compute(0.0);
		return intervalValue(state);
	}

	/**
	 * The method calculates the value of inaccuracy of state value.
	 * 
	 * @param state is a physical attribute
	 * @return the state value
	 */
	public double inaccValue(State state) {
		compute(0.0);
		return value(state);
	}

	/**
	 * The method calculates the inaccuracy of each state
	 * 
	 * @return the calculated states
	 */
	public State[] inaccStates() {
		return finaccStates(0);
	}

	/**
	 * The method calculates the future minimum inaccuracy of state value.
	 * 
	 * @param state  is a physical attribute
	 * @param future is the future required time in milli-sec
	 * @return the future minimum bound of the state
	 */
	public double finaccMin(State state, double future) {
		compute(future);
		return minBound(state);
	}

	/**
	 * The method calculates the future maximum inaccuracy of state value.
	 * 
	 * @param state  is a physical attribute
	 * @param future is the future required time in milli-sec
	 * @return the maximum bound of the state
	 */
	public double finaccMax(State state, double future) {
		compute(future);
		return maxBound(state);
	}

	/**
	 * The method calculates the future interval between the maximum and the minimum
	 * inaccuracy of state value.
	 * 
	 * @param state  is a physical attribute
	 * @param future is the future required time in milli-sec
	 * @return the interval between state bounds
	 */
	public double finaccInterval(State state, double future) {
		compute(future);
		return intervalValue(state);
	}

	/**
	 * The method calculates the value of inaccuracy of state value in future.
	 * 
	 * @param state  is a physical attribute
	 * @param future is the future period
	 * @return the state value
	 */
	public double finaccValue(State state, double future) {
		compute(future);
		return value(state);
	}

	/**
	 * The method calculates the inaccuracy of each state in the future
	 * 
	 * @param future is the future period
	 * @return the states in future
	 */
	public State[] finaccStates(double future) {
		compute(future);
		return model.getStates();
	}

	/**
	 * The methods sets the physical model used to calculate the state space model
	 * 
	 */
	public void setModel(StateSpaceModel model) {
		this.model = model;
	}

	/**
	 * The method sets the time stamp of the state space model update
	 * 
	 * @param currentTime is the last time stamp of state space model
	 */
	public void setCurrentTime(Double currentTime) {
		this.currentTime = currentTime;
	}

	/*
	 **********************************************************************************
	 **********************************************************************************
	 **********************************************************************************
	 ********************************************************************************** 
	 **********************************************************************************
	 */

	/**
	 * The method computes the new future bounds (i.e. min and max) for all states
	 * value.
	 * 
	 * @param future is the future time period in milli-sec
	 */
	private void compute(double future) {
		FirstOrderIntegrator integrator = new MidpointIntegrator(1);
		integrator.setMaxEvaluations((int) TIMEPERIOD);
		checkCurrentTime();
		double[] boundaries = model.getStateValuesAndBounds();
		if ((currentTime + future - model.getStatesTimeStamps()[0]) > 0) {
			integrator.integrate(model, model.getStatesTimeStamps()[0] / scale, boundaries,
					(currentTime + future) / scale, boundaries);
			model.setState(boundaries, currentTime + future);
		}
	}

	/**
	 * The method sets the state space model and external timestamp related to
	 * calculating the inaccuracy.
	 * 
	 * @param currenttime is the timestamp
	 * @param model       is the state space model
	 * @param isNotAdded  is a flag for adding the timestamp
	 */
	private void setStateSpaceData(double currenttime, StateSpaceModel model, boolean isNotAdded) {
		if (isNotAdded) {
			isInternalCurrentTime = true;
			this.currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		} else {
			isInternalCurrentTime = false;
			this.currentTime = currenttime;
		}
		this.model = model;
		this.scale = 1.0;
		this.timeperiodCVState = 0.0;
	}

	/**
	 * The method returns the maximum state value.
	 * 
	 * @param state is the target state
	 * @return the maximum bound
	 */
	private double maxBound(State state) {
		State[] states = model.getStates();
		for (int i = states.length - 1; i >= 0; i--) {
			if (state.getName().equals(states[i].getName()))
				return states[i].getMaxBound();
		}
		return 0.0;
	}

	/**
	 * The method returns the minimum state value.
	 * 
	 * @param state is the target state
	 * @return the minimum bound
	 */
	private double minBound(State state) {
		State[] states = model.getStates();
		for (int i = states.length - 1; i >= 0; i--) {
			if (state.getName().equals(states[i].getName()))
				return states[i].getMinBound();
		}
		return 0.0;
	}

	/**
	 * The method returns the interval between the maximum and minimum values.
	 * 
	 * @param state is the target state
	 * @return the interval between state value bounds
	 */
	private double intervalValue(State state) {
		return maxBound(state) - minBound(state);
	}
	
	
	/**
	 * The method returns the value of a state with the same name as the passed one
	 * 
	 * @param state holds the required name
	 * @return state value with a specific name
	 */
	private double value(State state) {
		State[] states = model.getStates();
		for (int i = states.length - 1; i >= 0; i--) {
			if (state.getName().equals(states[i].getName()))
				return states[i].getData();
		}
		return 0.0;
	}

	/**
	 * The method updates the current time in case it is not external
	 * 
	 */
	private void checkCurrentTime() {
		if (isInternalCurrentTime) {
			currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		}
	}
}
