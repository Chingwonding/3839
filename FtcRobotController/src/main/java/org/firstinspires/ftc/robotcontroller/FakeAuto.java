package org.firstinspires.ftc.robotcontroller;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class FakeAuto extends LinearOpMode {
    //instance variables

    //THIS CLASS SHOULD NEVER BE USED
    /*
    FROM GEARS AND GIZMOS
    JUST A TUTORIAL CLASS



     */
    //basically initiallizing the motors
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;

    private DcMotor backRight = null;

    //this will be the number of motors you have
    //so for our code specifically, not this tutorial, we
    //initialized these variables in the Hardware class (im pretty sure)
    //Remember DcMotor is a type of variable just like int or double


    private ElapsedTime runtime = new ElapsedTime();
    //initializing new time variable for calculating distance moved and stuff
    static final double COUNTS_PERMOTOR_REV = 537.6;
    //This is for a specific type of motor and is just for the demonstration
    //scaled for this specific wheel and motor I think

    static final double DRIVE_GEAR_REDUCTION = 1;

    static final double WHEEL_DIAMETER_INCHES = 4.094;

    static final double COUNTS_PER_INCHES = (COUNTS_PERMOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);
    //basically just calculating distance?
    // TotalRotations = Distance per rotation * Reduction of Drive Gear/ (circumference of the wheel)

    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    //this will be our main method
    public void runOpMode()
    {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");


        //method from hardwareMap...
        //I assume since it is hardware is a class, when we initialized them earlier,
        //the hardware class stores values and automatically stores the name as a string as an instance
        //especially since it is a .get method


        //but why do you need to set frontLeft to itself?
        //we previously set the motors to be null and only stored the variables
        //so here we set it to itself to remove the null part by using the class methods



        //on the same side (both right side)
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);



        //tell code to stop and reset the encoders so we can move again
        //we do this with all the wheels to set it as a clean slate and make new movements way easier
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //then we run
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //telemetry
        //what doe sthis even do bruh
        //what is %d
        telemetry.addData("Drive Motors", "frontLeft %d frontRight: %d backLeft: %d backRight: %d");
        telemetry.update();

        waitForStart();

        //I made up the last two integers since I couldn't see the board
        encoderDrive(1,1,1,1, 5, 5);

    }
    //public void encoderDrive(double speed, double frontLeftInches, double frontRightInches)
    public void encoderDrive(double speed, double frontLeftInches, double frontRightInches, double backLeftInches, double backRightInches, double timeoutS)
    {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        //what does this do?
        //ts is wrong?
        newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)(frontLeftInches);
        newFrontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches);
        newBackLeftTarget = backRight.getCurrentPosition() + (int)(backRightInches);
        newBackRightTarget = backLeft.getCurrentPosition() + (int)(backLeftInches);

        frontLeft.setTargetPosition(newFrontLeftTarget);
        frontRight.setTargetPosition(newFrontRightTarget);
        backRight.setTargetPosition(newBackRightTarget);
        backLeft.setTargetPosition(newBackLeftTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));
        backLeft.setPower(Math.abs(speed));

        //basically code about telemetry
        /*while (opModeIsActive() && runtime.seconds() && frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())
        {

        }
        */
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        sleep(250);




    }







}
