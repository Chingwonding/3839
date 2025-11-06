package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp (name = "Demo for 3839")
public class OdometryTeleOp extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();


    Hardware robot = Hardware.getInstance();



    public void runOpMode(){
        double forward, sideways, turning, max;
        double scaleFactor = 0.8;

        //before autonomous code starts
        robot.init(hardwareMap);
        // In autonomous we will press init then leave the robot, initializing the hardware class
        //always add telemetry.update() to make sure telemetry runs repeatedly
        telemetry.addData( "Statue", "Hello Drivers");
        telemetry.update();
        boolean shot = true;
        boolean waitingToShoot = false;
        int delay = 1000;
        long TargetTime = 0;
        boolean pressingRT = false;



        waitForStart();
        while (opModeIsActive()) {

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



