package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf;

import java.io.IOException;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.modes.ModeSwitchingPlugin;

public class Execute {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException,
			AnnotationProcessorException, IOException {
		System.out.println("start");

		SimulationTimer simulationTimer = new DiscreteEventTimer();
		/* create main application container */
		DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
		simulation.addPlugin(new ModeSwitchingPlugin().withPeriod(2000));

		/* create nodes and components */
		DEECoNode deecoNode = simulation.createNode(1);

		/* deploy components */
		deecoNode.deployComponent(new Vehicle("Vehicle1", "", 0, 0, 0, 0));
		deecoNode.deployComponent(new Environment("E1","Vehicle1", 0, 0));
		deecoNode.deployComponent(new DistanceSensors("Vehicle1Sensors", "sensors", "Vehicle1", "", 0, 0, 0, 0));
		deecoNode.deployComponent(new Vehicle("Vehicle2", "Vehicle1", 0, 0, 0, 0));
		deecoNode.deployComponent(new Environment("E2","Vehicle2", 0, 0));
		deecoNode.deployComponent(new DistanceSensors("Vehicle2Sensors", "sensors", "Vehicle2", "Vehicle1", 0, 0, 0, 0));
		deecoNode.deployComponent(new Vehicle("Vehicle3", "Vehicle2", 0, 0, 0, 0));
		deecoNode.deployComponent(new Environment("E3","Vehicle3", 0, 0));
		deecoNode.deployComponent(new DistanceSensors("Vehicle3Sensors", "sensors", "Vehicle3", "Vehicle2", 0, 0, 0, 0));
		
		deecoNode.deployEnsemble(VehicleEnvEnsemble.class);
		deecoNode.deployEnsemble(V2VEnsemble.class);
		deecoNode.deployEnsemble(DistanceSensorsReferenceVehicleEnsemble.class);
		deecoNode.deployEnsemble(VehicleDistanceSensorEnsemble.class);
		
		/* start simulation */
		deecoNode.getRuntimeLogger().close();

		simulation.start(100000000);
	}

}