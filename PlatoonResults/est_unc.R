library(ggplot2)
library(gridExtra)


setwd('C:/Users/rima/Desktop/R_thesis_results')


guard <- read.csv(file="./est_unc.csv",sep=",",head=TRUE)   
names(guard)

v1 <- subset(guard, guard$vehicleID == "Vehicle1")
v2 <- subset(guard, guard$vehicleID == "Vehicle2")
v3 <- subset(guard, guard$vehicleID == "Vehicle3")

#v1 <- v1[-1:-1,]
#v2 <- v2[-1:-1,]
#v3 <- v3[-1:-1,]

uncFontSize <- 5
#speed v1 & v2 & v3
ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedValue)), color = "Vehicle2", ymin = as.numeric(as.character(v3$refSpeedMin)), ymax = as.numeric(as.character(v3$refSpeedMax))),
              stat="identity", fill = "blue", alpha = 0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedMin)), color = "Vehicle2"), stat="identity", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$refSpeedMax)), color = "Vehicle2"), stat="identity", alpha=0.2) +
  geom_smooth(aes(y = as.numeric(as.character(v3$speed)), color = "Vehicle3"),
              stat="identity", fill = "red", alpha=0.2) +
  scale_color_manual("", breaks = c("Vehicle2", "Vehicle3"),
                     values = c("blue","red")) +
  theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0))+ 
  guides(color=guide_legend(override.aes=list(fill=NA))) +
  xlab("Position (meter)") + ylab("Speed (km/h)") +ylim(0,120) + xlim(0,7000) +  
  annotate("rect", xmin=c(1350), xmax=c(1450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1350, y = 1, label = "R", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(1750), xmax=c(1900), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1750, y = 1, label = "D", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(2200), xmax=c(2300), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2200, y = 1, label = "G", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(2600), xmax=c(2700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2600, y = 1, label = "W", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(3000), xmax=c(3050), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3000, y = 1, label = "GW", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(3350), xmax=c(3400), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3350, y = 1, label = "WR", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(3700), xmax=c(3750), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3700, y = 1, label = "RD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(4050), xmax=c(4100), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4050, y = 1, label = "GD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(4400), xmax=c(4450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4400, y = 1, label = "WD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(4750), xmax=c(4800), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4750, y = 1, label = "GR", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(5100), xmax=c(5150), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5100, y = 1, label = "GRD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(5450), xmax=c(5500), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5450, y = 1, label = "GWR", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(5800), xmax=c(5850), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5800, y = 1, label = "WRD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(6150), xmax=c(6200), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6150, y = 1, label = "GWD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(6500), xmax=c(6700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6500, y = 1, label = "GWRD", color = "grey20", size = uncFontSize, angle = 90) 
  

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
theme(text = element_text(size=25),
    # Change legend background color
    legend.position = "bottom",
    legend.key = element_rect(colour = "transparent", fill = "white"),
    legend.justification = c(1,0)) +ylim(-150,150) + xlim(0,7000) +
  annotate("rect", xmin=c(1350), xmax=c(1450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1350, y = -150, label = "R", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(1750), xmax=c(1900), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1750, y = -150, label = "D", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(2200), xmax=c(2300), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2200, y = -150, label = "G", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(2600), xmax=c(2700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2600, y = -150, label = "W", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(3000), xmax=c(3050), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3000, y = -150, label = "GW", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(3350), xmax=c(3400), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3350, y = -150, label = "WR", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(3700), xmax=c(3750), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3700, y = -150, label = "RD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(4050), xmax=c(4100), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4050, y = -150, label = "GD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(4400), xmax=c(4450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4400, y = -150, label = "WD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(4750), xmax=c(4800), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4750, y = -150, label = "GR", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(5100), xmax=c(5150), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5100, y = -150, label = "GRD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(5450), xmax=c(5500), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5450, y = -150, label = "GWR", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(5800), xmax=c(5850), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5800, y = -150, label = "WRD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(6150), xmax=c(6200), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6150, y = -150, label = "GWD", color = "grey20", size = uncFontSize, angle = 90) +
  annotate("rect", xmin=c(6500), xmax=c(6700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6500, y = -150, label = "GWRD", color = "grey20", size = uncFontSize, angle = 90) 




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
  theme(text = element_text(size=25),
        axis.title.y=element_blank(),
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
s<- 7

dp <- ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  ylim("","false","true") +
  theme(text = element_text(size=25))+
  geom_point(aes(y = tolower(v3$normalCondition)), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("normal")  + xlim(0,7000) + 
  annotate("rect", xmin=c(1350), xmax=c(1450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1350, y = 1, label = "R", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(1750), xmax=c(1900), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1750, y = 1, label = "D", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(2200), xmax=c(2300), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2200, y = 1, label = "G", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(2600), xmax=c(2700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2600, y = 1, label = "W", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(3000), xmax=c(3050), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3000, y = 1, label = "GW", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(3350), xmax=c(3400), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3350, y = 1, label = "WR", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(3700), xmax=c(3750), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3700, y = 1, label = "RD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(4050), xmax=c(4100), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4050, y = 1, label = "GD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(4400), xmax=c(4450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4400, y = 1, label = "WD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(4750), xmax=c(4800), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4750, y = 1, label = "GR", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(5100), xmax=c(5150), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5100, y = 1, label = "GRD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(5450), xmax=c(5500), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5450, y = 1, label = "GWR", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(5800), xmax=c(5850), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5800, y = 1, label = "WRD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(6150), xmax=c(6200), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6150, y = 1, label = "GWD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(6500), xmax=c(6700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6500, y = 1, label = "GWRD", color = "grey20", size = s, angle = 90)

dp1 <- ggplot(v3, aes(x = as.numeric(as.character(v3$vehPosValue))))+
  ylim("","false","true") +
  theme(text = element_text(size=25))+
  geom_point(aes(y = tolower(v3$minCondition)), color = "black", size=ps) +
  xlab("Vehicle3 Position (meter)") + ylab("min")  + xlim(0,7000) +
  annotate("rect", xmin=c(1350), xmax=c(1450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1350, y = 1, label = "R", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(1750), xmax=c(1900), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 1750, y = 1, label = "D", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(2200), xmax=c(2300), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2200, y = 1, label = "G", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(2600), xmax=c(2700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 2600, y = 1, label = "W", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(3000), xmax=c(3050), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3000, y = 1, label = "GW", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(3350), xmax=c(3400), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3350, y = 1, label = "WR", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(3700), xmax=c(3750), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 3700, y = 1, label = "RD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(4050), xmax=c(4100), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4050, y = 1, label = "GD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(4400), xmax=c(4450), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4400, y = 1, label = "WD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(4750), xmax=c(4800), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 4750, y = 1, label = "GR", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(5100), xmax=c(5150), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5100, y = 1, label = "GRD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(5450), xmax=c(5500), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5450, y = 1, label = "GWR", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(5800), xmax=c(5850), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 5800, y = 1, label = "WRD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(6150), xmax=c(6200), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6150, y = 1, label = "GWD", color = "grey20", size = s, angle = 90) +
  annotate("rect", xmin=c(6500), xmax=c(6700), ymin= -Inf , ymax= Inf, alpha=0.2, fill="lightgray") + 
  annotate("text", x = 6500, y = 1, label = "GWRD", color = "grey20", size = s, angle = 90)


grid.arrange(dp, dp1, ncol = 1, nrow = 2)
