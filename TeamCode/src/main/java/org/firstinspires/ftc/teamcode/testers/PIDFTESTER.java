package org.firstinspires.ftc.teamcode.testers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;

@Config
@TeleOp (name = "PIDF tester")
public class PIDFTESTER extends LinearOpMode {

    public static double kP = 0, kI = 0, kD = 0, kF = 0;

    private ElapsedTime runtime = new ElapsedTime();
    public int speed = 3500;
    public PIDFController controller = new PIDFController(new PIDFCoefficients(kP, kI, kD, kF));

    Timer timer = new Timer();
    Pewpew pewpew; // Moved initialization to runOpMode

    Hardware robot = Hardware.getInstance();
    boolean isShooting = false;

    @Override
    public void runOpMode() {
        // Initialize pewpew here to avoid NullPointerExceptions
        pewpew = new Pewpew(telemetry);
        
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot.init(hardwareMap);
        
        telemetry.addData("Status", "Hello Drivers");
        telemetry.update();

        waitForStart();
        
        while (opModeIsActive()) {
            // Update coefficients from Dashboard instead of re-creating the controller
            controller.setCoefficients(new PIDFCoefficients(kP, kI, kD, kF));

            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8);

            // Toggle Shooting State with X
            if (gamepad2.xWasPressed()) {
                isShooting = !isShooting;
                if (isShooting) {
                    timer.resetTimer();
                } else {
                    pewpew.reset();
                }
            }

            // Continuously call outtake while in shooting state
            if (isShooting) {
                pewpew.outtake(timer);
            }

            double sped = robot.shotMotorTwo.getVelocity();
            double sped2 = robot.shotMotorOne.getVelocity();

            telemetry.addData("Is Shooting", isShooting);
            telemetry.addData("Timer", timer.getElapsedTimeSeconds());
            telemetry.addData("Motor one velocity", sped2);
            telemetry.addData("Motor two velocity", sped);
            telemetry.update();
        }
    }

    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), 
                     Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        
        double scaleFactor = (max > 1) ? 1 / max : 1;
        scaleFactor *= Math.max(Math.abs(1 - gamepad1.right_trigger), 0.2);
        
        robot.setPower(frontRightPower * scaleFactor, backRightPower * scaleFactor, 
                       backLeftPower * scaleFactor, frontLeftPower * scaleFactor);
    }
}
