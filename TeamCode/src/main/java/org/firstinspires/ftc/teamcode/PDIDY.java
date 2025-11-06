package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PDIDY extends LinearOpMode {



    //we need to tune it???

    double integralSum = 0;
    double Kp = 0;
    //proportionall
    double Ki = 0;
    //integral term
    double Kd = 0;
    //derivative term

    double Kf = 0;
    //k feet forward?

    //we are supposed to tune Kf first then tune the other ones




    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;
    @Override
    public void runOpMode() throws InterruptedException
    {
        motor = HardwareMap.get(DcMotor.class, "motor");
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();
        while (opModeIsActive())
        {
            double power = PIDControl( 1000, motor.getVelocity());
            motor.setPower(power);

        }

    }

    public double PIDControl(double reference, double state)
    {
        //sets integral sum to something not zero
        double error = reference - state;
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) . timer.seconds();
        lastError - error;
        timer.reset();

        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki) + (reference * Kf);
        return output;
    }


}




/*
       Notes:
       1) Error-->The difference between desired and actual value
           -Our goal with PID is to set Error to zero
       2) PID stands for proportionally, integral, and derivative

       3) Kp, Ki, and Kd are coefficients

                Steps to building PID:
       -First use a method to calculate error--> double reference, double state
       basically what we want and where we are
       We get the error from this

       then Kp * error (preliminary controller)

       set all our coefficients to doubles as instances
       integral += error * dt;
       return Kp * error + Ki * integral


        trapezoid method-->more stable that the raymond sum approach

        improper tunings of thing will lead to unstable ocillations

        get error-->then multiply integrate and derive until



 */

