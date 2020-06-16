package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.bayes;


import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.DataTimeStamp;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.models.VehicleStateSpaceModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.LookupTable;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.ScenarioDataset;

@Component
public class Environment {

	public String id;
	public String eName;

	public State ePos = new State("ePos",0.0);
	public State eSpeed = new State("eSpeed",0.0);
	public State eAccel = new State("eAccel",0.0);
	public State eGas = new State("eGas",0.0);      
	public State eBrake = new State("eBrake",0.0);    	
	public LookupTable eTorques = new LookupTable();
	public LookupTable eRouteSlopes = new LookupTable();
	public Double eMass = 1000.0;
	
	public VehicleStateSpaceModel eModel;
	public Double eLastTime = 0.0;

	public State ePosACC = new State("ePos",0.0);
	public State eSpeedACC = new State("eSpeed",0.0);
	public State eAccelACC = new State("eAccel",0.0);
	public State eGasACC = new State("eGas",0.0);      
	public State eBrakeACC = new State("eBrake",0.0);    	
	public VehicleStateSpaceModel eModelACC;

	
	protected static ScenarioDataset sc = new ScenarioDataset();
//	protected static ScenarioDatasetRouteDriver sc = new ScenarioDatasetRouteDriver();
	protected static final double MILLI_NANO_FACTOR = 1000000;
	protected static final double KM_SEC_FACTOR = 0.000278; //m per sec
	
	
	
	public Environment(String name, String lname, double pos, double speed) {
		id = name;
		eName = lname;
		double currentTime = System.nanoTime()/MILLI_NANO_FACTOR;
		ePos.setData(new DataTimeStamp<Double>(pos,currentTime));
		eSpeed.setData(new DataTimeStamp<>(speed,currentTime));
		eLastTime = currentTime;
		eModel = new VehicleStateSpaceModel(KM_SEC_FACTOR, 2, new LookupTable(), new LookupTable(), 0.0, currentTime, eSpeed, ePos);
		ePosACC.setData(new DataTimeStamp<Double>(pos,currentTime));
		eSpeedACC.setData(new DataTimeStamp<>(speed,currentTime));
		eModelACC = new VehicleStateSpaceModel(KM_SEC_FACTOR, 2, new LookupTable(), new LookupTable(), 0.0, currentTime, eSpeed, ePos);
	}	
	
	@Process
	@PeriodicScheduling(period = 1000)
	public static void environmentResponse(
			@In("eName") String eName,
			@In("ePos") State ePos,			 
			@In("eSpeed") State eSpeed,
			@InOut("eAccel") ParamHolder<State> eAccel,
			@In("eGas") State eGas,
			@In("eBrake") State eBrake,
			@In("eTorques") LookupTable eTorques,
			@In("eRouteSlopes") LookupTable eRouteSlopes,
			@In("eMass") Double eMass,
			@InOut("eModel") ParamHolder<VehicleStateSpaceModel> eModel,

			@In("ePosACC") State ePosACC,			 
			@In("eSpeedACC") State eSpeedACC,
			@InOut("eAccelACC") ParamHolder<State> eAccelACC,
			@In("eGasACC") State eGasACC,
			@In("eBrakeACC") State eBrakeACC,
			@InOut("eModelACC") ParamHolder<VehicleStateSpaceModel> eModelACC,
			@Out("eLastTime") ParamHolder<Double> eLastTime
			){
//	System.err.println(eName+"  "+eTorques+"  "+eRouteSlopes);
		double currentTime = System.nanoTime()/MILLI_NANO_FACTOR;
		// ----------------------- model ----------------------------------
		eModel.value.updateStates(eSpeed, ePos);
		eModel.value.setParams(eGas.getData(), eBrake.getData());
		eModel.value.updateModel(sc.getTorques(), sc.getRouteSlops(), sc.getMass());
		eModel.value.createBasicDataRange(currentTime);
		//------------------------ accel ----------------------------------
		eAccel.value.setData(eModel.value.getBasicDataRange());


		// ----------------------- model ----------------------------------
		eModelACC.value.updateStates(eSpeedACC, ePosACC);
		eModelACC.value.setParams(eGasACC.getData(), eBrakeACC.getData());
		eModelACC.value.updateModel(sc.getTorques(), sc.getRouteSlops(), sc.getMass());
		eModelACC.value.createBasicDataRange(currentTime);
		//------------------------ accel ----------------------------------
		eAccelACC.value.setData(eModelACC.value.getBasicDataRange());
		
		eLastTime.value = currentTime;
	}
}