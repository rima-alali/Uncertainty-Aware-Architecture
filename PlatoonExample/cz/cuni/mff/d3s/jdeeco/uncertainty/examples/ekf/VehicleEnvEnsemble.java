package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;
import cz.cuni.mff.d3s.jdeeco.uncertainty.examples.basic.util.LookupTable;

@Ensemble
@PeriodicScheduling(period = 500)
public class VehicleEnvEnsemble {

	@Membership
	public static boolean membership(@In("coord.id") String id, 
			@In("coord.vehPos") State vehPos,
			@In("coord.vehSpeed") State vehSpeed, 
			@In("coord.vehAccel") State vehAccel, 
			@In("coord.vehGas") State vehGas,
			@In("coord.vehBrake") State vehBrake,
			@In("coord.vehTorques") LookupTable vehTorques,
			@In("coord.vehRouteSlopes") LookupTable vehRouteSlopes,
			@In("coord.vehMass") Double vehMass,
			
			@In("member.eName") String eName, 
			@In("member.eGas") State eGas,
			@In("member.eBrake") State eBrake,
			@In("member.ePos") State ePos, 
			@In("member.eSpeed") State eSpeed,
			@In("member.eAccel") State eAccel,
			@In("member.eTorques") LookupTable eTorques,
			@In("member.eRouteSlopes") LookupTable eRouteSlopes,
			@In("member.eMass") Double eMass
			) {
		if (id == eName)
			return true;
		return false;
	}

	@KnowledgeExchange
	public static void map(
			@In("coord.id") String id, 
			@In("coord.vehPos") State vehPos,
			@In("coord.vehSpeed") State vehSpeed, 
			@InOut("coord.vehAccel") ParamHolder<State> vehAccel,
			@In("coord.vehGas") State vehGas,
			@In("coord.vehBrake") State vehBrake,
			@In("coord.vehTorques") LookupTable vehTorques,
			@In("coord.vehRouteSlopes") LookupTable vehRouteSlopes,
			@In("coord.vehMass") Double vehMass,
			@InOut("coord.vehLastTime") ParamHolder<Double> vehLastTime,
			
			@InOut("member.ePos") ParamHolder<State> ePos, 
			@InOut("member.eSpeed") ParamHolder<State> eSpeed,
			@In("member.eAccel") State eAccel,
			@InOut("member.eGas") ParamHolder<State> eGas, 
			@InOut("member.eBrake") ParamHolder<State> eBrake,
			@InOut("member.eTorques") ParamHolder<LookupTable> eTorques,
			@InOut("member.eRouteSlopes") ParamHolder<LookupTable> eRouteSlopes,
			@InOut("member.eMass") ParamHolder<Double> eMass,
			@In("member.eLastTime") Double eLastTime) {
		ePos.value.setData(vehPos.getDataRange());
		eSpeed.value.setData(vehSpeed.getDataRange());
		eGas.value.setData(vehGas.getDataRange());
		eBrake.value.setData(vehBrake.getDataRange());
		eTorques.value = vehTorques;
		eRouteSlopes.value = vehRouteSlopes;
		eMass.value = vehMass;
		
		vehAccel.value.setData(eAccel.getDataRange());
		vehLastTime.value = eLastTime;
//		System.err.println(" ens ..."+vehAccel.value.getData());
		
	}
}