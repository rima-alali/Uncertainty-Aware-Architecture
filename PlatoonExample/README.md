# Platoon (draft)
This folder contains examples of platoon with: base line, driving circumstances, and uncertainty awareness. The examples reflect the impact of the uncertainties on the runtime adaptation decision.

We start with a baseline evaluation that demonstrates how a vehicle adjusts its speed to maintain the distance from the vehicle in front without any uncertainty involved. In subsequent experiments, we add different kinds of uncertainties found during driving circumstances, which are related to communication, sensing and environment. After adding all the uncertainties in the scenario, we handled the uncertainties step by step and study the corresponding changes in adaptation decision evaluation. 

%CPS
We present a proof of concept for using the operators from the uncertainty-awareness APIs \footnote{https://github.com/rima-alali/Uncertainty-Aware-Architecture}. The scenario prototype is implemented in Java using the jDEECo framework, which follows the DEECo component model. The prototype contains components and ensembles with the use of Java annotations. The evaluation of the APIs is demonstrated in several steps; it starts with using the inaccuracy operators, then the statistical operators, and ends up using the learning operators. 


Base Line
----------------------------------------------------------
The example logs the speed and the distances between the vehicles. The example does not contain any kind of uncertainty (e.g. delays in processing or communication ... etc.). 


Driving Circumstances
----------------------------------------------------------
The example consider the communication between the vehicles and the processing time in each vehicle in the simulation. The vehicle uses sensor fusion to estimate the distance from the reference vehicle. Also, we added additional uncertainties in specific vehicle positions (i.e. scenario.PNG, scenario.xlsx), which are: losing GPS signals, additional WiFi delays, route slops, and changes in the leader vehicle driver behavior (i.e. speed).  


Uncertainty Awareness 
----------------------------------------------------------
The example consider evaluating different kind of adaptation conditions that consider delays, noise and violating operational boundaries. It is worth mentioning that in case of correlations, the performance of the operations are not optimized, which will add delays in the execution. However, we illustrate the use of the operators by processing the condition using outside execution file dropping the computation time of the operator itself for now.
