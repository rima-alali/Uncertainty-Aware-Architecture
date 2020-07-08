# Platoon (draft)
This folder contains examples of platoon with: base line, driving circumstances, and uncertainty awareness. The examples reflect the impact of the uncertainties on the runtime adaptation decision.

Base Line
----------------------------------------------------------
The example logs the speed and the distances between the vehicles. The example does not contain any kind of uncertainty (e.g. delays in processing or communication ... etc.). 


Driving Circumstances
----------------------------------------------------------
The example consider the communication between the vehicles and the processing time in each vehicle in the simulation. The vehicle uses sensor fusion to estimate the distance from the reference vehicle. Also, we added additional uncertainties in specific vehicle positions (i.e. scenario.PNG, scenario.xlsx), which are: losing GPS signals, additional WiFi delays, route slops, and changes in the leader vehicle driver behavior (i.e. speed).  


Uncertainty Awareness 
----------------------------------------------------------
The example consider evaluating different kind of adaptation conditions that consider delays, noise and violating operational boundaries. It is worth mentioning that in case of correlations, the performance of the operations are not optimized, which will add delays in the execution. However, we illustrate the use of the operators by processing the condition using outside execution file dropping the computation time of the operator itself for now.
