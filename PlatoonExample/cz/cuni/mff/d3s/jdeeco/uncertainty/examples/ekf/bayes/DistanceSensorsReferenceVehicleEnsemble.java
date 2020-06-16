package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.bayes;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;

@Ensemble
@PeriodicScheduling(period = 500)
public class DistanceSensorsReferenceVehicleEnsemble {

	@Membership
	public static boolean membership(@In("coord.slid") String slid, 
			@In("coord.srefPos") State srefPos,
			@In("coord.srefSpeed") State srefSpeed,
			
			@In("member.id") String id, 
			@In("member.vehPos") State vehPos, 
			@In("member.vehSpeed") State vehSpeed) {
		if (id == slid)
			return true;
		return false;
	}

	@KnowledgeExchange
	public static void map(@In("coord.slid") String slid, 
			@InOut("coord.srefPos") ParamHolder<State> srefPos,
			@InOut("coord.srefSpeed") ParamHolder<State> srefSpeed,
			
			@In("member.id") String id, 
			@In("member.vehPos") State vehPos, 
			@In("member.vehSpeed") State vehSpeed) {

		srefPos.value.setData(vehPos.getDataRange());
		srefSpeed.value.setData(vehSpeed.getDataRange());
	}

}
