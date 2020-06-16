package cz.cuni.mff.d3s.jdeeco.uncertainty.external.example;

import cz.cuni.mff.d3s.jdeeco.uncertainty.external.KalmanFilter;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.Linear1dObservationModel;
import cz.cuni.mff.d3s.jdeeco.uncertainty.external.Linear1dProcessModel;

public class Linear1dModelTest {

	public static void main(String[] args) {
		
		Linear1dProcessModel model = new Linear1dProcessModel();
		Linear1dObservationModel obs = new Linear1dObservationModel();
		KalmanFilter filter = new KalmanFilter(model);

		for (int i = 0; i <= 100; i+=10) {
			double time = i;
			obs.setPosition(i);
			filter.update(time, obs);
			filter.estimate(obs);
			filter.predict(2);
		}

		double x = model.getState()[0][0];
		double v = model.getState()[1][0];

		System.out.println(x+"   "+v);
//		Assert.assertEquals(10, x, 1e-3);
//		Assert.assertEquals(1, v, 1e-3);
	}
}
