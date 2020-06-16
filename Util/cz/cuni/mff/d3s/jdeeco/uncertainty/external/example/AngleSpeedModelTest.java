package cz.cuni.mff.d3s.jdeeco.uncertainty.external.example;

import cz.cuni.mff.d3s.jdeeco.uncertainty.external.KalmanFilter;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.SpeedAngleObservationModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.SpeedAngleProcessModel;

public class AngleSpeedModelTest {

	public static void main(String[] args) {

		SpeedAngleProcessModel model = new SpeedAngleProcessModel();
		SpeedAngleObservationModel obs = new SpeedAngleObservationModel();
		KalmanFilter filter = new KalmanFilter(model);
		filter.setMaximalTimeStep(0.1);

		for (int i = 0; i <= 10; ++i) {
			double time = i;
			obs.setPosition(i, i);
			filter.update(time, obs);
		}

		double x = filter.model.state_estimate.data[0][0];
		double y = filter.model.state_estimate.data[1][0];
		double v = filter.model.state_estimate.data[2][0];
		double alpha = filter.model.state_estimate.data[3][0];

//		Assert.assertEquals(10, x, 1e-3);
//		Assert.assertEquals(10, y, 1e-3);
//		Assert.assertEquals(Math.sqrt(2), v, 1e-3);
//		Assert.assertEquals(Math.PI / 4, alpha, 1e-3);
	}

}
