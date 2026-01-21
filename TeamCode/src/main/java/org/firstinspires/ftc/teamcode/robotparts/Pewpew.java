package org.firstinspires.ftc.teamcode.robotparts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Pewpew {

    public Timer pathTimer = new Timer();
    private Telemetry telemetry;
    private Hardware robot = Hardware.getInstance();



    /**
     * Constructor for Pewpew. 
     * Passing the OpMode's telemetry allows us to use MultipleTelemetry to send data
     * to both the Driver Station and the FTC Dashboard.
     */
    public Pewpew(Telemetry opModeTelemetry) {
        this.telemetry = new MultipleTelemetry(opModeTelemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void resetTimer() {
        pathTimer.resetTimer();
    }

    // Individual outtakes sequence
    public boolean outtake(String separate) {
        robot.velocitySetter(3800);
        robot.intake.setPower(0.0);
        robot.wheel.setPower(0.0);
        robot.gatekeepTwo.setPosition(0.439);

        double time = pathTimer.getElapsedTimeSeconds();

        // Sequential logic
        if (time > 0.8 && time <= 2.0) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else if (time > 2.0 && time <= 3.2) {
            robot.wheel.setPower(0);
            robot.intake.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        } else if (time > 3.2 && time <= 4.4) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.wheel.setPower(0.99);
            robot.intake.setPower(0.99);
        } else if (time > 4.4 && time <= 5.6) {
            robot.wheel.setPower(0);
            robot.intake.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        } else if (time > 5.6) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.wheel.setPower(0.99);
            robot.intake.setPower(0.99);
        }

        // Send velocity data to Dashboard for graphing
        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());
        
        return (time > 6.5); // Example return condition
    }

    // All at once outtake


    public boolean outtake() {
        robot.velocitySetter(3800);
        robot.intake.setPower(0.0);
        robot.wheel.setPower(0.0);
        robot.gatekeepTwo.setPosition(0.439);

        if (pathTimer.getElapsedTimeSeconds() > 0.8) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        }

        // Send velocity data to Dashboard for graphing
        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());
        
        return (robot.intake.getPower() == 0.99 && robot.wheel.getPower() == 0.99);
    }
}
