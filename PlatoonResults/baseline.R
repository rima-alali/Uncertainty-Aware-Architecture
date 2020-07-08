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

library(ggplot2)

setwd('C:/Users/rima/Desktop/R_thesis_results')

guard <- read.csv(file="./baseline.csv",sep=",",head=TRUE)   
names(guard)

v3 <- subset(guard, guard$vehicleID == "Vehicle3")


#speed
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedValue)), color = "Vehicle2", ymin = as.numeric(as.character(v3$refSpeedMin)), ymax = as.numeric(as.character(v3$refSpeedMax))),
              stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$vehSpeedValue)), color = "Vehicle3", ymin = as.numeric(as.character(v3$vehSpeedMin)), ymax = as.numeric(as.character(v3$vehSpeedMax))),
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
  

#distance
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$distanceValue)), color = "Distance", ymin = as.numeric(as.character(v3$distanceMin)), ymax = as.numeric(as.character(v3$distanceMax))),
              stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$diffBrakingDistanceValue)), color = "DiffBrakingDistance", ymin = as.numeric(as.character(v3$diffBrakingDistanceMin)), ymax = as.numeric(as.character(v3$diffBrakingDistanceMax))),
              stat="identity", fill = "black", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$brakingDistanceValue)), color = "SelfBrakingDistance", ymin = as.numeric(as.character(v3$brakingDistanceMin)), ymax = as.numeric(as.character(v3$brakingDistanceMax))),
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


#difference
data_summary <- function(x) {
  m <- mean(x)
  ymin <- m-sd(x)
  ymax <- m+sd(x)
  return(c(y=m,ymin=ymin,ymax=ymax))
}

v_name <- c('Vehicle1'=1, 'Vehicle2'=2,'Vehicle3'=3)


diff <- as.numeric(as.character(v3$distanceMin))
ids <- v3$vehicleID
lab <- 0
minus1<- cbind(ids,diff,lab)

diff <- as.numeric(as.character(v3$diffBrakingDistanceMin))
ids <- v3$vehicleID
lab <- 1
minus3<- cbind(ids,diff,lab)


diff <- as.numeric(as.character(v3$brakingDistanceMin))
ids <- v3$vehicleID
lab <- 2
minus5<- cbind(ids,diff,lab)

minus<- rbind(minus1, minus3, minus5)

minus <- data.frame(VehicleID = minus[,1], Difference = minus[,2], Label = minus[,3])


fun_mean <- function(x){
  return(data.frame(y=mean(x),label=mean(x,na.rm=T)))}




ggplot(data = minus, aes(x=Label, y = Difference, group = Label))+ 
  geom_boxplot(aes(fill="Vehicle3"))+                              #Vehicle3
  facet_wrap(vars("Vehicle3"), nrow = 2)+
  stat_summary(fun.y=mean, colour="darkred", geom="point", 
               shape=18, size=3,show.legend = FALSE) + coord_flip()+
  scale_fill_discrete(name = "VehicleID") +
  scale_x_continuous(breaks=c(0, 1, 2),
                     labels=c("Distance", "Difference in Braking Distance", "Braking Distance")) +
  theme(text = element_text(size=25),
        axis.title.y=element_blank(),
        legend.position = "bottom") + 
  ylim(-200,200) + ylab("meters") 

