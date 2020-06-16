package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf;

import java.util.ArrayList;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.annotations.ComponentModeChart;
import cz.cuni.mff.d3s.jdeeco.annotations.ExcludeModes;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataRange;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.History;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.TimeState;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyEvaluation;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.InaccuracyOperators;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.LookupTable;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.ScenarioDataset;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.example.StateEstimator;

@ComponentModeChart(VehicleModeChartHolder.class)
@Component
public class Vehicle extends VehicleRole {

//	protected static ScenarioDatasetRouteDriver sc = new ScenarioDatasetRouteDriver();
	protected static ScenarioDataset sc = new ScenarioDataset();

	protected static final double kpD = 0.193;
	protected static final double kp = 0.12631;
	protected static final double ki = 0.001;
	protected static final double kd = 0;
	protected static final double kt = 0.01;
	protected static final double MILLI_NANO_FACTOR = 1000000;
	protected static final double SCALE = 1.0;
	protected static final double KM_SEC_FACTOR = 0.000278; // m per sec

	public Vehicle(String name, String lid, double pos, double speed, double refpos, double refspeed) {
		id = name;
		this.name = name;
		this.lid = lid;

		canTransit = false;
		mode = VehicleModeEnum.CACC;
		oldMode = VehicleModeEnum.CACC;

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;
		offsetTime = currentTime;
		timeElapse = 0.0;

		setVehicleKnowledge(name, name, speed, pos, currentTime);
		setRefVehicleKnowledge(lid, refspeed, refpos, currentTime);
	}

	@Process
	@ExcludeModes({})
	@PeriodicScheduling(period = 500)
	public static void checkTransit(@In("mode") VehicleModeEnum inMode,
			@InOut("oldMode") ParamHolder<VehicleModeEnum> oldMode,
			@InOut("canTransit") ParamHolder<Boolean> canTransit) {

		if (canTransit.value) {
			// Wait until a transition is taken
			return;
		}

		switch (inMode) {
		case CACC:
			oldMode.value = VehicleModeEnum.CACC;
			canTransit.value = true;
			break;
		case ACC:
			oldMode.value = VehicleModeEnum.ACC;
			canTransit.value = true;
			break;
		default:
			break;
		}
	}

	@Process
	@PeriodicScheduling(period = 4000)
	public static void updateVehModel(@In("id") String id, @In("lid") String lid,
			@InOut("vehPos") ParamHolder<State> vehPos, @InOut("vehSpeed") ParamHolder<State> vehSpeed,
			@In("vehAccel") State vehAccel, @In("vehGas") State vehGas, @In("vehBrake") State vehBrake,
			@InOut("vehModel") ParamHolder<VehicleStateSpaceModel> vehModel, @In("vehTorques") LookupTable vehTorques,
			@InOut("vehRouteSlopes") ParamHolder<LookupTable> vehRouteSlopes, @In("vehMass") Double vehMass,

			@In("pidPosOutput") Double pidPosOutput, @In("pidSpeedOutput") Double pidSpeedOutput,
			@InOut("history") ParamHolder<History> history,

			@In("vehLastTime") Double vehLastTime, @InOut("vehLastTimeUpdate") ParamHolder<Double> vehLastTimeUpdate) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * =================== vehicle ===========================
		 */
//		System.err.println("before ... pos "+vehPos.value.toString()+"  speed "+vehSpeed.value.toString());
		vehModel.value.setParams(vehGas.getData(), vehBrake.getData());
//		vehRouteSlopes.value = sc.getRouteSlops(vehPos.value.getData(), vehPos.value.getData() + 10);
		vehModel.value.updateModel(vehTorques, vehRouteSlopes.value, vehMass);
		vehPos.value.addNumber(pidPosOutput);
		vehSpeed.value.addNumber(pidSpeedOutput);
		// ---------------------- model --------------------
		if (vehLastTime > vehLastTimeUpdate.value) {

			//GPS
			history.value.addOutput("CACC", vehAccel.getData(), currentTime);
			vehModel.value.setBasicDataRange(vehAccel.getDataRange());
			vehModel.value.initiateStates(vehLastTime, vehSpeed.value, vehPos.value);
			vehModel.value.setLastTimeInitiated(vehLastTime);

			// -------------------- states --------------------
			State[] states = InaccuracyEvaluation.fstates(vehModel.value, 0, SCALE, currentTime);
			vehSpeed.value.setData(vehModel.value.refineSpeed(states[0].getDataRange()));
			vehPos.value.setData(vehModel.value.refinePos(states[1].getDataRange()));
			vehModel.value.updateStates(vehSpeed.value, vehPos.value);
		} else {
			// Dead reckoning ... use the last value
			// -------------------- states --------------------
			State[] states = InaccuracyEvaluation.fstates(vehModel.value, 0, SCALE, currentTime);
			vehSpeed.value.setData(vehModel.value.refineSpeed(states[0].getDataRange()));
			vehPos.value.setData(vehModel.value.refinePos(states[1].getDataRange()));
			vehModel.value.updateStates(vehSpeed.value, vehPos.value);
		}
		vehLastTimeUpdate.value = currentTime;
//		vehRouteSlopes.value = sc.getRouteSlops(vehPos.value, vehRouteSlopes.value, 1000, 1000);
	}

	@Process
	@PeriodicScheduling(period = 4000)
	public static void updateRefModel(@In("id") String id, @In("lid") String lid,
			@InOut("refModel") ParamHolder<VehicleStateSpaceModel> refModel, @In("refPos") State refPos,
			@In("refSpeed") State refSpeed, @In("refAccel") State refAccel, @In("refGas") State refGas,
			@In("refBrake") State refBrake, @In("refTorques") LookupTable refTorques,
			@In("refRouteSlopes") LookupTable refRouteSlopes, @In("refMass") Double refMass,

			@In("refLastTime") Double refLastTime, @InOut("refLastTimeUpdate") ParamHolder<Double> refLastTimeUpdate,

			@InOut("vehrefPos") ParamHolder<State> vehrefPos, @InOut("vehrefSpeed") ParamHolder<State> vehrefSpeed) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;
		/*
		 * ================= reference vehicle =================
		 */
		if (lid != "") {
			refModel.value.setParams(refGas.getData(), refBrake.getData());
			refModel.value.updateModel(refTorques, refRouteSlopes, refMass);

			// -------------------- model ---------------------
			if (refLastTime > refLastTimeUpdate.value) {
				// New
				refModel.value.setBasicDataRange(refAccel.getDataRange());
				refModel.value.updateStates(refSpeed, refPos);
				refModel.value.setLastTimeInitiated(refLastTime);
				// -------------------- states --------------------
				State[] refstates = InaccuracyEvaluation.fstates(refModel.value, 0, SCALE, currentTime);
				vehrefSpeed.value.setData(refModel.value.refineSpeed(refstates[0].getDataRange()));
				vehrefPos.value.setData(refModel.value.refinePos(refstates[1].getDataRange()));
				refModel.value.updateStatesBoundaries(vehrefSpeed.value, vehrefPos.value);
			} else {
				// -------------------- states --------------------
				State[] refstates = InaccuracyEvaluation.fstates(refModel.value, 0, SCALE, currentTime);
				vehrefSpeed.value.updateBounds(refModel.value.refineSpeed(refstates[0].getDataRange()));
				vehrefPos.value.updateBounds(refModel.value.refinePos(refstates[1].getDataRange()));
				refModel.value.updateStatesBoundaries(vehrefSpeed.value, vehrefPos.value);
			}
			refLastTimeUpdate.value = currentTime;
		}
	}

	@Process
	@PeriodicScheduling(period = 4000)
	public static void calculateWiFiDistances(@In("id") String id, @In("lid") String lid,
			@In("vehPos") State vehPos,
			@In("vehSpeed") State vehSpeed, @In("vehModel") VehicleStateSpaceModel vehModel,
			@In("refLength") Double refLength,
			@In("refModel") VehicleStateSpaceModel refModel, @In("vehrefPos") State vehrefPos,
			@In("vehrefSpeed") State vehrefSpeed, @In("refPos") State refPos, 
			@In("refSpeed") State refSpeed,
			@InOut("history") ParamHolder<History> history,
			@InOut("distance") ParamHolder<State> sdistance,
			@InOut("brakingDistance") ParamHolder<State> sbrakingDistance,
			@InOut("diffbrakingDistance") ParamHolder<State> diffbrakingDistance) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * ================= distances =================
		 */

		if (lid != "") {
			State distance = new State("distance", 0.0);
			distance = InaccuracyOperators.minus(refPos, vehPos, currentTime);
			distance.addNumber(-refLength);
			TimeState brakingPosition = InaccuracyEvaluation.cvLowerState(vehSpeed, 0.0, vehPos, vehModel, 1.0, currentTime);
			brakingPosition.addNumber(-vehPos.getData());
			TimeState diffbraking = InaccuracyOperators.minuscvLowersState(refSpeed, 0.0, refPos, refModel, vehSpeed, 0.0,
					vehPos, vehModel, 1.0, currentTime);
			diffbraking.addNumber(-refLength);

			
//			sdistance.value.setData(distance.getDataRange());
			diffbrakingDistance.value.setData(diffbraking.getState().getDataRange());
			sbrakingDistance.value.setData(brakingPosition.getState().getDataRange());
//			System.err.println(distance.getData() + "  " + sbrakingDistance.value.getData() + "  "
//					+ diffbrakingDistance.value.getData());
			
			history.value.addSoftThreshold("CACC", diffbrakingDistance.value.getData(), currentTime);
			history.value.addSoftThresholdState("CACC", diffbrakingDistance.value, currentTime);
			history.value.addInput("wifiDistance", refPos.getData() - vehPos.getData(), currentTime);
			history.value.addInput("wifiSpeed", vehrefSpeed.getData(), vehrefSpeed.getTimeStamp());
		}
	}


	@Process
	@PeriodicScheduling(period = 4000)
	public static void calculateTarget(@In("id") String id, @In("lid") String lid, @In("vehPos") State vehPos,
			@In("vehSpeed") State vehSpeed, @In("vehModel") VehicleStateSpaceModel vehModel,
			@In("refModel") VehicleStateSpaceModel refModel, @In("refPos") State refPos, @In("refSpeed") State refSpeed,
			@In("refLength") Double refLength,

			@InOut("actualPos") ParamHolder<Double> actualPos, @InOut("actualSpeed") ParamHolder<Double> actualSpeed,
			@InOut("targetPos") ParamHolder<Double> targetPos, @InOut("targetSpeed") ParamHolder<Double> targetSpeed,
			@In("vehDiseredDistance") Double vehDiseredDistance, @In("vehDiseredSpeed") Double vehDiseredSpeed,
			@InOut("history") ParamHolder<History> history,
			@InOut("pidPosOutput") ParamHolder<Double> pidPosOutput, @InOut("pidSpeedOutput") ParamHolder<Double> pidSpeedOutput,
			@InOut("est") ParamHolder<StateEstimator> est,
			@InOut("distance") ParamHolder<State> sdistance,
			@InOut("speed") ParamHolder<State> sspeed,
			@In("diffbrakingDistance") State diffbrakingDistance) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * ================= estimate ==========================
		 */

		double timestep = 0;
		double distance = 0;
		double lasttime = 0;
		if (history.value.getInputMerge("CACC", "Distance").size() > 1) {
			distance = history.value.getInputMerge("CACC", "Distance").lastEntry().getValue();
			lasttime = history.value.getInputMerge("CACC", "Distance").lastKey();
			double previous = history.value.getInputMerge("CACC", "Distance").lowerKey(lasttime);
			timestep = lasttime - previous;
		}
		double speed = 0;
		if (history.value.getInputMerge("CACC", "Speed").size() > 1) {
			speed = history.value.getInputMerge("CACC", "Speed").lowerEntry(lasttime).getValue();
		}
		est.value.addValue(distance, speed, timestep);
//		System.err.println(id + "  " + distance + "  " + Arrays.toString(est.value.getPosition()) + "  " + speed + "  "
//				+ Arrays.toString(est.value.getVelocity()));
		distance = est.value.getPosition()[0];
		speed = est.value.getVelocity()[0];
//		System.err.println(id+"   "+distance+"   "+speed);
		
		sdistance.value.setData(new DataRange(new DataTimeStamp<Double>(distance, currentTime)));
		sspeed.value.setData(new DataRange(new DataTimeStamp<Double>(speed, currentTime)));

		
		/*
		 * ================= actual & target =================
		 */

		double finalDesiredDistance = vehDiseredDistance;
//		if(diffbrakingDistance.getData() < vehDiseredDistance)
//			finalDesiredDistance = Math.abs(vehDiseredDistance - diffbrakingDistance.getData()) + vehDiseredDistance;
		
		if (lid == "") {
			actualPos.value = vehPos.getData();
			actualSpeed.value = vehSpeed.getData();
			targetPos.value = vehPos.getData();
			targetSpeed.value = vehDiseredSpeed;
		} else {
			actualPos.value = vehPos.getData();
			actualSpeed.value = vehSpeed.getData();
			targetPos.value = vehPos.getData() + distance - finalDesiredDistance; //	- posReaction.getInterval();
			targetSpeed.value = speed; // vehrefSpeed.value.getMinBound() - speedReaction.getInterval();

			if ((targetPos.value - vehPos.getData()) < 0 && vehPos.getData() == 0.0) {
				actualPos.value = vehPos.getData();
				actualSpeed.value = vehSpeed.getData();
				targetPos.value = vehPos.getData();
				targetSpeed.value = vehSpeed.getData();
			}
		}
		
//		System.err.println("final "+finalDesiredDistance+"  "+(targetPos.value - actualPos.value));
		
	}

	@Process
	@PeriodicScheduling(period = 4000)
	public static void speedController(@In("id") String id, @In("lid") String lid, @In("vehPos") State vehPos,
			@In("vehSpeed") State vehSpeed, @InOut("vehGas") ParamHolder<State> vehGas,
			@InOut("vehBrake") ParamHolder<State> vehBrake,

			@In("vehrefPos") State vehrefPos, @In("vehrefSpeed") State vehrefSpeed,

			@In("actualPos") Double actualPos, @In("actualSpeed") Double actualSpeed, @In("targetPos") Double targetPos,
			@In("targetSpeed") Double targetSpeed,

			@InOut("controller") ParamHolder<Controller> controller,
			@InOut("pidPosOutput") ParamHolder<Double> pidPosOutput,
			@InOut("pidSpeedOutput") ParamHolder<Double> pidSpeedOutput,

			@InOut("history") ParamHolder<History> history, @In("offsetTime") Double offsetTime,
			@InOut("timeElapse") ParamHolder<Double> timeElapse) {

		double currentTime = System.nanoTime() / MILLI_NANO_FACTOR;

		/*
		 * ================= actual & target =================
		 */
		double[] out = controller.value.getActuators(actualSpeed, targetSpeed, actualPos, targetPos);
		vehGas.value.setData(new DataTimeStamp<Double>(out[0], currentTime));
		vehBrake.value.setData(new DataTimeStamp<Double>(out[1], currentTime));
		pidPosOutput.value = controller.value.getPidPosOutput();
		pidSpeedOutput.value = controller.value.getPidSpeedOutput();
		timeElapse.value = currentTime - offsetTime;
		printInfo(id + " CACC ", lid, vehPos, vehSpeed, vehrefPos, vehrefSpeed, targetPos, actualPos, timeElapse.value);
	}

	/*
	 * private
	 */

	private void setVehicleKnowledge(String id, String name, double speed, double pos, double currentTime) {
		vehPos.setData(new DataTimeStamp<Double>(pos, currentTime));
		vehSpeed.setData(new DataTimeStamp<Double>(speed, currentTime));
		vehTorques = sc.getTorques();
		vehRouteSlopes = sc.getRouteSlops();
		vehMass = sc.getMass();
		vehModel = new VehicleStateSpaceModel(KM_SEC_FACTOR, 2, vehTorques, vehRouteSlopes, vehMass, currentTime,
				vehSpeed, vehPos);
		vehModel.setParams(vehGas.getData(), vehBrake.getData());
		est = new StateEstimator(vehModel);
		controller = new Controller();
		targetPos = 0.0;
		targetSpeed = 0.0;

		setInputs();

		vehLastTime = currentTime;
		vehLastTimeUpdate = currentTime;
	}

	private void setRefVehicleKnowledge(String lid, double refspeed, double refpos, double currentTime) {
		refPos.setData(new DataTimeStamp<Double>(refpos, currentTime));
		refSpeed.setData(new DataTimeStamp<Double>(refspeed, currentTime));
		refModel = new VehicleStateSpaceModel(KM_SEC_FACTOR, 2, refTorques, refRouteSlopes, refMass, currentTime,
				vehrefSpeed, vehrefPos);
		refModel.setParams(refGas.getData(), refBrake.getData());
		refLastTime = currentTime;
		refLastTimeUpdate = currentTime;
	}

	private static void printInfo(String id, String lid, State vehPos, State vehSpeed, State vehrefPos,
			State vehrefSpeed, Double targetPos, Double actualPos, Double timeElapse) {
		String str = id + " _  pos: " + vehPos.getData() + "  speed : " + vehSpeed.getData();
		if (lid != "")
			str += "  >>>  refpos: " + vehrefPos.getData() + "   refspeed : " + vehrefSpeed.getData()
					+ "  diff with target = " + (targetPos - actualPos) + "   time elapse : " + timeElapse;
		System.out.println(str);
	}

	private void setInputs() {
//		history.setInputs(new String[] { "wifiDistance", "RadarDistance", "LidarDistance", "CameraDistance", "wifiSpeed", "RadarSpeed", "LidarSpeed", "CameraSpeed" });
		HashMap<String, ArrayList<String>> shards = new HashMap<>();
		ArrayList<String> shardCACC = new ArrayList<>();
		shardCACC.add("wifiDistance");
		shardCACC.add("RadarDistance");
		shardCACC.add("LidarDistance");
		shardCACC.add("CameraDistance");
		shardCACC.add("wifiSpeed");
		shardCACC.add("RadarSpeed");
		shardCACC.add("LidarSpeed");
		shardCACC.add("CameraSpeed");
		shards.put("CACC", shardCACC);
		ArrayList<String> shardACC = new ArrayList<>();
		shardACC.add("RadarDistance");
		shardACC.add("LidarDistance");
		shardACC.add("CameraDistance");
		shardACC.add("RadarSpeed");
		shardACC.add("LidarSpeed");
		shardACC.add("CameraSpeed");
		shards.put("ACC", shardACC);
		history.setSituations(shards);
		history.setHardThreshold("CACC", 5.0);
		history.setHardThreshold("ACC", 7.0);
		
		ArrayList<String> shardDelays = new ArrayList<>();
		shardDelays.add("wifiDistance");
		shardDelays.add("wifiBrakingDistance");
		history.setInputsDelays(shardDelays);
	}

}