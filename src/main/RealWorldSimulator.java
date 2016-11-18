package main;

import main.Common.DigitalInput;
import main.Common.Motor;
import main.Common.MotorManager;
import main.Common.SensorManager;

public class RealWorldSimulator
{
    private static final int GarageFullyOpened = 250;

    private GarageState garageState;
    private int numUpdatesOpened;
    public RealWorldSimulator()
    {
        this.garageState = GarageState.Stopped;
        this.numUpdatesOpened = 0;
    }

    public void update()
    {
        Motor motor = MotorManager.get(ElectronicsConstants.GARAGEDOOR_MOTOR_CHANNEL);
        if (motor.get() > 0)
        {
            if (this.garageState != GarageState.Opening)
            {
                this.garageState = GarageState.Opening;
            }
            else
            {
                this.numUpdatesOpened++;
            }
        }
        else if (motor.get() < 0)
        {
            if (this.garageState != GarageState.Closing)
            {
                this.garageState = GarageState.Closing;
            }
            else
            {
                this.numUpdatesOpened--;
            }
        }
        else // if (motor.get() == 0)
        {
            if (this.garageState != GarageState.Stopped)
            {
                this.garageState = GarageState.Stopped;
            }
        }

        DigitalInput openSensor = (DigitalInput)SensorManager.get(ElectronicsConstants.GARAGEDOOR_OPENSENSOR_CHANNEL);
        DigitalInput closedSensor = (DigitalInput)SensorManager.get(ElectronicsConstants.GARAGEDOOR_CLOSEDSENSOR_CHANNEL);

        openSensor.set(false);
        closedSensor.set(false);
        if (this.numUpdatesOpened >= RealWorldSimulator.GarageFullyOpened)
        {
            openSensor.set(true);
        }
        else if (this.numUpdatesOpened <= 0)
        {
            closedSensor.set(true);
        }
    }
    
    public enum GarageState
    {
        Stopped,
        Opening,
        Closing;
    }
}