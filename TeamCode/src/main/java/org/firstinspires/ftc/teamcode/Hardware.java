package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Hardware {


    public DcMotor rf;
    public DcMotor lf;
    public DcMotor rb;
    public DcMotor lb;
    public Servo demoServo;
    public static double maxSpeed = 0.9;

    private static Hardware myInstance = null;
    public static Hardware getInstance() {

        if (myInstance == null) {
            myInstance = new Hardware();
        }
        return myInstance;
    }

    public void init(HardwareMap hwMap) {
        //time based: why is it bad? uses time, does not account for slippage
        //encoder: what is it? counts ticks to get to position, slippage
        //odometry: what is it? corrects position
        //initialize motors
        rf = hwMap.get(DcMotor.class, "rf");
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setPower(0);

        lf = hwMap.get(DcMotor.class, "lf");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setPower(0);

        rb = hwMap.get(DcMotor.class, "rb");
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setPower(0);

        lb = hwMap.get(DcMotor.class, "lb");
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setPower(0);

        //initialize servo


        demoServo = hwMap.get(Servo.class, "demoServo");
    }
    public void setPower(double fr, double br, double bl, double fl) {

        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));

    }


}
