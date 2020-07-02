library(ggplot2)
library(tidyverse)
library(gridExtra)

setwd('C:/Users/rima/Desktop/R_thesis_results')

guard <- read.csv(file="./bayes.csv",sep=",",head=TRUE)   
names(guard)

v1 <- subset(guard, guard$vehicleID == "Vehicle1")
v2 <- subset(guard, guard$vehicleID == "Vehicle2")
v3 <- subset(guard, guard$vehicleID == "Vehicle3")

v1 <- v1[-1:-1,]
v2 <- v2[-1:-1,]
#v3 <- v3[-1:-1,]


#speed v1 & v2 & v3
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v2$vehSpeedValue)), color = "Vehicle2", ymin = as.numeric(as.character(v2$vehSpeedMin)), ymax = as.numeric(as.character(v2$vehSpeedMax))),
              stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedValue)), color = "Vehicle2", ymin = as.numeric(as.character(v3$refSpeedMin)), ymax = as.numeric(as.character(v3$refSpeedMax))),
              stat="identity", fill = "blue", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$vehSpeedValue)), color = "Vehicle3", ymin = as.numeric(as.character(v3$vehSpeedMin)), ymax = as.numeric(as.character(v3$vehSpeedMax))),
              stat="identity", fill = "red", alpha=0.2) +
  scale_color_manual("", breaks = c("Vehicle2", "Vehicle3"),
                     values = c("blue","red")) +
  theme(
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0))+ 
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  xlab("Position (meter)") + ylab("Speed (km/h)") +ylim(0,120) + xlim(0,7000)
  

#distance v1 & v2 & v3
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
theme(
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) +ylim(-150,250) + xlim(0,7000) 


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


var_list <- c('2' = "Vehicle2",'3' = "Vehicle3")


color_match <- function(x){
  colors <- as.numeric(as.character(x)) 
  cm <- colors
  var_color <- c("Vehicle2","Vehicle3")
  for (i in 1:length(colors)) {
    cm[i] <- var_color[x[i]-1]
  }
  return(cm)
}


ggplot(data = minus, aes(x=Label, y = Difference, group = Label))+ 
  geom_boxplot(aes(fill=color_match(minus$VehicleID)))+                              #Vehicle3
  facet_wrap(vars(VehicleID), nrow = 2,labeller = as_labeller(var_list))+
  stat_summary(fun.y=mean, colour="darkred", geom="point", 
               shape=18, size=3,show.legend = FALSE) + coord_flip()+
  scale_fill_discrete(name = "VehicleID") +
  scale_x_continuous(breaks=c(0, 1, 2),
                   labels=c("Distance", "Difference in Braking Distance", "Braking Distance")) +
  theme(axis.title.y=element_blank(),
    legend.position = "bottom") + 
  ylim(-200,200) + ylab("meters") 



cond_elem <- function(x,y,z){
  cond <- FALSE
  if(x < z || y <= 0)
    cond <- TRUE
  return(cond)
}

 
cond_match <- function(x,y,z){
  cm <- x
  for (i in 1:length(x)) {
    cm[i] <- cond_elem[x[i],y[i],z]
  }
  return(cm)
}


ps <- 0.9
s<- 4


dp <- ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  ylim("","false","true") +
  geom_point(aes(y = v3$normalCondition), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("normal")  + xlim(0,7000)


dp1 <- ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  ylim("","false","true") +
  geom_point(aes(y = v3$minCondition), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("min")  + xlim(0,7000) 


dp2 <- ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  ylim("","false","true") +
  geom_point(aes(y = v3$statisticalCondition), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("lr") + xlim(0,7000) 


dp3 <- ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  ylim("","false","true") +
  geom_point(aes(y = v3$predictionCondition), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("lr prediction")  + xlim(0,7000) 


grid.arrange(dp, dp1, dp2, dp3, ncol = 1, nrow = 4)



# ------------------ the bayes part ------------------------------

#speed
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$speedACCValue)) - as.numeric(as.character(v3$speedCACCValue)), color = "ACC-CACC"),
              stat="identity", fill = "blue", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC-CACC", "ACC"),
                     values = c("black", "blue")) +
  xlab("Position (meter)") + ylab("Speed (km/h)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000)


#distance
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$distanceACCValue)) * 10- as.numeric(as.character(v3$distanceCACCValue)) *10, color = "ACC-CACC"),
              stat="identity", fill = "blue", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC-CACC", "ACC"),
                     values = c("black", "blue")) +
  xlab("Position (meter)") + ylab("Distance (meters * 10)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000)

#outputs
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$outsCACCValue)) - as.numeric(as.character(v3$outsACCValue)), color = "ACC-CACC"),
              stat="identity", fill = "black", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC-CACC", "Distance", "SelfBrakingDistance"),
                     values = c("black", "blue","red")) +
  xlab("Position (meter)") + ylab("Distance (meters)") +  
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  theme(
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000) 


guardcorr <- read.csv(file="./bayes_corr.csv",sep=",",head=TRUE)   

v3corr1 <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3" & guardcorr$state == "CACC" )
v3corr2 <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3"& guardcorr$state == "ACC")
v3condition <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3" & guardcorr$state == "Condition")
v3conditionFuture <- subset(guardcorr, guardcorr$vehicleID == "Vehicle3" & guardcorr$state == "ConditionFuture")

#v3corr1 <- v3corr1[-1:-1,]
#v3corr2 <- v3corr2[-1:-1,]
#v3condition <- v3condition[-1:-1,]

ggplot(v3corr2, aes(x = as.numeric(as.character(v3corr2$posValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3corr2$value))*1000000000 , color = "ACC"),
              stat="identity", fill = "black", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3corr1$value))*1000000000, color = "CACC"),
              stat="identity", fill = "black", alpha=0.2) +
  scale_color_manual("", breaks = c("ACC", "CACC", "SelfBrakingDistance"),
                     values = c("black", "blue","red")) +
  xlab("Position (meter)") + ylab("Correlation coefficient * 10e9") +  
  guides(color=guide_legend(override.aes=list(fill=NA)))+
  theme(
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) + xlim(0,7000)


dpBayes <- ggplot(v3condition, aes(x = as.numeric(as.character(v3condition$posValue))))+
  ylim("","false","true") +
  geom_point(aes(y = v3condition$value), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("corr")

dpBayesFuture <- ggplot(v3conditionFuture, aes(x = as.numeric(as.character(v3conditionFuture$posValue))))+
  xlab("Vehicle3 Position (meter)") + ylab("corr prediction") + ylim("","false","true") +
  geom_point(aes(y = v3conditionFuture$value), color = "black", size=ps) 

grid.arrange(dp, dp1, dp2, dp3, dpBayes, dpBayesFuture, 
             ncol = 1, nrow = 6)
