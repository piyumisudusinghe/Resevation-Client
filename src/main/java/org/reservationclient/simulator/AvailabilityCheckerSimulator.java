package org.reservationclient.simulator;

import org.enactor.commonlibrary.util.Constant;
import org.enactor.commonlibrary.util.RequestGenerator;
import org.reservationclient.Main;
import org.reservationclient.config.ConfigLoader;
import org.reservationclient.util.ResClientConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvailabilityCheckerSimulator
{
    private static final Logger logger = Main.logger;
    public static String availabilityCheckerBaseUrl;

    /**
     * This method is used to simulate the calling of availability end point in availability-checker-service
     */
    public static void simulate() {
        int numberOfUsers = Integer.parseInt(ConfigLoader.config.getProperty(ResClientConstant.NO_OF_USERS));
        // Create a thread pool with the desired number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
        // Create a list to hold the tasks
        java.util.List<Callable<Void>> tasks = new ArrayList<>();
        // Create a callable task for each user
        try
        {
            for (int i = 0; i < numberOfUsers; i++)
            {
                executorService.submit(createAvailabilityCheckTask("User" + i));
            }
        } catch ( Exception e)
        {
            logger.log(Level.SEVERE, "Error during task execution", e);
        } finally
        {
            // Shutdown the executor service
            executorService.shutdown();
        }
    }

    /**
     * This method is used to create the availability check task for threads
     * @param userName username of the user/thread
     * @return the created task
     */
    private static Callable<Void> createAvailabilityCheckTask(String userName)
    {
        return () ->
        {
            try
            {
                availabilityCheck();
            } catch (Exception e)
            {
                logger.log(Level.SEVERE, () -> "Error while making request to the availability checker service\n" + e);
            }
            return null;
        };
    }

    /**
     * This method create a GET request for availability end point ine availability-checker-service
     */
    private static void availabilityCheck()
    {
        availabilityCheckerBaseUrl = ConfigLoader.config.getProperty(ResClientConstant.AVAILABILITY_CHECKER_BASE_URL);
        try
        {
            String apiUrl = buildApiUrl(availabilityCheckerBaseUrl);
            // Make the GET request
            String response = RequestGenerator.makeGetRequest(apiUrl);
            logger.log(Level.INFO, () -> "Response from server:\n" + response);
        } catch (IOException e) {
            logger.log(Level.SEVERE, () -> "Error during availability check\n" + e.getMessage());
        }
    }



    /**
     * This method is used to build the API Url relevant to availability endpoint with relevant query parameters
     * @param baseUrl base url with domain
     * @return complete url for get endpoint with query parameter values
     */
    private static String buildApiUrl(String baseUrl)
    {
        String vehicleType = ConfigLoader.config.getProperty(Constant.VEHICLE_TYPE);
        String vehicleNum = ConfigLoader.config.getProperty(Constant.VEHICLE_NUM);
        String origin = ConfigLoader.config.getProperty(Constant.ORIGIN);
        String destination = ConfigLoader.config.getProperty(Constant.DESTINATION);
        String paxCount = ConfigLoader.config.getProperty(Constant.PAX_COUNT);
        String turn = ConfigLoader.config.getProperty(Constant.TURN_OF_THE_DAY);
        String date = ConfigLoader.config.getProperty(Constant.TRAVELLING_DATE);

        return String.format("%s/v1/availabilityCheck?vehicleType=%s&vehicleNum=%s&origin=%s&destination=%s&paxCount=%s&turn=%s&date=%s",
                baseUrl,
                vehicleType,
                vehicleNum,
                origin,
                destination,
                paxCount,
                turn,
                date);
    }
}
