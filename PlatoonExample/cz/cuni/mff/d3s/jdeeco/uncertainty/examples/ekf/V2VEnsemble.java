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
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.StdRandom;

@Ensemble
@PeriodicScheduling(period = 500)
public class V2VEnsemble {

	private static double i = 500.0;
	private static double expRand = 700.0;

	@Membership
	public static boolean membership(@In("coord.lid") String lid, 
			@In("coord.refPos") State refPos,
			@In("coord.refSpeed") State refSpeed, 
			@In("coord.refAccel") State refAccel,
			@In("coord.refTorques") LookupTable refTorques,
			@In("coord.refRouteSlopes") LookupTable refRouteSlopes,
			@In("coord.refMass") Double refMass,
			@In("coord.refLastTime") Double refLastTime,

			@In("member.id") String id, 
			@In("member.vehPos") State vehPos, 
			@In("member.vehSpeed") State vehSpeed,
			@In("member.vehAccel") State vehAccel, 
			@In("member.vehTorques") LookupTable vehTorques,
			@In("member.vehRouteSlopes") LookupTable vehRouteSlopes, 
			@In("member.vehMass") Double vehMass,
			@In("member.vehLastTime") Double vehLastTime) {
		if (id == lid)
			return true;
		return false;
	}

	@KnowledgeExchange
	public static void map(@In("coord.lid") String lid, @InOut("coord.refPos") ParamHolder<State> refPos,
			@InOut("coord.refSpeed") ParamHolder<State> refSpeed, @InOut("coord.refAccel") ParamHolder<State> refAccel,
			@InOut("coord.refTorques") ParamHolder<LookupTable> refTorques,
			@InOut("coord.refRouteSlopes") ParamHolder<LookupTable> refRouteSlopes,
			@InOut("coord.refMass") ParamHolder<Double> refMass,
			@InOut("coord.refLastTime") ParamHolder<Double> refLastTime,

			@In("member.id") String id, @In("member.vehPos") State vehPos, @In("member.vehSpeed") State vehSpeed,
			@In("member.vehAccel") State vehAccel, @In("member.vehTorques") LookupTable vehTorques,
			@In("member.vehRouteSlopes") LookupTable vehRouteSlopes, @In("member.vehMass") Double vehMass,
			@In("member.vehLastTime") Double vehLastTime) {

			refPos.value.setData(vehPos.getDataRange());
			refSpeed.value.setData(vehSpeed.getDataRange());
			refAccel.value.setData(vehAccel.getDataRange());
			refTorques.value = vehTorques;
			refRouteSlopes.value = vehRouteSlopes;
			refMass.value = vehMass;
			refLastTime.value = vehLastTime;
	}

}
