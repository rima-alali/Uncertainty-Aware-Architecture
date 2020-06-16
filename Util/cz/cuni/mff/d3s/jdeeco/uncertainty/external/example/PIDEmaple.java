package cz.cuni.mff.d3s.jdeeco.uncertainty.external.example;

import cz.cuni.mff.d3s.jdeeco.uncertainty.external.MiniPID;

public class PIDEmaple {

	public static void main(String[] args) {
//		MiniPID pidPos = new MiniPID(0.4, 0.5 , 0.001);//0.15 0.045 0.0125
//		pidPos.setOutputLimits(1);
//		MiniPID pidSpeed = new MiniPID(0.1, 0, 0);
//		pidSpeed.setOutputLimits(1.0);
		
		MiniPID miniPID; 
		
		miniPID = new MiniPID(0.25, 0.01, 0.4);
		miniPID.setOutputLimits(10);
		//miniPID.setMaxIOutput(2);
		//miniPID.setOutputRampRate(3);
		//miniPID.setOutputFilter(.3);
		miniPID.setSetpointRange(40);

		double target=100;
		
		double actual=0;
		double output=0;
		
		miniPID.setSetpoint(0);
		miniPID.setSetpoint(target);
		
		System.err.printf("Target\tActual\tOutput\tError\n");
		//System.err.printf("Output\tP\tI\tD\n");

		// Position based test code
		for (int i = 0; i < 100; i++){
			
			//if(i==50)miniPID.setI(.05);
			
			if (i == 60)
				target = 50;
				
			//if(i==75)target=(100);
			//if(i>50 && i%4==0)target=target+(Math.random()-.5)*50;
			
			output = miniPID.getOutput(actual, target);
			actual = actual + output;
			
			//System.out.println("=========================="); 
			//System.out.printf("Current: %3.2f , Actual: %3.2f, Error: %3.2f\n",actual, output, (target-actual));
			System.err.printf("%3.2f\t%3.2f\t%3.2f\t%3.2f\n", target, actual, output, (target-actual));
			
			//if(i>80 && i%5==0)actual+=(Math.random()-.5)*20;
	}
	}
}

//double posO = controller.value.controllerPos(vehSpeed.value.getMaxBound(), vehrefSpeed.getMinBound(), distance.getMinBound(),
//safetyDistance + reactionDistance + refLength);
//vehPos.value.setData(new DataTimeStamp<Double>(vehPos.value.getData()+posO, currentTime));
//double speedO = controller.value.controllerSpeed(vehSpeed.value.getMaxBound(), vehrefSpeed.getMinBound(), distance.getMinBound(),
//safetyDistance + reactionDistance + refLength, posO);
//vehSpeed.value.setData(new DataTimeStamp<Double>(vehSpeed.value.getData()+speedO, currentTime));
