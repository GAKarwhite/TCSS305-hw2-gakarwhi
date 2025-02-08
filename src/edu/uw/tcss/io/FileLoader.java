/*
 * TCSS 305 - Road Rage
 */
 
package edu.uw.tcss.io;
import edu.uw.tcss.logic.PropertyChangeEnabledRoadRageControls;
import edu.uw.tcss.logic.RoadRage;
import edu.uw.tcss.model.Atv;
import edu.uw.tcss.model.Bicycle;
import edu.uw.tcss.model.Car;
import edu.uw.tcss.model.Direction;
import edu.uw.tcss.model.Human;
import edu.uw.tcss.model.Taxi;
import edu.uw.tcss.model.Terrain;
import edu.uw.tcss.model.Truck;
import edu.uw.tcss.model.Vehicle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;




/**
 * A utility class for The Road Rage application. 
 * 
 * @author Charles Bryan
 * @version Autumn 2018
 */
@SuppressWarnings("LocalCanBeFinal")
public final class FileLoader {

    /**
     * The filename of the city map.
     */
    private static final String CITY_FILE = 
                    "maps" 
                    + File.separator
                    + "city_map1.txt";
    
    /**
     * A private constructor, to prevent external instantiation.
     */
    private FileLoader() {
        super();
    }
    
    /**
     * Read the city text file and build an RoadRagePanel based on the file. 
     * @param theFrame the Frame containing the panel. Needed for error output. 
     * @return the RoadRagePanel based on the city text file. 
     */
    public static PropertyChangeEnabledRoadRageControls readCity(final JFrame theFrame) {
        PropertyChangeEnabledRoadRageControls result = null;
        try (Scanner input = new Scanner(new File(CITY_FILE))) {

            // First, we read the map description
            // Then, we read where the initial vehicles are
           
            result = new RoadRage(readGrid(input), readVehicles(input));

        } catch (final IOException ioe) {
            JOptionPane.showMessageDialog(theFrame, "Could not read city map file " + CITY_FILE
                                          + ":\n\n" + ioe.getMessage(), "I/O Error",
                                      JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }
    
    
    /**
     * Reads the grid portion of the map file.
     * 
     * @param theInput The input scanner.
     * @return the grid of the terrains.
     */
    private static Terrain[][] readGrid(final Scanner theInput) {
        final int numRows = theInput.nextInt();
        final int numColumns = theInput.nextInt();
        theInput.nextLine();
        final Terrain[][] grid = new Terrain[numRows][numColumns];
        for (int row = 0; row < numRows; row++) {
            final String line = theInput.nextLine();
            for (int column = 0; column < numColumns; column++) {
                grid[row][column] = Terrain.valueOf(line.charAt(column));
            }
        }
        return grid;
    }

    /**
     * Reads the vehicle portion of the map file.
     * 
     * @param theInput The input scanner.
     * @return the list of Vehicles
     */
    @SuppressWarnings("OverlyLongMethod")
    private static List<Vehicle> readVehicles(final Scanner theInput) {
        final List<Vehicle> vehicles = new ArrayList<>();
        final int numVehicles = theInput.nextInt();
        theInput.nextLine();
        for (int lineCount = 0; lineCount < numVehicles; lineCount++) {
            final char vehicleType = theInput.next().charAt(0);
            final int vehicleX = theInput.nextInt();
            final int vehicleY = theInput.nextInt();
            final char vehicleDirection = theInput.next().charAt(0);

            switch (vehicleType) {
                case 'B': // Bicycle
                    vehicles.add(new Bicycle(vehicleX, vehicleY,
                                               Direction.valueOf(vehicleDirection)));
                    break;

                case 'A': //ATV
                    vehicles.add(new Atv(vehicleX, vehicleY,
                                           Direction.valueOf(vehicleDirection)));
                    break;

                case 'H': // Human
                    vehicles.add(new Human(vehicleX, vehicleY,
                                             Direction.valueOf(vehicleDirection)));
                    break;

                case 'T': // Truck
                    vehicles.add(new Truck(vehicleX, vehicleY,
                                             Direction.valueOf(vehicleDirection)));
                    break;
                    
                case 'X': // Taxi
                    vehicles.add(new Taxi(vehicleX, vehicleY,
                                            Direction.valueOf(vehicleDirection)));
                    break;
                    
                case 'C': // Car
                    vehicles.add(new Car(vehicleX, vehicleY,
                                            Direction.valueOf(vehicleDirection)));
                    break;

                default:
                    // this should never happen
                    assert false;
                    break;
            }
            theInput.nextLine();
        }
        return vehicles;
    }
    
}
