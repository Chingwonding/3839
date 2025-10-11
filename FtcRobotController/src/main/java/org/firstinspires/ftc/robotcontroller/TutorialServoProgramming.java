package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "Servo Demo")
public class TutorialServoProgramming extends LinearOpMode {
    public Servo intakeServo;
    //type of servo
    //review servo

    public void init(HardwareMap hwMap)
    {
        intakeServo = hwMap.get(Servo.class, "intakeServo");
    }
    public void runOpMode()
    {
        boolean intakeIsOn = false;
        waitForStart();
        while(opModeIsActive())
        {
            if (gamepad2.a)
            {
                if (!intakeIsOn)
                {
                    intakeServo.setPosition(1);
                }
                else
                {
                    intakeServo.setPosition(.1);
                }


            }
            if (gamepad2.b)
            {
                intakeServo.setPosition(.1);
            }
        }

    }


}
