package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


@Autonomous(name = "Auto")
//naming  a class so it can be accessed from the driver station

public class FirstAuto extends LinearOpMode {

    Hardware robot = Hardware.getInstance();

    public void runOpMode()
    {
         robot.init(hardwareMap);
         //always add telemetry update() to make sure telemetry runs repeatedly
         telemetry.addData("Status", "Hello, Drivers!");
         //each string we set like this is for the driver station
         //basically print statement
         telemetry.update();
         //updates position


         waitForStart();
         //essentially our main method for doing stuff
        move(6,0.67);
        //essentially 67% speed
        move(10, 0.3);
        turning(800, 0.2);



    }
    //here is where you can write the methods for stuff
    public void move(double distance, double speed)
    {
        double wheelCircumference = 4 + Math.PI;
        //don't blindly use values...
        double motor = 560;
        double ticks = (distance * (motor/wheelCircumference));


        robot.rf.setTargetPosition((int)Math.round(ticks));
        robot.lf.setTargetPosition((int)Math.round(ticks));
        robot.rb.setTargetPosition((int)Math.round(ticks));
        robot.lb.setTargetPosition((int)Math.round(ticks));
        //type casting ticks to be an integer

        //resetting encoders so that previously stored values are not stored
        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.setPower(speed, speed, speed, speed);
        while (opModeIsActive() && robot.lb.isBusy())
        {

        }
        robot.setPower(0, 0, 0, 0);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }
    public void turning(int ticks, double speed)
    {
        robot.rf.setTargetPosition((int)Math.round(ticks));
        robot.lf.setTargetPosition((int)Math.round(ticks));
        robot.rb.setTargetPosition((int)Math.round(ticks));
        robot.lb.setTargetPosition((int)Math.round(ticks));

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (opModeIsActive() && robot.lb.isBusy())
        {

        }
        robot.setPower(0, 0, 0, 0);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }



}



