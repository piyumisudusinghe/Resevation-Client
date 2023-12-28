package org.reservationclient.config;

import org.enactor.commonlibrary.util.Constant;

import java.util.Scanner;

public class AvailabilityConfigLoader extends ConfigLoader
{
    public static void loadConfig(Scanner scanner)
    {
        config.setProperty(Constant.VEHICLE_TYPE, getUserInput("Enter the type of the vehicle(bus,van,train): ",scanner));
        config.setProperty(Constant.VEHICLE_NUM, getUserInput("Enter the vehicle number: ",scanner));
        config.setProperty(Constant.ORIGIN, getUserInput("Enter the origin of the journey(A,B,C,D): ",scanner));
        config.setProperty(Constant.DESTINATION, getUserInput("Enter the destination of the journey(A,B,C,D): ",scanner));
        config.setProperty(Constant.PAX_COUNT, getUserInput("Enter the number of passengers: ",scanner));
        config.setProperty(Constant.TURN_OF_THE_DAY, getUserInput("Enter the turn of vehicle in the day(number): ",scanner));
        config.setProperty(Constant.TRAVELLING_DATE, getUserInput("Enter the travelling date(yyyy-MM-dd) : ",scanner));
    }
}
