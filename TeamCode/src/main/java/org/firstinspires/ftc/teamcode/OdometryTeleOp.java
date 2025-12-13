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


//let me try to beautify this whole thing so its more readable


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

            //for driving and strafing hopefully
            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)), (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)), (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.5);



            if(gamepad1.leftBumperWasPressed())
            {
                shotOrNah += 1;
                if (shotOrNah % 2 == 0) {


                    velocity = 0.65 * 6000 * 28 / 60;
                    robot.shotMotorOne.setVelocity(velocity);
                    robot.shotMotorTwo.setVelocity(velocity);
                    telemetry.addData("shot power: ", velocity);
                    telemetry.update();

                }
                else
                {
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                }




            }
            if (gamepad1.x)
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

            if (gamepad1.rightBumperWasPressed())
            {
                robotServo = true;
                robot.UpServo.setPosition(0.428);
                timer.resetTimer();
            }

            if (robotServo && timer.getElapsedTimeSeconds() > 0.5) {
                robotServo = false;
                robot.UpServo.setPosition(0.593);

            }

            if(gamepad1.a)
            {

                servoCount2 += 1;
                if(servoCount2 % 2 == 0) {
                    robot.servoTwo.setPosition(0.3);


                }
                else
                {
                    robot.servoTwo.setPosition(0.1);
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



