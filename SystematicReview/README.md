# Uncertainty-Aware Self-Adaptive Component Design in Cyber-Physical Systems (draft)

This guide is part of my thesis. It contains the data of the study in systematic_review_data.xlsx and the technical report in FullReportReview2019.pdf for the systematic review performed over EU projects. We added a representation of the results as a decision model using Bayesian network and influence diagram (i.e. using GeNIe 2.2).

Research Questions
---------------------------------------------------------------
- **RQ1:**	What are the typical domains for collective sCPS?
- **RQ2:**	What are the challenges addressed by collective sCPS?
- **RQ3:**	What is the existing information about uncertainty in each of the demonstrators?


Selection Critria
---------------------------------------------------------------
			
*Criteria 1	(Smart aspect)* 
- smart(*)
- intelligen(*)
- adapt(*)
- autonom(*)	
- aware(*)
   
*Criteria 2	(Collective aspect)* 
- distribut(*)
- de(-)central(*) 
- co(-)operat(*)
- communicat(*)
- collaborat(*)
- connective(*)
- emergent
- swarm
- collective
       
*Criteria 3  (Physical aspect)* 
- physical	
- CPS
- Internet of Things
- IoT
- embedded	
- device
- hardware
- robot	

*Criteria 4  (Entities)*
- \# > 2
 	 		    

	  	    	        
		    	
	   	    	
   	    	
	    	    
	            	    	
	            	        	
	            	            
	            		
(*) means zero or several letters (-) means it is possible to contain hyphen in the word.  #  means number of entities (i.e. physical nodes, human ..) that interact in use case			
			
			
			
Inclusion Criteria
------------------------------------------------------------------		
*Calls*			
- **IC1:**	The call was made in 2007�2016.		
- **IC2:**	The title of a call contains at least one keyword from each selection criteria 1, 2, and 3 (c.f. Table I).		
- **IC3:**	The description of a call contains at least one keyword from each of the selection criteria 1, 2, and 3.	
	
*Projects*			
- **IC4:**	The project title contains at least one keyword from any of the selection criteria 1, 2, or 3.		
- **IC5:**	The project description contains at least one keyword from each of the selection criteria 1, 2, and 3.		
- **IC6:**	The project has deliverables.	
	
*Use cases*			
- **IC7:**	Challenges in the use case target collective behavior.		
- **IC8:**	Use case has more than two interacting entities and at least 2 of them are nodes. The interaction represents the collective behavior (e.g., robots working together or interacting with humans). In this context, the entities could be nodes (e.g. robots) or humans.		
			
			
Exclusion Criteria
-------------------------------------------------------------------		
*Calls*			
- **EC1:**	(updated) Call started 2016-2017 since it is without results yet.		
- **EC2:**	Call description contains only keywords in context different from one of the three first criteria (e.g., collaboration between researchers).
		
*Projects*			
- **EC3:**	Project description contains only keywords in context different from one of the three first criteria (e.g., collaboration between researchers).		
- **EC4:**	(updated) Project does not have working website - last time checked on 8/2/2018.		
- **EC5:**	(updated) Project has no deliverables.		

*Use cases*			
- **EC6:**	Use case challenges are unrelated to collective behavior.		
- **EC7:**	(updated) Use cases challenges that do not target neither Application Layer nor Service Middleware Layer. For instance, use cases to exclude that represent network reconfiguration from switches level, or a collective knowledge exchange between people (e.g. social networks, software for rating places, roadmaps, and platforms).		
EC8	Use cases have one-to-one or one-to-many relation between nodes such as robots and humans (e.g., wearables, tour guide robot).		
			
			
Quality Assessment
---------------------------------------------------------------------	
- **QA1:**	Is a challenge found in the deliverables missing from the project abstract? 
		- evaluation determines which challenges to count in the selection. So, if the evaluation is "yes" then the challenge is added. Otherwise, the challenge could be a part of another challenge partially or totally. Then, the evaluation is "partially" or "no", and the challenge is disregarded	
- **QA2:**	Does the challenge in the deliverables require a collective behavior and involve uncertainty in self-adaptation decision?	
		- determines which challenge has uncertainty and requires collective behavior. So, if the evaluation is "no" or "partially" then disregard the challenge. The only evaluation to be considered is "yes", which has both uncertainty and collective behavior. Then, the challenge counts.	
