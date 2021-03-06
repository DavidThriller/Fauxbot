package org.usfirst.frc.team1318.robot.fauxbot;

import org.usfirst.frc.team1318.robot.MechanismManager;
import org.usfirst.frc.team1318.robot.driver.Driver;

public class FauxbotRunner implements Runnable
{
    private final MechanismManager mechanisms;
    private final Driver driver;
    private final IRealWorldSimulator simulator;
    private final Fauxbot fauxbot;
    private boolean stop;

    public FauxbotRunner(MechanismManager mechanisms, Driver driver, IRealWorldSimulator simulator, Fauxbot fauxbot)
    {
        this.mechanisms = mechanisms;
        this.driver = driver;
        this.simulator = simulator;
        this.fauxbot = fauxbot;
        this.stop = false;
    }

    @Override
    public void run()
    {
        while (!this.stop)
        {
            this.driver.update();
            this.mechanisms.update();
            this.simulator.update();
            this.fauxbot.refresh();

            try
            {
                Thread.sleep(20);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void stop()
    {
        this.stop = true;
    }
}
