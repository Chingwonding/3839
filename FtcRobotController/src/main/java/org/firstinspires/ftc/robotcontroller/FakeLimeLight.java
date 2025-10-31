package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class FakeLimeLight extends OpMode {

    //instance variable...?
    private Limelight3A limelight3A;
    //here the variable type is Limelight 3a with the actual name being limelight3A with a lowercase
    @Override
    public void init()
    {
        limelight3A = hardwareMap.get(Limelight3A.class, "limeLight");
        limelight3A.pipelineSwitch(0); //0 is blue and 1 is red
        //questions:
        // Is it possible to detect more than 2 colors since we need more than 2,
        //and how will it look like?





    }
    public void start()
    {
        limelight3A.start();
    }


    @Override
    public void loop() {
        LLResult llResult = limelight3A.getLatestResult();
        if (llResult != null && llResult.isValid())
        {
            //if result is not empty and is valid
            telemetry.addData("Target X offset", llResult.getTx());
            telemetry.addData("Target Y offset", llResult.getTy());
            telemetry.addData("Target Area offset", llResult.getTa());

            //what does Ta mean?
            //What is target x and target y, are they just movement in the x and y direction
            //or is it a set point where
        }
    }
    /*
    Notes:
    1. When it means target X or target Y, it means the distance
    in y and x axis from a ball (for this year)
        -Different ways to reach target...
        -The approach I think we should do is to try and turn the robot until Tx is near 0, and then move the y until the intake can store the balls


     */

}
