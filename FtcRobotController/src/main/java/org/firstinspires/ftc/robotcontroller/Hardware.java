package org.firstinspires.ftc.robotcontroller;

//check if this is right
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Hardware {

    //time based: uses time, does not account for slippage
    //encoder: counts ticks to get to position, slippage
    //odometry: corrects position


    //instance variables
    public DcMotor rf;
    //Dcmotor is a variable type
    public DcMotor lf;

    public DcMotor rb;

    public DcMotor lb;
    public Servo demoServo;

    public static double maxSpeed = 0.9;
    //this is the speed

    private static Hardware myInstance = null;
    //yellow squiggles do not matter (just a better way to write something)


    //create method
    public static Hardware getInstance() {
        if (myInstance == null) {
            myInstance = new Hardware();
        }
        //need return
        return myInstance;

    }


    public void init(HardwareMap hwMap) {
        //defining variable
        rf = hwMap.get(DcMotor.class, "rf");
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setPower(0);
        //motor goes by gearbox by encoder
        //other thing goes by coordinate
        lf = hwMap.get(DcMotor.class, "rf");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setPower(0);


        rb = hwMap.get(DcMotor.class, "rf");
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setPower(0);
        lb = hwMap.get(DcMotor.class, "rf");
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setPower(0);
        //initialize servo
        demoServo = hwMap.get(Servo.class, "demoServo");
    }
    public void setPower(double fr, double br, double bl, double fl)
    {

        //fr = front right br = back right...etc
        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(fr, -maxSpeed, maxSpeed));


    }

}