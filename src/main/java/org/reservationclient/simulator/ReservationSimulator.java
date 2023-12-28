package org.reservationclient.simulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.enactor.commonlibrary.exception.InvalidRequestException;
import org.enactor.commonlibrary.model.BusReservationCriteria;
import org.enactor.commonlibrary.model.VehicleType;
import org.enactor.commonlibrary.util.Constant;
import org.enactor.commonlibrary.util.DateUtil;
import org.enactor.commonlibrary.util.JsonMappingUtil;
import org.enactor.commonlibrary.util.RequestGenerator;
import org.reservationclient.Main;
import org.reservationclient.config.ConfigLoader;
import org.reservationclient.util.ResClientConstant;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationSimulator
{
    private static final Logger logger = Main.logger;
    public static String reservationServiceBaseUrl;

    /**
     * This method is used to simulate the calling of seat reservation end point in reservation-service
     */
    public static void simulate()
    {
        int numberOfUsers = Integer.parseInt(ConfigLoader.config.getProperty(ResClientConstant.NO_OF_USERS));
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
        java.util.List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++)
        {
            tasks.add(createReserveSeatsTask("User" + i));
        }
        try
        {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e)
        {
            logger.log(Level.SEVERE, "Error during task execution", e);
        } finally
        {
            // Shutdown the executor service
            executorService.shutdown();
        }
    }

    /**
         * This method create a POST request for reserve seats end point ine reservation-service
     */
    private static Callable<Void> createReserveSeatsTask(String userName)
    {
        return () ->
        {
            try
            {
                reserveSeats();
            } catch (Exception e)
            {
                logger.log(Level.SEVERE, () -> "Error while making request to the reservation service\n" + e);
            }
            return null;
        };
    }

    /**
     * This method create a POST request for reserve end point ine reservation-service
     */
    private static void reserveSeats()
    {
        reservationServiceBaseUrl = ConfigLoader.config.getProperty(ResClientConstant.RESERVATION_SERVICE_BASE_URL);
        String endPointUrl = reservationServiceBaseUrl + "/v1/reserveSeats";
        try
        {
            String responseBody = createRequestBody();
            String response = RequestGenerator.makePostRequest(endPointUrl,responseBody);
            logger.log(Level.INFO, () -> "Response from server:\n" + response);
        }catch (InvalidRequestException e)
        {
            logger.log(Level.SEVERE, () -> "Mandatory arguments were given in invalid formats" + e.getMessage());
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, () -> "Error during reserve seats\n" + e);
        }
    }

    /**
     * This method is used to build the response body relevant to reserve tickets endpoint
     * @return request body to reserve seats in the vehicle
     */
    private static String createRequestBody() throws JsonProcessingException
    {
        try
        {
            BusReservationCriteria busReservationCriteria = new BusReservationCriteria();
            busReservationCriteria.setOrigin(ConfigLoader.config.getProperty(Constant.ORIGIN));
            busReservationCriteria.setDestination(ConfigLoader.config.getProperty(Constant.DESTINATION));
            busReservationCriteria.setTurn(Integer.parseInt(ConfigLoader.config.getProperty(Constant.TURN_OF_THE_DAY)));
            busReservationCriteria.setPaxCount(Integer.parseInt(ConfigLoader.config.getProperty(Constant.PAX_COUNT)));
            busReservationCriteria.setDate(DateUtil.convertStringToDate(ConfigLoader.config.getProperty(Constant.TRAVELLING_DATE)));
            busReservationCriteria.setVehicleNum(ConfigLoader.config.getProperty(Constant.VEHICLE_NUM));
            busReservationCriteria.setVehicleType(VehicleType.getVehicleType(ConfigLoader.config.getProperty(Constant.VEHICLE_TYPE)));
            return JsonMappingUtil.converObjectToString(busReservationCriteria);
        }
        catch (Exception e)
        {
            throw new InvalidRequestException("Mandatory arguments were given in invalid formats");
        }
    }
}
