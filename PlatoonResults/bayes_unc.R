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
library(gridExtra)

#Check the current directory 
#getwd()

#Add a new directory if needed 
#dir <- #e.g. 'D:/examples'
#setwd(dir)


#Read the data
guard <- read.csv(file="./bayes_unc.csv",sep=",",head=TRUE)   
#Check the names of data columns 
#names(guard)

#Select the rows of "vehicle3"
v3 <- subset(guard, guard$vehicleID == "Vehicle3")

##############################################
##### add uncertainies details to plots ######
##############################################


#Set the size of the uncertianites font on plots
uncFontSize <- 5

#Define the text to disply the uncertianies on plots
addUncTextToPlot <- function(g,uncFontStart,uncFontSize){
  return(g +
           annotate("rect", xmin=c(1350), xmax=c(1450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 1350, y = uncFontStart, label = "R", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(1750), xmax=c(1900), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 1750, y = uncFontStart, label = "D", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(2200), xmax=c(2300), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 2200, y = uncFontStart, label = "G", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(2600), xmax=c(2700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 2600, y = uncFontStart, label = "W", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(3000), xmax=c(3050), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 3000, y = uncFontStart, label = "GW", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(3350), xmax=c(3400), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 3350, y = uncFontStart, label = "WR", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(3700), xmax=c(3750), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 3700, y = uncFontStart, label = "RD", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(4050), xmax=c(4100), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 4050, y = uncFontStart, label = "GD", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(4400), xmax=c(4450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 4400, y = uncFontStart, label = "WD", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(4750), xmax=c(4800), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 4750, y = uncFontStart, label = "GR", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(5100), xmax=c(5150), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 5100, y = uncFontStart, label = "GRD", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(5450), xmax=c(5500), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 5450, y = uncFontStart, label = "GWR", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(5800), xmax=c(5850), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 5800, y = uncFontStart, label = "WRD", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(6150), xmax=c(6200), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 6150, y = uncFontStart, label = "GWD", color = "grey20", size = uncFontSize, angle = 90) +
           annotate("rect", xmin=c(6500), xmax=c(6700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
           annotate("text", x = 6500, y = uncFontStart, label = "GWRD", color = "grey20", size = uncFontSize, angle = 90))
}



##############################################
##### plot speed and distance in vehicle3 ####
##############################################

#Set the start of the uncertianies text on plot on y axis
uncFontStart <- 1

#Plot the speed of the vehicle3 according to its position and plot the belief about the speed of vehicle2 in vehicle3 
addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
                   geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedValue)), color = "Vehicle2", ymin = as.numeric(as.character(v3$refSpeedMin)), ymax = as.numeric(as.character(v3$refSpeedMax))),
              stat="identity", fill = "blue", alpha=0.2) +
#  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedMin)), color = "line"), stat="identity", fill = "blue", alpha=0.2) +
#  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedMax)), color = "line"), stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$speedCACCValue)), color = "Vehicle3"),
              stat="identity", fill = "red", alpha=0.2) +
  scale_color_manual("", breaks = c("Vehicle2", "Vehicle3"),
                     values = c("blue","red","lightgray")) +
  theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0))+ 
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  xlab("Position (meter)") + ylab("Speed (km/h)") +ylim(0,120) + xlim(0,7000) , uncFontStart, uncFontSize) 

#Set the start of the uncertianies text on plot on y axis
uncFontStart <- -150

#Plot the distance of the vehicle3 according to its position and plot the belief about the distance of vehicle2 in vehicle3
addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$distanceCACCValue)), color = "Distance"),
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
    legend.justification = c(1,0)) +ylim(-150,150) + xlim(0,7000) , uncFontStart, uncFontSize)




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


#####################################
##### plot conditions evaluation ####
#####################################

#Set the font on the plot
plotFontSize <- 0.9
#Reset the size of the uncertianites font on plots
uncFontSize <- 7
#Set the start of the uncertianies text on plot on y axis
uncFontStart <- 1

#Plot the normal condition evaluation 
normalCondition <- addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
                                      ylim("","false","true") +
                                      theme(text = element_text(size=25))+
                                      geom_point(aes(y = tolower(v3$normalCondition)), color = "black", size=plotFontSize) +
                                      xlab("Vehicle3 Position (meter)") + ylab("normal")  + xlim(0,7000), uncFontStart, uncFontSize)

#Plot the condition evaluation which is based on minimum values 
minCondition <- addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
                                   ylim("","false","true") +
                                   theme(text = element_text(size=25))+
                                   geom_point(aes(y = tolower(v3$minCondition)), color = "black", size=plotFontSize) +
                                   xlab("Vehicle3 Position (meter)") + ylab("min")  + xlim(0,7000) , uncFontStart, uncFontSize)

#Plot the condition evaluation which is based on statistical testing 
statisticalCondition <- addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
                                           ylim("","false","true") +
                                           theme(text = element_text(size=25)) +
                                           geom_point(aes(y = tolower(v3$statisticalCondition)), color = "black", size=plotFontSize) +
                                           xlab("Vehicle3 Position (meter)") + ylab("lr") + xlim(0,7000) , uncFontStart, uncFontSize)

#Plot the condition evaluation which is based on statistical testing, and future prediction based on linear regression 
predictionCondition <- addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
                                          ylim("","false","true") +
                                          theme(text = element_text(size=25)) +
                                          geom_point(aes(y = tolower(v3$predictionCondition)), color = "black", size=plotFontSize) +
                                          xlab("Vehicle3 Position (meter)") + ylab("lr prediction")  + xlim(0,7000) , uncFontStart, uncFontSize)

#Plot the evaluations of all conditions in one grid
grid.arrange(normalCondition, minCondition, statisticalCondition, predictionCondition, ncol = 1, nrow = 4)


#####################################
########### the bayes part ##########
#####################################

#Set the start of the uncertianies text on plot on y axis
uncFontStart <- -3


#Plot the difference in ACC and CACC speed of the vehicle3 according to its position
addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$speedACCValue)) - as.numeric(as.character(v3$speedCACCValue)), color = "ACC-CACC"),
              stat="identity", fill = "blue", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC-CACC", "ACC"),
                     values = c("black", "blue")) +
  xlab("Position (meter)") + ylab("Speed (km/h)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000) , uncFontStart, uncFontSize)

#Set the start of the uncertianies text on plot on y axis
uncFontStart <- -1

#Plot the difference in ACC and CACC distance of the vehicle3 according to its position
addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$distanceACCValue)) * 10- as.numeric(as.character(v3$distanceCACCValue)) *10, color = "ACC-CACC"),
              stat="identity", fill = "blue", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC-CACC", "ACC"),
                     values = c("black", "blue")) +
  xlab("Position (meter)") + ylab("Distance (meters * 10)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000), uncFontStart, uncFontSize)

#Set the start of the uncertianies text on plot on y axis
uncFontStart <- -5

#Plot the difference in ACC and CACC accerlration/outputs of the vehicle3 according to its position
addUncTextToPlot(ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$outsCACCValue)) - as.numeric(as.character(v3$outsACCValue)), color = "ACC-CACC"),
              stat="identity", fill = "black", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC-CACC", "Distance", "SelfBrakingDistance"),
                     values = c("black", "blue","red")) +
  xlab("Position (meter)") + ylab("Distance (meters)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000), uncFontStart, uncFontSize)



#####################################
########### correlations ############
#####################################

#Read the correaltions data
guardcorr <- read.csv(file="./bayes_corr_unc.csv",sep=",",head=TRUE)   

#Get the vehicle3 data in mode CACC
v3corr1 <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3" & guardcorr$state == "CACC" )
#Get the vehicle3 data in mode ACC
v3corr2 <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3"& guardcorr$state == "ACC")
#Get the vehicle3 condition evaluation 
v3condition <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3" & guardcorr$state == "Condition")
#Get the vehicle3 condition with prediction evaluation 
v3conditionFuture <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3" & guardcorr$state == "ConditionFuture")


#Set the font on the plot
plotFontSize <- 0.9
#Reset the size of the uncertianites font on plots
uncFontSize <- 5
#Set the start of the uncertianies text on plot on y axis
uncFontStart <- -10

addUncTextToPlot(ggplot(v3corr2, aes(x = as.numeric(as.character(v3corr2$posValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3corr2$value))*1000000000 , color = "ACC"),
              stat="identity", fill = "black", alpha=0.2) +
  geom_line(aes(y = as.numeric(as.character(v3corr1$value))*1000000000, color = "CACC"),
              stat="identity", fill = "black", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC", "CACC", "SelfBrakingDistance"),
                     values = c("black", "blue","red")) +
  xlab("Position (meter)") + ylab("Correlation coefficient * 10e9") +  
  guides(color=guide_legend(override.aes=list(fill=NA)))+
  theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000) , uncFontStart, uncFontSize)



#######################################################
##### plot conditions evaluation with correlations ####
#######################################################

#Set the font on the plot
plotFontSize <- 0.9
#Reset the size of the uncertianites font on plots
uncFontSize <- 7
#Set the start of the uncertianies text on plot on y axis
uncFontStart <- 1

corrCondition <- addUncTextToPlot(ggplot(v3condition, aes(x = as.numeric(as.character(v3condition$posValue))))+
  ylim("","false","true") +
  geom_point(aes(y = tolower(v3condition$value)), color = "black", size=plotFontSize) +
  theme(text = element_text(size=25)) + 
  xlab("Vehicle3 Position (meter)") + ylab("corr") , uncFontStart, uncFontSize)


corrFutureCondition <- addUncTextToPlot(ggplot(v3conditionFuture, aes(x = as.numeric(as.character(v3conditionFuture$posValue))))+
  ylim("","false","true") +
  geom_point(aes(y = tolower(v3conditionFuture$value)), color = "black", size=plotFontSize) +
  theme(text = element_text(size=25))  + 
  xlab("Vehicle3 Position (meter)") + ylab("corr prediction"), uncFontStart, uncFontSize)

#Plot the evaluations of all conditions in one grid
grid.arrange(normalCondition, minCondition, statisticalCondition, predictionCondition,corrCondition, corrFutureCondition, ncol = 1, nrow = 6)

