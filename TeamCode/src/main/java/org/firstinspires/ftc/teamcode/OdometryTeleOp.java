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
    public void runOpMode(){


        //setting variables
        double forward, sideways, turning, max;
        double scaleFactor = 0.8;


        robot.init(hardwareMap);
        //always add telemetry.update() to make sure telemetry runs repeatedly
        telemetry.addData( "Statue", "Hello Drivers");
        telemetry.update();


        //for the servo that puts the ball up near the wheel
        boolean shot = true;
        boolean waitingToShoot = false;
        int delay = 1000;
        long TargetTime = 0;
        boolean pressingRT = false;

        int upOrDown = 1;
        int shotOrNah = 1;






        waitForStart();
        while (opModeIsActive()) {


            //hopefully this works...




            //movement
            //most important part....
            //will have to debug fs
            forward = -(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5));
            sideways = (Math.atan(5 * gamepad1.left_stick_x) / Math.atan(5));
            turning = (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.5;
            max = Math.max(Math.abs(forward - sideways - turning), Math.max(Math.abs(forward + sideways - turning), Math.max(Math.abs(forward + sideways + turning), Math.abs(forward + turning - sideways))));
            if (max > 1) {
                scaleFactor = 1 / max;
            } else {
                scaleFactor = 1;
            }
            scaleFactor *= Math.max(Math.abs(1 - gamepad1.right_trigger), 0.2);
            robot.setPower((forward - sideways - turning) * scaleFactor, (forward + sideways - turning) * scaleFactor, (forward + sideways + turning) * scaleFactor, (forward + turning - sideways) * scaleFactor);

            //add separate part for shooting
            //this is actually for the shooting
            if (gamepad1.x)
            {
                shotOrNah += 1;
                if (shotOrNah % 2 == 0)
                {
                    robot.shotMotor.setPower(0.88);

                }
                else
                {
                    //the motor gets turned off
                    robot.shotMotor.setPower(0.0);
                }

                //the way I coded this right now is that it starts at 1 so the % doesn't show errors in
                //calculating the math...
                //by putting the counter above ts it means with one click it turns on
                //if I put the counter at the end I think it might take 2 to get the desired effect at the beginning

            }




            //add this servo to be dropped
            //this is servotwo
            if (gamepad1.b)
            {
                //this should bring it down so the ball can drop
                //I forgot if it starts at 1 or if it starts at 0.360...
                //probably starts at 1?
                //I'm a genius
                upOrDown++;
                if (upOrDown % 2 == 0)
                {
                    robot.Servotwo.setPosition(0.998);
                }
                else {
                    robot.Servotwo.setPosition(0.360);
                }
                //counter starts at 1
                //one click-->2 goes into the first if
                //-->sets position to be 0.998, dropping the ball
                //confirm positions on Friday


            }

            //we need to add servo three
            //what does servo three do again?
            /*

            if (gamepad1.a)
            {
                set position to something (we need to test using the servo tester class)
            }

             */



            //for servo that launches the ball towards the shooter
            if (gamepad1.right_trigger > 0.1 && !pressingRT)
            {


                robot.shotServo.setPosition(0.998);
                waitingToShoot = true;
                TargetTime = (long) (System.currentTimeMillis() + delay);


            }
            else if (!(gamepad1.right_trigger > 0.1))
            {
                pressingRT = false;

            }
            if (waitingToShoot && (System.currentTimeMillis() > TargetTime) )
            {
                robot.shotServo.setPosition(0.375);
                pressingRT = true;
                waitingToShoot = false;



            }

        }
    }

}



