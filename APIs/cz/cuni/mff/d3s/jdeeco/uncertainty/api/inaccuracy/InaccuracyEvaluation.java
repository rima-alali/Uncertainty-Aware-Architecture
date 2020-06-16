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

import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.TimeState;

/**
 * This class provides a number of operations to calculate the real values of
 * stale data of physical entity.
 * 
 * The developer should provide a state-space model and the values in ORDER. The
 * class allows to call the minimum and maximum values as well as the interval
 * between. Additionally, it is possible to calculate the value of one state
 * depending on a specific future value for other state.
 * 
 * @author Rima Al-Ali (alali@d3s.mff.cuni.cz)
 *
 */
public class InaccuracyEvaluation {

	private static final double MILLISEC_NANOSEC_FACTOR = 1000000;

	/**
	 * This method calculates the minimum value of the passed state. The calculation
	 * depends on the passed model.
	 * 
	 * @param state is the required state
	 * @param model is the state space model that should be considered
	 * @return the minimum value of the state
	 */
	public static double min(State state, StateSpaceModel model) {
		return fmin(state, model, 0.0);
	}

	/**
	 * This method calculates the maximum value of the passed state. The calculation
	 * depends on the passed model.
	 * 
	 * @param state is the required state
	 * @param model is the state space model that should be considered
	 * @return the maximum value of the state
	 */
	public static double max(State state, StateSpaceModel model) {
		return fmax(state, model, 0.0);
	}

	/**
	 * This method calculates the interval between maximum and minimum values of the
	 * passed state. The calculation depends on the passed model.
	 * 
	 * @param state is the required state
	 * @param model is the state space model that should be considered
	 * @return the interval between maximum and minimum values of the state
	 */
	public static double interval(State state, StateSpaceModel model) {
		return finterval(state, model, 0.0);
	}

	/**
	 * This method calculates the value of the passed state. The calculation depends
	 * on the passed model.
	 * 
	 * @param state is the required state
	 * @param model is the state space model that should be considered
	 * @return the value of the state
	 */
	public static double value(State state, StateSpaceModel model) {
		return fvalue(state, model, 0.0);
	}

	/**
	 * This method calculates the new state of the passed state. The calculation
	 * depends on the passed model.
	 * 
	 * @param state is the required state
	 * @param model is the state space model that should be considered
	 * @return the new state
	 */
	public static State state(State state, StateSpaceModel model) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fstate(state, model, 0.0, 1.0, currentTime);
	}

	/**
	 * This method calculates the new states of the passed model.
	 * 
	 * @param model is the state space model that have the states
	 * @return the new states
	 */
	public static State[] states(StateSpaceModel model) {
		return fstates(model, 0.0);
	}

	/*
	 * future
	 */

	/**
	 * This method calculates the future minimum value of the passed state. The
	 * calculation depends on the passed model.
	 * 
	 * @param state  is the required state
	 * @param model  is the state space model that should be considered
	 * @param future is the future time
	 * @return the future minimum value of the state
	 */
	public static double fmin(State state, StateSpaceModel model, double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fmin(state, model, future, 1.0, currentTime);
	}

	/**
	 * This method calculates the future maximum value of the passed state. The
	 * calculation depends on the passed model.
	 * 
	 * @param state  is the required state
	 * @param model  is the state space model that should be considered
	 * @param future is the future time
	 * @return the future maximum value of the state
	 */
	public static double fmax(State state, StateSpaceModel model, double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fmax(state, model, future, 1.0, currentTime);
	}

	/**
	 * This method calculates the future interval between maximum and minimum values
	 * of the passed state. The calculation depends on the passed model.
	 * 
	 * @param state  is the required state
	 * @param model  is the state space model that should be considered
	 * @param future is the future time
	 * @return the interval between future maximum and minimum values of the state
	 */
	public static double finterval(State state, StateSpaceModel model, double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return finterval(state, model, future, 1.0, currentTime);
	}

	/**
	 * This method calculates the future value of the passed state. The calculation
	 * depends on the passed model.
	 * 
	 * @param state  is the required state
	 * @param model  is the state space model that should be considered
	 * @param future is the future time
	 * @return the future value of the state
	 */
	public static double fvalue(State state, StateSpaceModel model, double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fvalue(state, model, future, 1.0, currentTime);
	}

	/**
	 * This method calculates the future state of the passed state. The calculation
	 * depends on the passed model.
	 * 
	 * @param state  is the required state
	 * @param model  is the state space model that should be considered
	 * @param future is the future time
	 * @return the future state
	 */
	public static State fstate(State state, StateSpaceModel model, double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fstate(state, model, future, 1.0, currentTime);
	}

	/**
	 * This method calculates the future states of the passed model.
	 * 
	 * @param model  is the state space model that have the states
	 * @param future is the future time
	 * @return the future states
	 */
	public static State[] fstates(StateSpaceModel model, double future) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return fstates(model, future, 1.0, currentTime);
	}

	/*
	 * ***********************************************************
	 * ******************** time customized***********************
	 * ***********************************************************
	 * 
	 * These methods are encouraged to use in case there is a specific requirements
	 * related to time.
	 */

	/**
	 * This method calculates the future minimum value of the passed state. The
	 * calculation depends on the passed model. It allows for changing the time
	 * scale of the time period entered for the ODE, and also allows for entering
	 * the current timestamp externally.
	 * 
	 * @param state     is the required state
	 * @param model     is the state space model that should be considered
	 * @param future    is the future time
	 * @param scale     is the time scale for the ODE, where the time period is
	 *                  divided by it
	 * @param timestamp is the current timestamp
	 * @return the future minimum value of the state
	 */
	public static double fmin(State state, StateSpaceModel model, double future, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(timestamp, model);
		inacc.setScale(scale);
		double result = inacc.finaccMin(state, future);
		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		return result;
	}

	/**
	 * This method calculates the future maximum value of the passed state. The
	 * calculation depends on the passed model. It allows for changing the time
	 * scale of the time period entered for the ODE, and also allows for entering
	 * the current timestamp externally.
	 * 
	 * @param state     is the required state
	 * @param model     is the state space model that should be considered
	 * @param future    is the future time
	 * @param scale     is the time scale for the ODE, where the time period is
	 *                  divided by it
	 * @param timestamp is the current timestamp
	 * @return the future maximum value of the state
	 */
	public static double fmax(State state, StateSpaceModel model, double future, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(timestamp, model);
		inacc.setScale(scale);
		double result = inacc.finaccMax(state, future);
		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		return result;
	}

	/**
	 * This method calculates the future interval between maximum and minimum values
	 * of the passed state. The calculation depends on the passed model. It allows
	 * for changing the time scale of the time period entered for the ODE, and also
	 * allows for entering the current timestamp externally.
	 * 
	 * @param state     is the required state
	 * @param model     is the state space model that should be considered
	 * @param future    is the future time
	 * @param scale     is the time scale for the ODE, where the time period is
	 *                  divided by it
	 * @param timestamp is the current timestamp
	 * @return the interval between future maximum and minimum values of the state
	 */
	public static double finterval(State state, StateSpaceModel model, double future, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(timestamp, model);
		inacc.setScale(scale);
		double result = inacc.finaccInterval(state, future);
		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		return result;
	}

	/**
	 * This method calculates the future value of the passed state. The calculation
	 * depends on the passed model. It allows for changing the time scale of the
	 * time period entered for the ODE, and also allows for entering the current
	 * timestamp externally.
	 * 
	 * @param state     is the required state
	 * @param model     is the state space model that should be considered
	 * @param future    is the future time
	 * @param scale     is the time scale for the ODE, where the time period is
	 *                  divided by it
	 * @param timestamp is the current timestamp
	 * @return the future value of the state
	 */
	public static double fvalue(State state, StateSpaceModel model, double future, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(timestamp, model);
		inacc.setScale(scale);
		double result = inacc.finaccValue(state, future);
		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		return result;
	}

	/**
	 * This method calculates the future state of the passed state. The calculation
	 * depends on the passed model. It allows for changing the time scale of the
	 * time period entered for the ODE, and also allows for entering the current
	 * timestamp externally.
	 * 
	 * @param state     is the required state
	 * @param model     is the state space model that should be considered
	 * @param future    is the future time
	 * @param scale     is the time scale for the ODE, where the time period is
	 *                  divided by it
	 * @param timestamp is the current timestamp
	 * @return the future state
	 */
	public static State fstate(State state, StateSpaceModel model, double future, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(timestamp, model);
		inacc.setScale(scale);
		State[] states = new State[model.getStatesCount()];
		State[] temp = inacc.finaccStates(future);
		for (int i = 0; i < states.length; i++) {
			states[i] = new State(temp[i].getName());
			states[i].setData(temp[i].getDataRange());
		}
		State result = states[model.getStateIndex(state.getName())];

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		return result;
	}

	/**
	 * This method calculates the future states of the passed model. It allows for
	 * changing the time scale of the time period entered for the ODE, and also
	 * allows for entering the current timestamp externally.
	 * 
	 * @param model     is the state space model that have the states
	 * @param future    is the future time
	 * @param scale     is the time scale for the ODE, where the time period is
	 *                  divided by it
	 * @param timestamp is the current timestamp
	 * @return the future states
	 */
	public static State[] fstates(StateSpaceModel model, double future, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(timestamp, model);
		inacc.setScale(scale);
		State[] result = new State[model.getStatesCount()];
		State[] temp = inacc.finaccStates(future);
		for (int i = 0; i < result.length; i++) {
			result[i] = new State(temp[i].getName());
			result[i].setData(temp[i].getDataRange());
		}
		oper.pastModelInfo(model);
		return result;
	}

	/*
	 * corresponding state
	 */

	/**
	 * The method calculates a corresponding minimum value of one state based on
	 * when another state minimum value reaches a specific value.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @return the minimum value of the target state when the minimum value of the
	 *         passed state reaches the required value
	 */
	public static double cvLower(State state, double requiredVal, State targetState, StateSpaceModel model) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return cvLower(state, requiredVal, targetState, model, 1.0, currentTime);
	}

	/**
	 * The method calculates a corresponding maximum value of one state based on
	 * when another state maximum value reaches a specific value.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @return the maximum value of the target state when the maximum value of the
	 *         passed state reaches the required value
	 */
	public static double cvUpper(State state, double requiredVal, State targetState, StateSpaceModel model) {
		double currentTime = System.nanoTime() / MILLISEC_NANOSEC_FACTOR;
		return cvUpper(state, requiredVal, targetState, model, 1.0, currentTime);
	}

	/**
	 * The method calculates a corresponding minimum value of one state based on
	 * when another state minimum value reaches a specific value. It allows for
	 * changing the time scale of the time period entered for the ODE, and also
	 * allows for entering the current timestamp externally.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @param scale       is the time scale for the ODE, where the time period is
	 *                    divided by it
	 * @param timestamp   is the current timestamp
	 * @return the minimum value of the target state when the minimum value of the
	 *         passed state reaches the required value
	 */
	public static double cvLower(State state, double requiredVal, State targetState, StateSpaceModel model,
			double scale, double timestamp) {
		return cvLowerDataTimeStamp(state, requiredVal, targetState, model, scale, timestamp).getData();
	}

	/**
	 * The method calculates a corresponding timestamped minimum value of one state
	 * based on when another state minimum value reaches a specific value. It allows
	 * for changing the time scale of the time period entered for the ODE, and also
	 * allows for entering the current timestamp externally.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @param scale       is the time scale for the ODE, where the time period is
	 *                    divided by it
	 * @param timestamp   is the current timestamp
	 * @return the timestamped minimum value of the target state when the minimum
	 *         value of the passed state reaches the required value
	 */
	public static DataTimeStamp<Double> cvLowerDataTimeStamp(State state, double requiredVal, State targetState,
			StateSpaceModel model, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(model.getLastTimeInitiated(), model);
		inacc.setScale(scale);
		double future = 0;
		double timesteps = (timestamp - model.getLastTimeInitiated()) / 10;
		if (timesteps == 0)
			timesteps = 100;
		Double limit = state.getMinBound();
		State[] states = model.getStates();
		int index = model.getStateIndex(state.getName());
		int targetindex = model.getStateIndex(targetState.getName());
		while (limit.intValue() > requiredVal) {
			model.createBasicDataRange(model.getLastTimeInitiated() + future);
			inacc.setCurrentTime(model.getLastTimeInitiated() + future);
			inacc.setModel(model);
			future += timesteps;
			states = inacc.finaccStates(future);
			limit = states[index].getMinBound();
		}
		double result = states[targetindex].getMinBound();

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());

		return new DataTimeStamp<Double>(result, model.getLastTimeInitiated() + future);
	}

	/**
	 * The method calculates a corresponding data range of one state based on when
	 * another state minimum value reaches a specific value. It allows for changing
	 * the time scale of the time period entered for the ODE, and also allows for
	 * entering the current timestamp externally.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @param scale       is the time scale for the ODE, where the time period is
	 *                    divided by it
	 * @param timestamp   is the current timestamp
	 * @return the timestamped data range of the target state when the minimum value
	 *         of the passed state reaches the required value. The timestamps are
	 *         the difference in time for each value.
	 */
	public static TimeState cvLowerState(State state, double requiredVal, State targetState, StateSpaceModel model,
			double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);

		DataTimeStamp<Double> resultmin = cvLowerDataTimeStamp(state, requiredVal, targetState, model, scale,
				timestamp);

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());

		State[] states = model.getStates();
		State[] statesval = new State[states.length];
		State[] statesmax = new State[states.length];
		for (int i = 0; i < states.length; i++) {
			statesval[i] = new State(states[i].getName());
			statesval[i].setData(states[i].getData(), states[i].getData(), states[i].getData(),
					states[i].getTimeStamp());
			statesmax[i] = new State(states[i].getName());
			statesmax[i].setData(states[i].getMaxBound(), states[i].getMaxBound(), states[i].getMaxBound(),
					states[i].getTimeStamp());
		}

		// val
		StateSpaceModel modelval = model;
		modelval.setStates(statesval);
		DataTimeStamp<Double> resultval = cvLowerDataTimeStamp(state, requiredVal, targetState, model, scale,
				timestamp);

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());

		// max
		StateSpaceModel modelmax = model;
		modelmax.setStates(statesmax);
		DataTimeStamp<Double> resultmax = cvLowerDataTimeStamp(state, requiredVal, targetState, model, scale,
				timestamp);

		TimeState result = new TimeState("result");
		result.setMin(resultmin.getData(), resultmin.getTimestamp());
		result.setDataTimeStamp(resultval.getData(), resultval.getTimestamp());
		result.setMax(resultmax.getData(), resultmax.getTimestamp());
		result.setTimestamp(timestamp);

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());
		return result;
	}

	/**
	 * The method calculates a corresponding maximum value of one state based on
	 * when another state maximum value reaches a specific value. It allows for
	 * changing the time scale of the time period entered for the ODE, and also
	 * allows for entering the current timestamp externally.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @param scale       is the time scale for the ODE, where the time period is
	 *                    divided by it
	 * @param timestamp   is the current timestamp
	 * @return the maximum value of the target state when the maximum value of the
	 *         passed state reaches the required value.
	 */
	public static double cvUpper(State state, double requiredVal, State targetState, StateSpaceModel model,
			double scale, double timestamp) {
		return cvUpperDataTimeStamp(state, requiredVal, targetState, model, scale, timestamp).getData();
	}

	/**
	 * The method calculates a corresponding timestamped maximum value of one state
	 * based on when another state maximum value reaches a specific value. It allows
	 * for changing the time scale of the time period entered for the ODE, and also
	 * allows for entering the current timestamp externally.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @param scale       is the time scale for the ODE, where the time period is
	 *                    divided by it
	 * @param timestamp   is the current timestamp
	 * @return the timestamped maximum value of the target state when the maximum
	 *         value of the passed state reaches the required value
	 */
	public static DataTimeStamp<Double> cvUpperDataTimeStamp(State state, double requiredVal, State targetState,
			StateSpaceModel model, double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		InaccuracyModel inacc = new InaccuracyModel(model.getLastTimeInitiated(), model);
		inacc.setScale(scale);
		double future = 0;
		double timesteps = (timestamp - model.getLastTimeInitiated()) / 10;
		if (timesteps == 0)
			timesteps = 100;
		Double limit = state.getMaxBound();
		State[] states = model.getStates();
		int index = model.getStateIndex(state.getName());
		int targetindex = model.getStateIndex(targetState.getName());
		while (limit.intValue() < requiredVal) {
			model.createBasicDataRange(model.getLastTimeInitiated() + future);
			inacc.setCurrentTime(model.getLastTimeInitiated() + future);
			inacc.setModel(model);
			future += timesteps;
			states = inacc.finaccStates(future);
			limit = states[index].getMaxBound();
		}

		double result = states[targetindex].getMaxBound();

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());

		return new DataTimeStamp<Double>(result, model.getLastTimeInitiated() + future);
	}

	/**
	 * The method calculates a corresponding data range of one state based on when
	 * another state maximum value reaches a specific value. It allows for changing
	 * the time scale of the time period entered for the ODE, and also allows for
	 * entering the current timestamp externally.
	 * 
	 * @param state       is the state the should reach a specific value
	 * @param requiredVal is the value that it should be reached
	 * @param targetState is the state that the developer is interested in
	 * @param model       is the state space model for the physical entity
	 * @param scale       is the time scale for the ODE, where the time period is
	 *                    divided by it
	 * @param timestamp   is the current timestamp
	 * @return the timestamped data range of the target state when the maximum value
	 *         of the passed state reaches the required value. The timestamps are
	 *         the difference in time for each value.
	 */
	public static TimeState cvUpperState(State state, double requiredVal, State targetState, StateSpaceModel model,
			double scale, double timestamp) {
		StateSpaceModelOperations oper = new StateSpaceModelOperations(model.getStatesCount());
		oper.copyModelInfo(model);
		DataTimeStamp<Double> resultmin = cvUpperDataTimeStamp(state, requiredVal, targetState, model, scale,
				timestamp);

		State[] states = model.getStates();
		State[] statesval = new State[states.length];
		State[] statesmax = new State[states.length];
		for (int i = 0; i < states.length; i++) {
			statesval[i] = new State(states[i].getName());
			statesval[i].setData(states[i].getData(), states[i].getData(), states[i].getData(),
					states[i].getTimeStamp());
			statesmax[i] = new State(states[i].getName());
			statesmax[i].setData(states[i].getMaxBound(), states[i].getMaxBound(), states[i].getMaxBound(),
					states[i].getTimeStamp());
		}

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());

		// val
		StateSpaceModel modelval = model;
		modelval.setStates(statesval);
		DataTimeStamp<Double> resultval = cvUpperDataTimeStamp(state, requiredVal, targetState, model, scale,
				timestamp);

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());

		// max
		StateSpaceModel modelmax = model;
		modelmax.setStates(statesmax);
		DataTimeStamp<Double> resultmax = cvUpperDataTimeStamp(state, requiredVal, targetState, model, scale,
				timestamp);

		TimeState result = new TimeState("result");
		result.setMin(resultmin.getData(), resultmin.getTimestamp());
		result.setDataTimeStamp(resultval.getData(), resultval.getTimestamp());
		result.setMax(resultmax.getData(), resultmax.getTimestamp());
		result.setTimestamp(timestamp);

		oper.pastModelInfo(model);
		state.setData(model.getState(state.getName()).getDataRange());
		targetState.setData(model.getState(targetState.getName()).getDataRange());
		return result;
	}

	/**
	 * The method calculates the new state in a model depending on the time interval
	 * of the last update in the passed model.
	 * 
	 * @param model       is the state space model for the physical entity
	 * @param targetState is the state that the developer is interested in
	 * @param targetModel is the model that the developer is interested in
	 * @param timestamp   is the time stamp in interest
	 * @return the new state after the time interval from a last model update
	 */
	public static State matchIntervalImpact(StateSpaceModel model, State targetState, StateSpaceModel targetModel,
			double timestamp) {
		double period = cvTimeInterval(model, timestamp);
		return InaccuracyEvaluation.fstate(targetState, targetModel, period);
	}

	/*
	 * private
	 */

	/**
	 * The method calculates the difference between the passed time stamp and the last update of the model. 
	 * 
	 * @param model is the state space model for the physical entity
	 * @param timestamp is the time stamp in interest
	 * @return the time interval since the last update
	 */
	private static double cvTimeInterval(StateSpaceModel model, double timestamp) {
		return timestamp - model.getLastTimeInitiated();
	}
}