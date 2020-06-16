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
package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.inaccuracy.uncertainty;

import java.io.Serializable;

import cz.cuni.mff.d3s.jdeeco.uncertainty.external.MiniPID;

public class Controller implements Serializable{
	public static final long serialVersionUID = 750L;

	protected static final double MILLISEC_NANOSEC_FACTOR = 1000000;
	
	protected MiniPID pidPos;
	protected MiniPID pidSpeed;	
	protected Double error = 0.0;
	protected Double pidPosOutput = 0.0;
	protected Double pidSpeedOutput = 0.0;
	
	
	public Controller() {
		setPIDKnowledge();
	}	
	
	
	
	public double[] getActuators(double speed, double targetSpeed, double pos,  double targetpos) {
		double[] ac = new double[2];
		controllersOutput(speed, targetSpeed, pos, targetpos);
		double out = saturate();
		
		if (pidSpeedOutput >= 0) {
			ac[0] = out;
			ac[1] = 0.0;
		} else {
			ac[0] = 0.0;
			ac[1] = -out;
		}
		return ac;
	}
	
	public void controllersOutput(double actualSpeed, double targetSpeed, double actualPos, double targetPos) {
		pidPosOutput = pidPos.getOutput(actualPos, targetPos);
		pidSpeedOutput = pidSpeed.getOutput(actualSpeed - pidPosOutput, targetSpeed);
	}
	
	public Double getPidSpeedOutput() {
		return pidSpeedOutput;
	}
	
	public Double getPidPosOutput() {
		return pidPosOutput;
	}
	
	
	private double saturate() {
		double out = 0.0;
		if (pidSpeedOutput < -1) {
			out = -1;
		} else if (pidSpeedOutput > 1) {
			out = 1;
		} else {
			out = pidSpeedOutput;
		}
		return out;
	}
	
	/*
	 * private
	 */
	
	private void setPIDKnowledge() {
	
		pidPos = new MiniPID(0.04, 0.0, 0.0);
		pidSpeed = new MiniPID(0.04, 0.0, 0.0); //0.039
		
//		pidPos = new MiniPID(0.034, 0.0, 0.02);
//		pidSpeed = new MiniPID(0.023, 0.00002, 0.0);
	}
}

