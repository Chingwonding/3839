package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Appears on driver Station")

public class DemoTeleOP extends LinearOpMode {

    Hardware robot = Hardware.getInstance();
    //don't have to constantly reinitialize all other code
    public void runOpMode(){
        robot.init(hardwareMap){
            // all happens once you press init
            telemetry.addData("status","Hello, Drivers!");
            // essentially system.out.print
            telemetry.update();
            // if you update you can print out data repeatedly
            waitForStart();
            // starts once you press play
            boolean pressingb = false;
            boolean pressingx = false;

            boolean difference = false;
            while(opModeIsActive()) {

                double movement=gamepad1.right_stick_x;
                double strafing = -gamepad1.right_stick_y;
                double turning = -gamepad1.left_stick_y;

                double max = Math.max(Math.abs(movement - strafing - turning),
                        Math.max(Math.abs(movement + strafing - turning),
                                Math.max(Math.abs(movement + strafing + turning),
                                        Math.abs(movement - strafing + turning))));

                if (max< robot.maxSpeed) {
                    robot.setPower(movement - strafing - turning,
                            movement + strafing - turning,
                            movement + strafing + turning,
                            movement - strafing + turning);
                }else {
                    double scaleFactor = max / robot.maxSpeed ;
                    robot.setPower((movement- strafing - turning * scaleFactor,
                            movement + strafing -turning,
                            movement + strafing + turning,
                            movement - strafing + turning);
                }
                if (gamepad2.a) {
                    robot.lb.setPower(1);
                }
                if(gamepad2.left_trigger > 0.3){
                    robot.lb.setPower(1);
                }

                if(gamepad2.b && !pressingb) {
                    robot.lb.setPower(1);
                    pressingb = true;
                }else if (!gamepad1.b) {
                    pressingb = false;
                }



                if(gamepad2.b && !pressingx && difference) {
                    robot.lb.setPower(1);
                    pressingx = true;
                    difference = false;
                }else if (gamepad2.x && !pressingx && (difference == false)) {
                    robot.rb.setPower(-1);
                    pressingx = true;
                }
                else if (!gamepad1.x) {
                    pressingb = false;
                    pressingx = false;
                }


            }
        }
    }


}
