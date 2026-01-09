package org.firstinspires.ftc.teamcode;


import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp (name = "3839's tears")
public class OdometryTeleOp extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();


    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();
    boolean robotServo;

    //main run method
    public void runOpMode() {


        //setting variables
        double forward, sideways, turning, max;
        double scaleFactor = 0.8;

        //int delay = 500;

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



            if(gamepad1.right_trigger > 0.01)
            {
                shotOrNah += 1;
                if (shotOrNah % 2 == 0) {


                    velocity = 0.450
                            * 6000 * 28 / 60;
                    robot.shotMotorOne.setPower(velocity);
                    robot.shotMotorTwo.setPower(velocity);

                    telemetry.addData("Velocity", velocity);
                    telemetry.update();



                }
                else
                {
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                }




            }



        //first servo
            if (gamepad1.leftBumperWasPressed())
            {
                servoCount += 1;
                if(servoCount % 2 == 0) {
                    robotServo = true;
                    robot.gatekeepTwo.setPosition(0.807);
                    robot.gatekeepOne.setPosition(0.463);
                    timer.resetTimer();

                }
                else {
                    robotServo = false;
                    robot.gatekeepOne.setPosition(0.711);
                    robot.gatekeepTwo.setPosition(0.359);

                }

            }









            //above is an example of how to set something into a certain position for a certain amount
            // of time before automatically readjusting

            if (gamepad2.right_bumper)

            {
                upOrDown += 1;
                if (upOrDown % 2 == 0)
                {
                    robot.intake.setPower(0.99);

                }
                else
                {
                    robot.intake.setPower(0);
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


}



