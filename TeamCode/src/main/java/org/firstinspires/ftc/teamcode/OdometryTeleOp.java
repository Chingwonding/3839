package org.firstinspires.ftc.teamcode;


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


    Hardware robot = Hardware.getInstance();


    //main run method
    public void runOpMode() {


        //setting variables
        double forward, sideways, turning, max;
        double scaleFactor = 0.8;


        robot.init(hardwareMap);
        //always add telemetry.update() to make sure telemetry runs repeatedly
        telemetry.addData("Statue", "Hello Drivers");
        telemetry.update();


        //for the servo that puts the ball up near the wheel
        boolean shot = true;
        boolean waitingToShoot = false;
        int delay = 1000;
        long TargetTime = 0;
        boolean pressingRT = false;

        int upOrDown = 1;
        int shotOrNah = 1;
        int servoCount = 1;



        waitForStart();
        while (opModeIsActive()) {

            //for driving and strafing hopefully
            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)), (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)), (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.5);

            if(gamepad1.x)
            {
                shotOrNah += 1;
                if (shotOrNah % 2 == 0) {
                    robot.shotMotorOne.setPower(0.67);
                    robot.shotMotorTwo.setPower(0.67);
                }
                else
                {
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                }




            }
            if (gamepad1.y)
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
            
            if (gamepad1.leftBumperWasPressed())
            {
                servoCount += 1;
                if (servoCount % 2 == 0)
                {
                    robot.UpServo.setPosition(0.260);
                }
                else
                {
                    robot.UpServo.setPosition(0.400);
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



