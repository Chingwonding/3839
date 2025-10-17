package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp( name = "Appears on Driver Station")
public class TeleOpClass extends LinearOpMode{

    Hardware robot = Hardware.getInstance();

    public void runOpMode() {
        robot.init(hardwareMap);
        {
            //once you press init
            telemetry.addData( "Status", "Hello, Drivers!");
            telemetry.update();


            waitForStart();
            //once you press play
            boolean pressingb = false;
            boolean pressingx = false;


            boolean difference = false;
            while (opModeIsActive())
            {
                double movement = gamepad1.right_stick_x;
                //gamepad1 = one driver
                //gamepad2 = two drivers
                double strafing = -gamepad1.right_stick_y;
                double turning = -gamepad1.left_stick_y;


                double max = Math.max(Math.abs(movement - strafing - turning), Math.max(Math.abs(movement + strafing - turning), Math.max(Math.abs(movement + strafing + turning), Math.abs(movement - strafing + turning))));

                if (max < robot.maxSpeed) {
                    robot.setPower(movement - strafing - turning,
                            movement + strafing - turning,
                            movement + strafing + turning,
                            movement - strafing + turning);

                }
                else
                {
                    double scaleFactor = max / robot.maxSpeed;
                    robot.setPower(movement - strafing - turning * scaleFactor,
                            movement + strafing - turning * scaleFactor,
                            movement + strafing + turning * scaleFactor,
                            movement - strafing + turning * scaleFactor);
                }
                if (gamepad2.a)
                {
                    robot.lb.setPower(1);

                }
                //just keep making if statements so that stuff happens for each button
                if ((gamepad2.left_trigger > 0.3))
                {
                    robot.lb.setPower(1);
                }
                if (gamepad2.b && !pressingb)
                {
                    robot.lb.setPower(1);
                    pressingb = true;
                    difference = false;

                }
                else if (gamepad2.x && !pressingx && !difference)
                {
                    robot.rb.setPower(-1);

                }
                else if (!gamepad1.x)
                {
                    pressingx = false;
                }
                else if (!gamepad1.b) {
                    pressingb = false;


                }



            }

        }
    }







}

