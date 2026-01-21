package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Intake;
import org.firstinspires.ftc.teamcode.robotparts.Lebron;

@Config
@TeleOp (name = "3839's tears")
public class OdometryTeleOp extends LinearOpMode {

    public static double kP = 0, kI = 0, kD = 0, kF = 0;

    private ElapsedTime runtime = new ElapsedTime();

    public int speed = 3500;



    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();

    Intake intake = new Intake();

    Lebron lebron = new Lebron(telemetry);
    boolean robotServo;

    Timer pathTimer = new Timer();

    //main run method
    public void runOpMode() {


        //setting variables
        double forward, sideways, turning, max;
        double scaleFactor = 0.8;

        //int delay = 500;

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot.init(hardwareMap);
        //always add telemetry.update() to make sure telemetry runs repeatedly
        telemetry.addData("Statue", "Hello Drivers");
        telemetry.update();


        //for the servo that puts the ball up near the wheel
        boolean shot = true;
        boolean waitingToShoot = false;
        long TargetTime = 0;
        boolean pressingRT = false;



        int upOrDown = 1;
        int shotOrNah = 1;
        int servoCount = 1;
        int servoCount2 = 1;



        double velocity;

        waitForStart();
        while (opModeIsActive()) {

            //for driving and strafing, hopefully
            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8);



            //shooting
            if(gamepad1.right_trigger > 0.001)
            {
                shotOrNah += 1;
                if (shotOrNah % 2 == 0) {


                    outtake();


                    lebron.goat();



                }
                else
                {
                    robot.shotMotorOne.setVelocity(0);
                    robot.shotMotorTwo.setVelocity(0);
                }

            }
            if (gamepad1.leftBumperWasPressed())
            {
                servoCount += 1;
                if(servoCount % 2 == 0) {
                    robotServo = true;
                    robot.gatekeepTwo.setPosition(0.45);
                    //robot.gatekeepOne.setPosition(1.4);
                    timer.resetTimer();

                }
                else {
                    robotServo = false;
                    //robot.gatekeepOne.setPosition(0.3);
                    robot.gatekeepTwo.setPosition(0.75);


                }

            }









            //above is an example of how to set something into a certain position for a certain amount
            // of time before automatically readjusting

            if (gamepad2.right_bumper)

            {
                upOrDown += 1;
                if (upOrDown % 2 == 0)
                {
                    intake.intake();

                }
                else
                {
                    intake.intake(0);
                }

            }


            if(gamepad2.aWasPressed())
            {


                servoCount2 += 1;
                if(servoCount2 % 2 == 0) {
                    robot.wheel.setPower(0.99);


                }
                else
                {
                    //robot.servoTwo.setPosition(0.65);
                    robot.wheel.setPower(0.00);
                }


                outtake();

            }


            if (gamepad2.xWasPressed())
            {
                outtake();
            }









        }
    }

    public void drive(double forward, double right, double rotate)
    {


        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double max = Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.max(Math.abs(frontLeftPower), Math.abs(backRightPower))));
        double scaleFactor;
        if (max > 1) {
            scaleFactor = 1 / max;
        } else {
            scaleFactor = 1;
        }
        scaleFactor *= Math.max(Math.abs(1 - gamepad1.right_trigger), 0.2);
        robot.setPower((frontRightPower) * scaleFactor, (backRightPower) * scaleFactor, (backLeftPower) * scaleFactor, (frontLeftPower) * scaleFactor);





    }


    public void outtake() {
        // Start shooting motors immediately
        robot.shotMotorTwo.setPower(0.99);
        robot.shotMotorOne.setPower(0.99);

        // You mentioned intake/wheels need to be ON for outtake to work
        robot.intake.setPower(0.99);
        robot.wheel.setPower(0.99);

        // Introducing the gap: Wait 0.5s for motors to rev before opening gate
        if (pathTimer.getElapsedTimeSeconds() > 3) {
            robot.gatekeepTwo.setPosition(0.75); // Open
        } else {
            robot.gatekeepTwo.setPosition(0.45); // Closed
        }
    }

}
