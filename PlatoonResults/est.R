#/*******************************************************************************
#  * Copyright 2015 Charles University in Prague
#  *  
#  *  Licensed under the Apache License, Version 2.0 (the "License");
#  *  you may not use this file except in compliance with the License.
#  *  You may obtain a copy of the License at
#  *  
#  *      http://www.apache.org/licenses/LICENSE-2.0
#  *  
#  *  Unless required by applicable law or agreed to in writing, software
#  *  distributed under the License is distributed on an "AS IS" BASIS,
#  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  *  See the License for the specific language governing permissions and
#  *  limitations under the License.
#  *******************************************************************************/
#
# Author: Rima Al-ALi (alali@d3s.mff.cuni.cz)
#

library(ggplot2)

#Check the current directory 
#getwd()

#Add a new directory if needed 
#dir <- #e.g. 'D:/examples'
#setwd(dir)


#Read the data
guard <- read.csv(file="./est.csv",sep=",",head=TRUE)   

#Check the names of data columns 
#names(guard)

#Select the rows of "vehicle3"
v3 <- subset(guard, guard$vehicleID == "Vehicle3")

##############################################
##### plot speed and distance in vehicle3 ####
##############################################


#Plot the speed of the vehicle3 according to its position and plot the belief about the speed of vehicle2 in vehicle3 
ggplot(v3, aes(x = as.numeric(as.character(vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(refSpeedValue)), color = "Vehicle2", ymin = as.numeric(as.character(refSpeedMin)), ymax = as.numeric(as.character(refSpeedMax))),
              stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(vehSpeedValue)), color = "Vehicle3", ymin = as.numeric(as.character(vehSpeedMin)), ymax = as.numeric(as.character(vehSpeedMax))),
              stat="identity", fill = "red", alpha=0.2) +
  scale_color_manual("", breaks = c("Vehicle2", "Vehicle3"),
                     values = c("blue","red")) +
  theme(text = element_text(size=25),
        # Change legend background color
        legend.position = "bottom",
        legend.key = element_rect(colour = "transparent", fill = "white"),
        legend.justification = c(1,0))+ 
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  xlab("Position (meter)") + ylab("Speed (km/h)") +ylim(0,120) + xlim(0,7000) 


#Plot the distance of the vehicle3 according to its position and plot the belief about the distance of vehicle2 in vehicle3
ggplot(v3, aes(x = as.numeric(as.character(vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(distanceValue)), color = "Distance", ymin = as.numeric(as.character(distanceMin)), ymax = as.numeric(as.character(distanceMax))),
              stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(diffBrakingDistanceValue)), color = "DiffBrakingDistance", ymin = as.numeric(as.character(diffBrakingDistanceMin)), ymax = as.numeric(as.character(diffBrakingDistanceMax))),
              stat="identity", fill = "black", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(brakingDistanceValue)), color = "SelfBrakingDistance", ymin = as.numeric(as.character(brakingDistanceMin)), ymax = as.numeric(as.character(brakingDistanceMax))),
              stat="identity", fill = "red", alpha=0.2) +
  scale_color_manual("", breaks = c("DiffBrakingDistance", "Distance", "SelfBrakingDistance"),
                     values = c("black", "blue","red")) +
  xlab("Position (meter)") + ylab("Distance (meters)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(text = element_text(size=25),
        # Change legend background color
        legend.position = "bottom",
        legend.key = element_rect(colour = "transparent", fill = "white"),
        legend.justification = c(1,0)) +ylim(-150,150) + xlim(0,7000)



#################################
##### plot minimum distances ####
#################################

#Match the names to the enumaration 
v_name <- c('Vehicle1'=1, 'Vehicle2'=2,'Vehicle3'=3)

#Extract the minimum distance
distmin <- as.numeric(as.character(v3$distanceMin))
ids <- v3$vehicleID
lab <- 0
dist1<- cbind(ids,distmin,lab)
#Extract the minimum difference between braking distances of vehicle3 and vehicle2
distmin <- as.numeric(as.character(v3$diffBrakingDistanceMin))
ids <- v3$vehicleID
lab <- 1
dist2<- cbind(ids,distmin,lab)
#Extract the minimum braking distance for vehicle3
distmin <- as.numeric(as.character(v3$brakingDistanceMin))
ids <- v3$vehicleID
lab <- 2
dist3<- cbind(ids,distmin,lab)
#Add the minimum distances
distmins<- rbind(dist1, dist2, dist3)
#Create a data frame with the minimum distances
distmins <- data.frame(VehicleID = distmins[,1], Difference = distmins[,2], Label = distmins[,3])

#Display the id of the Vehicle on th plot 
var_list <- c('2' = "Vehicle2",'3' = "Vehicle3")
var_match <- function(x){
  vars <- as.numeric(as.character(x)) 
  cm <- vars
  var <- c("Vehicle2","Vehicle3")
  for (i in 1:length(vars)) {
    cm[i] <- var[x[i]-1]
  }
  return(cm)
}

#Plot the minimum distances
ggplot(data = distmins, aes(x=Label, y = Difference, group = Label))+ 
  geom_boxplot(aes(fill=var_match(VehicleID)))+                              #Vehicle3
  facet_wrap(vars(VehicleID), nrow = 2,labeller = as_labeller(var_list))+
  stat_summary(fun.y=mean, colour="darkred", geom="point", 
               shape=18, size=3,show.legend = FALSE) + coord_flip()+
  scale_fill_discrete(name = "VehicleID") +
  scale_x_continuous(breaks=c(0, 1, 2),
                   labels=c("Distance", "Difference in Braking Distance", "Braking Distance")) +
  theme(text = element_text(size=25),
        axis.title.y=element_blank(),
    legend.position = "bottom") + 
  ylim(-200,200) + ylab("meters") 
