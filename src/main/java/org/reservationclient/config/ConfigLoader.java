package org.reservationclient.config;

import org.reservationclient.util.ResClientConstant;

import java.util.Properties;
import java.util.Scanner;

public class ConfigLoader
{
    public static final Properties config = new Properties();
    public static void loadConfig(Scanner scanner)
    {
        config.setProperty(ResClientConstant.AVAILABILITY_CHECKER_BASE_URL, getUserInput("Enter base URL of the availability-checker-service: ",scanner));
        config.setProperty(ResClientConstant.RESERVATION_SERVICE_BASE_URL, getUserInput("Enter base URL of the reservation-service: ",scanner));
        config.setProperty(ResClientConstant.NO_OF_USERS, getUserInput("Enter the number of users: ", scanner));
    }
    protected static String getUserInput(String prompt, Scanner scanner)
    {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
