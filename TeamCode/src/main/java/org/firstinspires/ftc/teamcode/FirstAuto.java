package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto")
public class FirstAuto extends LinearOpMode {


    Hardware robot = Hardware.getInstance();

    public void runOpMode() {
        //before autonomous code starts
        robot.init(hardwareMap);
        // In autonomous we will press init then leave the robot, initializing the hardware class
        //always add telemetry.update() to make sure telemetry runs repeatedly
        telemetry.addData( "Statue", "Hello Drivers");
        telemetry.update();

        waitForStart();
        // essentially the main method
        move(6, 0.67);
        move(3, 0.67);
        turning(800,0.2);
    }
    public void move(double distance, double speed){
        double wheelCircumference = 4 * Math.PI;
        double motor = 560;
        double ticks = (distance * (motor/wheelCircumference));

        robot. rf.setTargetPosition((int) Math.round(ticks));
        robot. lf.setTargetPosition((int) Math.round(ticks));
        robot. rb.setTargetPosition((int) Math.round(ticks));
        robot. lb.setTargetPosition((int) Math.round(ticks));

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //its going to save the last value of ticks, reset so it thinks its at 0 and goes again

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // tells it to run to certain number of ticks

        robot.setPower(speed, speed, speed, speed);
        while (opModeIsActive() && robot.lb.isBusy()){

        }
        robot.setPower(0,0,0,0);
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }
    //make new method for setting shot speed??

    public void turning (int ticks, double speed){

        robot. rf.setTargetPosition(ticks);
        robot. lf.setTargetPosition(-ticks);
        robot. rb.setTargetPosition(ticks);
        robot. lb.setTargetPosition(-ticks);

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //its going to save the last value of ticks, reset so it thinks its at 0 and goes again

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // tells it to run to certain number of ticks

        robot.setPower(speed, speed, -speed, -speed);
        while (opModeIsActive() && robot.lb.isBusy()){

        }
        robot.setPower(0,0,0,0);
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
