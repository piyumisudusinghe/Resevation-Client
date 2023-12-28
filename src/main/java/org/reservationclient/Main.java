package org.reservationclient;

import org.reservationclient.config.AvailabilityConfigLoader;
import org.reservationclient.config.ConfigLoader;
import org.reservationclient.config.ReservationConfigLoader;
import org.reservationclient.simulator.AvailabilityCheckerSimulator;
import org.reservationclient.simulator.ReservationSimulator;
import org.reservationclient.util.ResClientConstant;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main
{
    public static final Logger logger = Logger.getLogger(ResClientConstant.RESERVATION_CLIENT);

    public static void main(String[] args)
    {
        try (Scanner scanner = new Scanner(System.in))
        {
            System.out.println("----------------Welcome to the Enactor Reservation Service---------------");
            ConfigLoader.loadConfig(scanner);
            while(true)
            {
                try
                {
                    System.out.println("To choose the service enter the relevant number:\n" +
                            "1.Check seat availability\n" +
                            "2.Reserve tickets\n" +
                            "3.Exit service");
                    String serviceSelection = scanner.nextLine().trim();
                    if (ResClientConstant.AVAILABILITY_SERVICE_OPTION.equals(serviceSelection))
                    {
                        AvailabilityConfigLoader.loadConfig(scanner);
                        AvailabilityCheckerSimulator.simulate();
                    }
                    else if (ResClientConstant.RESERVATION_SERVICE_OPTION.equals(serviceSelection))
                    {
                        ReservationConfigLoader.loadConfig(scanner);
                        ReservationSimulator.simulate();
                    }
                    else
                    {
                        return;
                    }
                }
                catch (Exception exception)
                {
                    logger.log(Level.SEVERE, "Error during task execution", exception);
                    return;
                }
            }
        }
    }
}
