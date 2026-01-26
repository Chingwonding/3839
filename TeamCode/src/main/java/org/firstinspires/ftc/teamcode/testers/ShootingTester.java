package org.firstinspires.ftc.teamcode.testers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;

@Config
@TeleOp (name = "PIDF tester")
public class ShootingTester extends LinearOpMode {

    public static int targetVelocity = 3500;

    public static int targetLongShot = 5000;
    private ElapsedTime runtime = new ElapsedTime();
    public int speed = 3800;

    char b = 'b';

    Timer timer = new Timer();
    Pewpew pewpew;

    Hardware robot = Hardware.getInstance();
    boolean isShooting = false;

    boolean farshooting = false;
    @Override
    public void runOpMode() {
        pewpew = new Pewpew(telemetry);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot.init(hardwareMap);
        
        telemetry.addData("Status", "Hello Drivers");
        telemetry.update();


        waitForStart();
        
        while (opModeIsActive()) {

            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8);


            // Toggle Shooting State with X
            if (gamepad2.xWasPressed()) {
                isShooting = !isShooting;
                if (isShooting) {
                    timer.resetTimer();
                }

            }

            if (gamepad1.xWasPressed())
            {
                farshooting = !farshooting;
                if (farshooting)
                {
                    timer.resetTimer();
                }
            }


            if (isShooting) {
                pewpew.outtake(timer, targetVelocity);
                /*
                if (targetVelocity > robot.shotMotorOne.getVelocity()
                        && targetVelocity > robot.shotMotorTwo.getVelocity())
                {
                    robot.shotMotorOne.setPower(1);
                    robot.shotMotorTwo.setPower(1);
                }
                else
                {
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                }

                 */
            }
            if (!isShooting & !farshooting)
            {
                pewpew.reset();
                /*
                if (targetLongShot > robot.shotMotorOne.getVelocity()
                        && targetLongShot > robot.shotMotorTwo.getVelocity())
                {
                    robot.shotMotorOne.setPower(1);
                    robot.shotMotorTwo.setPower(1);
                }
                else
                {
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                }

                 */
            }
            if (farshooting)
            {
                pewpew.outtake(timer, b);
            }




            double velo = robot.getVelocity();
            telemetry.addData("Shotmotortwo velocity: ", velo);
            telemetry.addData("Intake:", robot.intake.getPower() * 435);
            telemetry.addData("Wheel: ", robot.wheel.getPower() * 435);
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
