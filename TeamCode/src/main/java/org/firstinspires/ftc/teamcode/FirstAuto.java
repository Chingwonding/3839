package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Hardware;
@Autonomous(name = "Auto")
public class FirstAuto extends LinearOpMode {

    Hardware robot = Hardware.getInstance();

    //essentially our main method
    public void runOpMode(){
        robot.init(hardwareMap);
        //always add telemetry.update() to make sure telemetry runs repeatedly
        telemetry.addData("Status", "Hello, Drivers!");
        telemetry.update();

        waitForStart();
        //once auto starts
        move(6, 0.67);
        move(10, 0.3);
        turning(800, 0.2);

    }
    public void move(double distance, double speed){
        double wheelCircumference = 4 * Math.PI;
        double motor = 560;
        double ticks = (distance * (motor/wheelCircumference));

        robot.rf.setTargetPosition((int)Math.round(ticks));
        robot.lf.setTargetPosition((int)Math.round(ticks));
        robot.rb.setTargetPosition((int)Math.round(ticks));
        robot.lb.setTargetPosition((int)Math.round(ticks));

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setPower(speed, speed, speed, speed);

        while (opModeIsActive() && robot.lb.isBusy()){

        }
        robot.setPower(0, 0, 0, 0);

        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    public void turning(int ticks, double speed){
        robot.rf.setTargetPosition(ticks);
        robot.lf.setTargetPosition(-ticks);
        robot.rb.setTargetPosition(ticks);
        robot.lb.setTargetPosition(-ticks);

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setPower(speed, speed, -speed, -speed);

        while (opModeIsActive() && robot.lb.isBusy()){

        }
        robot.setPower(0, 0, 0, 0);

        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
