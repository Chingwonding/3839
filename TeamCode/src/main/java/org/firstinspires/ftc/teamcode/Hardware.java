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





    public DcMotor intake;

    //new shot motors for robot and one new servo
    public DcMotorEx shotMotorOne;

    public DcMotorEx shotMotorTwo;


    public Servo UpServo;

    public Servo servoTwo;
    //intake motors and servo


    //motor for shooting

    /*
    public DcMotor shotMotor;
    
    

    //servos
    public Servo shotServo;

    public Servo Servotwo;

    public Servo Servothree;

     */


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
        rf.setPower(0);
        //1

        rb = hwMap.get(DcMotorEx.class, "cm0");
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setPower(0);
        //3


        lf = hwMap.get(DcMotorEx.class, "em1");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setPower(0);
        //0

        lb = hwMap.get(DcMotorEx.class, "em2");
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setPower(0);

        //motor for shooting
        //I set it two em3 can change it later

        /*
        shotMotor = hwMap.get(DcMotorEx.class, "em3");
        shotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shotMotor.setPower(0);

        shotServo = hwMap.get(Servo.class, "es1");

        Servotwo = hwMap.get(Servo.class, "es2");

         */



        shotMotorOne = hwMap.get(DcMotorEx.class, "cm3");

        shotMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shotMotorOne.setDirection(DcMotorSimple.Direction.REVERSE);
        shotMotorOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shotMotorOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shotMotorOne.setPower(0);


        shotMotorTwo = hwMap.get(DcMotorEx.class, "em3");
        shotMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shotMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shotMotorTwo.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
        shotMotorTwo.setPower(0);

        intake = hwMap.get(DcMotorEx.class, "cm2");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setPower(0);
        //0

        //servos
        UpServo = hwMap.get(Servo.class, "s0");

        servoTwo = hwMap.get(Servo.class, "s1");

    }

    public void setPower(double fr, double br, double bl, double fl) {

        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));

    }



}
