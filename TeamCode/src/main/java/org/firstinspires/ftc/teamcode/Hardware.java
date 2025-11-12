package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Hardware {


    public DcMotor rf;
    public DcMotor lf;
    public DcMotor rb;
    public DcMotor lb;

    //intake motors and servo


    //motor for shooting
    public DcMotor shotMotor;
    
    

    //servos
    public Servo shotServo;

    public Servo Servotwo;

    public Servo Servothree;


    public static double maxSpeed = 0.9;

    private static Hardware myInstance = null;
    public static Hardware getInstance() {

        if (myInstance == null) {
            myInstance = new Hardware();
        }
        return myInstance;
    }


    public void init(HardwareMap hwMap) {

        rf = hwMap.get(DcMotorEx.class, "cm1");
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setPower(0);
        //1

        rb = hwMap.get(DcMotorEx.class, "cm0");
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setPower(0);
        //3

        lf = hwMap.get(DcMotorEx.class, "em1");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lf.setPower(0);
        //0

        lb = hwMap.get(DcMotorEx.class, "em2");
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setPower(0);

        //motor for shooting
        //I set it two em3 can change it later
        shotMotor = hwMap.get(DcMotorEx.class, "em3");
        shotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shotMotor.setPower(0);

        shotServo = hwMap.get(Servo.class, "es1");

        Servotwo = hwMap.get(Servo.class, "es2");

        //Servothree = hwMap.get(Servo.class, "es0");
    }

    public void setPower(double fr, double br, double bl, double fl) {

        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));

    }



}
