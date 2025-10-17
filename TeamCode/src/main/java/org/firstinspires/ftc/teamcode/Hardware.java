package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Hardware {

    //Time based: doesn't account for skid
    //encoder: measures how many time the magnets pass, doesn't account for slip
    //odometry: corrects position and sees exact location

    //test

    public DcMotor rf;
    public DcMotor lf;
    public DcMotor rb;
    public DcMotor lb;

    public Servo demoServo;

    public static double maxSpeed = 0.9;

    private static Hardware myInstance = null;
    public static Hardware getInstance() {
        if (myInstance == null){
            myInstance = new Hardware();
        }
        return myInstance;
    }


    public void init(HardwareMap hwMap) {
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

        //Initialize servo

        demoServo = hwMap.get(Servo.class, "demoServo");
    }

    public void setPower(double fr, double br, double bl, double fl){

        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));
    }
}
