package cz.cuni.mff.d3s.jdeeco.uncertainty.examples.ekf.inaccuracy;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.data.History;
import cz.cuni.mff.d3s.jdeeco.uncertainty.api.inaccuracy.State;

@Ensemble
@PeriodicScheduling(period = 500)
public class VehicleDistanceSensorEnsemble {

	@Membership
	public static boolean membership(@In("coord.sid") String sid, 
			@In("coord.slid") String slid, 
			@In("coord.svehPos") State svehPos,
			@In("coord.svehSpeed") State svehSpeed,
			@In("coord.radarDistance") State radarDistance, 
			@In("coord.lidarDistance") State lidarDistance,
			@In("coord.cameraDistance") State cameraDistance, 
			@In("coord.radarsrefSpeed") State radarsrefSpeed,
			@In("coord.lidarsrefSpeed") State lidarsrefSpeed,
			@In("coord.camerasrefSpeed") State camerasrefSpeed,

			@In("member.id") String id,
			@In("member.vehPos") State vehPos, 
			@In("member.vehSpeed") State vehSpeed,
			@In("member.history") History history) {
		if (id == sid)
			return true;
		return false;
	}

	@KnowledgeExchange
	public static void map(@In("coord.sid") String sid, 
			@In("coord.slid") String slid, 
			@InOut("coord.svehPos") ParamHolder<State> svehPos,
			@InOut("coord.svehSpeed") ParamHolder<State> svehSpeed,
			@In("coord.radarDistance") State radarDistance, 
			@In("coord.lidarDistance") State lidarDistance,
			@In("coord.cameraDistance") State cameraDistance, 
			@In("coord.radarsrefSpeed") State radarsrefSpeed,
			@In("coord.lidarsrefSpeed") State lidarsrefSpeed,
			@In("coord.camerasrefSpeed") State camerasrefSpeed,

			@In("member.id") String id,
			@In("member.vehPos") State vehPos, 
			@In("member.vehSpeed") State vehSpeed,
			@InOut("member.history") ParamHolder<History> history) {

		svehPos.value.setData(vehPos.getDataRange());
		svehSpeed.value.setData(vehSpeed.getDataRange());

		history.value.addInput("RadarSpeed", radarsrefSpeed.getData(), radarsrefSpeed.getTimeStamp());
		history.value.addInput("LidarSpeed", lidarsrefSpeed.getData(), lidarsrefSpeed.getTimeStamp());
		history.value.addInput("CameraSpeed", camerasrefSpeed.getData(), camerasrefSpeed.getTimeStamp());

		history.value.addInput("RadarDistance", radarDistance.getData(), radarDistance.getTimeStamp());
		history.value.addInput("LidarDistance", lidarDistance.getData(), lidarDistance.getTimeStamp());
		history.value.addInput("CameraDistance", cameraDistance.getData(), cameraDistance.getTimeStamp());

//		System.err.println("ens .... " + radarDistance.toString()+"  "+lidarvehrefSpeed.toString());
	}
}
