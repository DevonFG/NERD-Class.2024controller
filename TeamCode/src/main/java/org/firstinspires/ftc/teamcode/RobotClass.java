package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.checkerframework.checker.units.qual.degrees;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class RobotClass {
    private DcMotor motorFrontRight, motorFrontLeft, motorBackRight, motorBackLeft;
    private DcMotor intake, outtake;

    private DistanceSensor distanceSensor;

    private Servo bucket;
    private CRServo duck;

    //imu stuff
    private BNO055IMU imu;
    private Orientation angles, lastAngles, startAngles;
    private double globalAngle;

    private double kp = 1;//TODO Change AFTER you do PIDCalibration!!
    private double kd = 0;//TODO Change AFTER you do PIDCalibration!!

    //other variables
    private double gain = 0.1;
    double ticksperrev = ((((1+(46/17))) * (1+(46/11))) * 28);

    //Declare an opmode and a telemetry object
    private LinearOpMode opMode;
    private Telemetry telemetry;

    private final double DRIVE_WHEEL_CIRCUMFERENCE = 9.6 * Math.PI;
    private final double MOTOR_RPM = 312;
    private final double MOTOR_SPR = 60/MOTOR_RPM;//CHANGE
    private final double SECONDS_PER_CM = MOTOR_SPR/DRIVE_WHEEL_CIRCUMFERENCE;
    private final double TICKS_PER_IN =34.225;//don't change..... actually is ticks_per_in....

    private static double lastCm = 0;
    //also ^^ isn't used except once...

    //setup
    /**
     * Full Constructor
     * @param motorFrontRight
     * @param motorFrontLeft
     * @param motorBackRight
     * @param motorBackLeft
     * @param intake
     * @param outtake
     * @param bucket
     * @param duck
     * @param distanceSensor
     * @param imu
     * @param opMode
     * */
    public RobotClass(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft,
                      DcMotor intake, DcMotor outtake, Servo bucket, CRServo duck, DistanceSensor distanceSensor, BNO055IMU imu, LinearOpMode opMode){
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
        this.intake = intake;
        this.outtake = outtake;
        this.bucket = bucket;
        this.duck = duck;
        this.distanceSensor = distanceSensor;
        this.imu = imu;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
    }

    /**
     * Constructor without outtake
     * @param motorFrontRight
     * @param motorFrontLeft
     * @param motorBackRight
     * @param motorBackLeft
     * @param intake
     * @param duck
     * @param imu
     * @param opMode
     * */
    public RobotClass(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft,
                      DcMotor intake, CRServo duck, BNO055IMU imu, LinearOpMode opMode){
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
        this.intake = intake;
        this.duck = duck;
        this.imu = imu;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
//        ticksperrev = motorFrontLeft.getMotorType().getTicksPerRev();//((((1+(46/17))) * (1+(46/11))) * 28)
    }

    /**
     * Base only
     * @param motorFrontRight
     * @param motorFrontLeft
     * @param motorBackRight
     * @param motorBackLeft
     * @param imu
     * @param opMode
     * */
    public RobotClass(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft,
                      BNO055IMU imu, LinearOpMode opMode){
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
        this.imu = imu;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
    }

    /**
     * Base only + duck
     * @param motorFrontRight
     * @param motorFrontLeft
     * @param motorBackRight
     * @param motorBackLeft
     * @param duck
     * @param imu
     * @param opMode
     * */
    public RobotClass(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft,
                      CRServo duck, BNO055IMU imu, LinearOpMode opMode){
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
        this.duck = duck;
        this.imu = imu;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
    }

    /**
     * Base only + outtake
     * @param motorFrontRight
     * @param motorFrontLeft
     * @param motorBackRight
     * @param motorBackLeft
     * @param motorOuttake
     * @param imu
     * @param opMode
     * */
    public RobotClass(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft,
                      DcMotor motorOuttake, BNO055IMU imu, LinearOpMode opMode){
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
        this.outtake = motorOuttake;
        this.imu = imu;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
    }

    /**
     * Get IMU parameters
     * @return IMU parameters
     * */
    private BNO055IMU.Parameters getIMUParameters(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        parameters.mode                = BNO055IMU.SensorMode.IMU;

        return parameters;
    }

    /**
     * Setup and initialize IMU parameters
     * */
    private void setIMUParameters(){
        BNO055IMU.Parameters parameters = getIMUParameters();
        imu.initialize(parameters);
    }

    /**
     * Set directions and behaviors of motors, calibrates IMU
     * @throws InterruptedException if robot stopped when IMU is calibrating
     * */
    public void setupRobot() throws InterruptedException{
        //reverse the needed motors?
        // motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
//       motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //note: Outtake is the only that can seup herebecause we don't want to continuously reset encoders
        outtake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        setIMUParameters();
        resetEncoders();
        resetAngle();


        while (!imu.isGyroCalibrated()) {
            telemetry.addData("IMU", "calibrating...");
            telemetry.update();
            Thread.sleep(50);
        }

        telemetry.addData("IMU", "ready");
        telemetry.update();

    }

    public void runToPosSetupRobot() throws InterruptedException{
        //reverse the needed motors?
        //motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outtake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtake.setTargetPosition(0);
        outtake.setPower(0);
        outtake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setIMUParameters();
        resetEncoders();
        resetAngle();


        while (!imu.isGyroCalibrated()) {
            telemetry.addData("IMU", "calibrating...");
            telemetry.update();
            Thread.sleep(50);
        }

        telemetry.addData("IMU", "ready");
        telemetry.update();

    }
    
    /**
     * Reset motor encoders
     */
    public void resetEncoders(){
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Reset angle measurement
     * */
    public void resetAngle() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        startAngles = lastAngles;
        globalAngle = 0;
    }

    //imu stuff
    /**
     * Get change in angle since last reset
     * @return angle in degrees (+ CCW, - CW)
     */
    public double getAngle(){
        //Get a new angle measurement
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //Get the difference between current angle measurement and last angle measurement
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        //Process the angle to keep it within (-180,180)
        //(Once angle passes +180, it will rollback to -179, and vice versa)
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        //Add the change in angle since last measurement (deltaAngle)
        //to the change in angle since last reset (globalAngle)
        globalAngle += deltaAngle;
        //Set last angle measurement to current angle measurement
        lastAngles = angles;

        return globalAngle;
    }

    /**
     * Create telemetry with angle after last reset, current angle, and change since last reset
     */
    private void composeAngleTelemetry(){
        telemetry.addData("Start Angle", startAngles.firstAngle);
        telemetry.addData("Current Angle", angles.firstAngle);
        telemetry.addData("Global Angle", globalAngle);
    }

    //robot motion
    /**
     * Set motor powers to 0 (complete stop)
     */
    public void completeStop(){
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    /**
     * Tank drive turn CW at set power
     * @param power power for motors
     * */
    public void turnClockwise(double power){
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(-power);
        motorFrontRight.setPower(power);
        motorBackRight.setPower(-power);
    }

    /**
     *Tank drive turn CCW at set power
     * @param power power for motors
     */
    public void turnCounterClockwise(double power){
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(power);
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(power);
    }

    /**
     * Generic turn function, works for CW and CCW
     * @param power power for motors, + is CW, - is CCW
     */
    public void turn(double power){
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(-power);
    }

    /**
     * Make precise turn using gyro
     * Old version
     * */
    public void gyroTurn(int degrees, double power) throws InterruptedException{
        //restart angle tracking
        resetAngle();

        if(degrees > 0){
            turnClockwise(power);
        }else if(degrees < 0){
            turnCounterClockwise(power);
        }else{
            return;
        }

        //Rotate until current angle is equal to the target angle
        if (degrees < 0){
            while (opMode.opModeIsActive() && getAngle() > degrees+10){
                composeAngleTelemetry();
                //display the target angle
                telemetry.addData("Target angle", degrees);
                telemetry.update();
            }
        }else{
            while (opMode.opModeIsActive() && getAngle() < degrees-10) {
                composeAngleTelemetry();
                //display the target angle
                telemetry.addData("Target angle", degrees);
                telemetry.update();
            }
        }

        completeStop();
        //Wait .5 seconds to ensure robot is stopped before continuing
        Thread.sleep(500);
        resetAngle();
    }

    /**
     * Precise turn with gyro and PD tuner (no I)
     * Power is calculated within function
     * @param degrees to turn
     * */
    public void pidGyroTurn(int degrees) throws InterruptedException{
        //restart angle tracking
        resetAngle();

        boolean clockwise;//flag for determining if clockwise or not
        if(degrees < 0){
            clockwise = true;
        }else if(degrees > 0){
            clockwise = false;
        }else{//already reached angle!:)
            return;
        }
        //values needed for pd tuner
        double angle = getAngle();
        double error = angle-degrees;
        double lastError = 0;
        double derivative;
        double power;
        ElapsedTime timer = new ElapsedTime();//to measure time
        timer.reset();

        //tolerance can be adjusted
        double tolerance = 10;

        while(opMode.opModeIsActive() && Math.abs(error) > tolerance){
            composeAngleTelemetry();
            telemetry.addData("Target angle", degrees);
            telemetry.update();
            angle = getAngle();
            error = angle-degrees;//find error

            derivative = (error - lastError)/timer.seconds();//find change in error
            //there's a turn function, generic
            power = kp*error + kd*derivative;
            //turn accordingly
            if(clockwise){
                //negative
                power = power<= -1? power : -1;
            }
            else{
                //positive
                power = power<= 1? power : 1;
            }
            turn(power);///////////////////////////////////////

            lastError = error;
            timer.reset();
        }

        completeStop();
        //Wait .5 seconds to ensure robot is stopped before continuing
        Thread.sleep(500);
        resetAngle();
    }

    /**
     * Check if robot is moving in straight line. If not, then get a power correction
     * @return correction, +=CW, -=CCW
     * */
    public double getCorrection(){
        //Get the current angle of the robot
        double angle = getAngle();

        //Use the angle to calculate the correction
        if (angle == 0){
            //If angle = 0, robot is moving straight; no correction needed
            return 0;
        }else{
            //If angle != 0, robot is not moving straight
            //Correction is negative angle (to move the robot in the opposite direction)
            //multiplied by gain; the gain is the sensitivity to angle
            //We have determined that .1 is a good gain; higher gains result in overcorrection
            //Lower gains are ineffective
            return -angle*gain;
        }
    }

    /**
     * Straight Mecanum drive (no turns, allows strafes)
     * @param leftPower, for frontleft and backright
     * @param rightPower, for frontright and backleft
     * */
    public void tankDrive(double leftPower, double rightPower){
        motorFrontLeft.setPower(leftPower);
        motorFrontRight.setPower(rightPower);
        motorBackLeft.setPower(rightPower);
        motorBackRight.setPower(leftPower);
    }

    /**
     * Straight mecanum drive with correction
     * @param leftPower for front left and back right
     * @param rightPower for front right and back left
     * @param correction
     * */
    public void correctedTankStrafe(double leftPower, double rightPower, double correction){
        motorFrontLeft.setPower(leftPower + correction);
        motorFrontRight.setPower(rightPower - correction);
        motorBackLeft.setPower(rightPower + correction);
        motorBackRight.setPower(leftPower - correction);
    }

    /**
     * Gyro Drive using seconds and power
     * */
    public void gyroDriveSec(double power, double seconds) throws InterruptedException{
        //restart angle tracking
        resetAngle();

        //create an ElapsedTime object to track the time the robot moves
        ElapsedTime timer = new ElapsedTime();
        //restart time tracking
        timer.reset();

        //drive straight with gyro until timer reaches number of given seconds
        while(timer.seconds() < seconds && opMode.opModeIsActive()){
            //Get a correction
            double correction = getCorrection();
            //Use the correction to adjust robot power so robot drives straight
            tankDrive(power + correction, power - correction);
        }
        completeStop();
        //Wait .5 seconds to ensure robot is stopped before continuing
        Thread.sleep(500);
        resetAngle();
    }

    /**
     * Gyro drive using distance and power
     * @param in distance in centimeters
     */
    public void gyroDriveIn(double power, double in) throws InterruptedException{
        gyroDriveSec(power, (in*SECONDS_PER_CM)/Math.abs(power));
    }

    /**
     * PD tuned Gyro drive with power and distance
     * Straight drive, no angles
     * forwards (+) and backwards (-) only
     * Power is calculated in the function
     * @param in distance to travel
     */
    public void pidGyroDriveIn(double in) throws InterruptedException{
        resetAngle();
        resetEncoders();

        double dist = getDistanceTraveled();
        double error = in-dist;
        double lastError = 0;
        double derivative;
        double correction;
        double power;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(opMode.opModeIsActive() && Math.abs(error) > 0){
            telemetry.addData("current position",dist);
            telemetry.update();

            dist = getDistanceTraveled();
            error = in-dist;

            derivative = (error-lastError) / timer.seconds();
            correction = getCorrection();
            double abspow = Math.abs(kp*error+kd*derivative);
            power = abspow <= 1? kp*error+kd*derivative : abspow/(kp*error+kd*derivative);
            tankDrive(power+correction, power-correction);

            lastError = error;
            timer.reset();
        }
        completeStop();
        resetAngle();
    }

    /**
     * Drives forward using encoders and gyro (uses gyroStrafe)
     * @param power
     * @param in inches
     * @throws InterruptedException if robot is stopped
     */
    public void gyroDriveEncoder(double power, double in) throws InterruptedException{
        setNewGain(0.05);
        gyroStrafeEncoder(power, 0, in);
    }

    /**
     * Strafe indefinite amount of time in any direction
     * @param power
     * @param angle Direction to strafe (0 = forward, 180 = backward)
     * @throws InterruptedException if robot is stopped
     */
    public void gyroStrafe(double power, double angle) throws InterruptedException{
        //restart angle tracking
        resetAngle();

        //convert direction (degrees) into radians
        double newDirection = angle * Math.PI/180 + Math.PI/4;
        //calculate powers needed using direction
        double leftPower = Math.cos(newDirection) * power;
        double rightPower = Math.sin(newDirection) * power;

        //while(opMode.opModeIsActive()){
        //Get a correction
        double correction = getCorrection();
        //Use the correction to adjust robot power so robot faces straight
        correctedTankStrafe(leftPower, rightPower, correction);
        //}
    }

    /**
     * Strafe in any direction using gyro to keep robot facing straight forward
     * @param power power
     * @param angle direction to strafe, in degrees (0 = forward, 180 = backward)
     * @param seconds time to run
     * @throws InterruptedException if the robot is stopped
     */
    public void gyroStrafeSec(double power, double angle, double seconds) throws InterruptedException{
        //restart angle tracking
        resetAngle();

        //convert direction (degrees) into radians
        double newDirection = angle * Math.PI/180 + Math.PI/4;
        //calculate powers needed using direction
        double leftPower = Math.cos(newDirection) * power;
        double rightPower = Math.sin(newDirection) * power;

        //create an ElapsedTime object to track the time the robot moves
        ElapsedTime timer = new ElapsedTime();
        //restart time tracking
        timer.reset();

        //strafe using gyro to keep robot facing straight for
        while(timer.seconds() < seconds && opMode.opModeIsActive()){
            telemetry.addData("moving in direction",angle);
            telemetry.addData("moving for ",seconds);
            telemetry.addData("current time:",timer.seconds());
            //Get a correction
            double correction = getCorrection();
            //Use the correction to adjust robot power so robot faces straight
            tankDrive(leftPower, rightPower);
        }
        completeStop();
        //Wait .5 seconds to ensure robot is stopped before continuing
        Thread.sleep(500);
        resetAngle();
    }

    /** Drive to wall, straight
     *
     */
    public void driveToWall(double power) throws InterruptedException{
        resetAngle();
        motorBackLeft.setPower(-1);
        motorBackRight.setPower(-1);
        motorFrontLeft.setPower(-1);
        motorFrontRight.setPower(-1);

        while(distanceSensor.getDistance(DistanceUnit.CM) > 30){
            // double correction = getCorrection();
//            correctedTankStrafe(power, power, 0);//??????????

            telemetry.addData("distance reading:", distanceSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
        completeStop();
        Thread.sleep(500);
        resetAngle();
    }
    /**
     * Strafe in any direction using gyro to keep robot facing forward. Strafes a certain distance
     * */
    public void gyroStrafeIn(double power, double angle, double in) throws InterruptedException{
        gyroStrafeSec(power, angle, (in*SECONDS_PER_CM)/power);
    }

    /**
     * PID controlled strafe in any direction using gyro to keep robot facing straight forward
     * Power is calculated in the function
     * @param angle direction to strafe, in degrees (0 = forward, 180 = backward)
     * @param in distance to go
     * @throws InterruptedException if the robot is stopped
     */
    public void pidGyroStrafeIn(double angle, double in) throws InterruptedException{
        resetAngle();
        resetEncoders();
        //set gain???

        double newDirection = angle * Math.PI/180 + Math.PI/4;

        //calculate powers needed using direction
        double leftPower;
        double rightPower;

        double dist = getDistanceTraveled();
        double error = in-dist;
        double lastError = 0;
        double derivative = 0;
        double correction = getCorrection();
        double power;
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(opMode.opModeIsActive() && Math.abs(error) > 0) {
            telemetry.addData("current position",dist);
            telemetry.update();
            dist = getDistanceTraveled();
            error = in-dist;

            derivative = (error-lastError) / timer.seconds();
            correction = getCorrection();
            double abspow = Math.abs(kp*error+kd*derivative);
            power = abspow <= 1? kp*error+kd*derivative : abspow/(kp*error+kd*derivative);
            leftPower = Math.cos(newDirection) * power;
            rightPower = Math.sin(newDirection) * power;
            correctedTankStrafe(leftPower, rightPower, correction);

            lastError = error;
            timer.reset();
        }
        completeStop();
        //Wait .5 seconds to ensure robot is stopped before continuing
        Thread.sleep(500);
        resetAngle();
        resetEncoders();
    }

    /**
     * PID Tuner Method
     * Copy the values after using tuner!!!
     * @param angle direction to strafe, in degrees (0 = forward, 180 = backward)
     * @param in distance to go
     * @throws InterruptedException if the robot is stopped
     */
    public void pidTunerStrafe(double angle, double in) throws InterruptedException{
        if(in != lastCm) {
            resetAngle();
            resetEncoders();
            lastCm = in;
        }
        //set gain???

        double newDirection = angle * Math.PI/180 + Math.PI/4;

        //calculate powers needed using direction
        double leftPower;
        double rightPower;
        int targetticks = distanceToTicks(in);
        motorBackLeft.setTargetPosition(targetticks);
        motorFrontLeft.setTargetPosition(targetticks);
        motorBackRight.setTargetPosition(targetticks);
        motorFrontRight.setTargetPosition(targetticks);

        double kp = PIDCalibration.getKp();//Todo
        double kd = PIDCalibration.getKd();//todo


        double dist = motorFrontLeft.getCurrentPosition();
        double error = (targetticks-dist)/Math.abs(targetticks);
        double lastError = 0;
        double derivative;
        double correction;
        double power;
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        double tolerance = 0.20;
        while(opMode.opModeIsActive() && Math.abs(error) > tolerance) {
            dist = motorFrontLeft.getCurrentPosition();
            error = (targetticks-dist)/Math.abs(targetticks);
            derivative = (lastError-error) / timer.seconds()/Math.abs(error);
            correction = getCorrection();
            double abspow = Math.abs(kp*error+kd*derivative);
            power = kp*error+kd*derivative;// abspow <= 1? kp*error+kd*derivative : abspow/(kp*error+kd*derivative);
            leftPower = Math.cos(newDirection) * power;
            rightPower = Math.sin(newDirection) * power;
            tankDrive(power+correction, power-correction);
            telemetry.addData("distance traveled==current position",dist);
//            telemetry.addData("ticks per rev",((((1+(46/17))) * (1+(46/11))) * 28));
            telemetry.addData("target ticks",targetticks);
//            telemetry.addData("p",power);
//            telemetry.addData("correction",correction);
//            telemetry.addData("current position",dist);
            telemetry.addData("error", error);
            telemetry.addData("last error", lastError);
//            telemetry.addData("circumference", DRIVE_WHEEL_CIRCUMFERENCE);
            telemetry.update();
            lastError = error;
            timer.reset();
        }
        completeStop();
        //Wait .5 seconds to ensure robot is stopped before continuing
        Thread.sleep(500);
        resetAngle();
        resetEncoders();
        dist = getDistanceTraveled();
        telemetry.addData("current position",dist);
        telemetry.update();
    }
    /**
     * Strafe in any direction using encoders.
     * sorta closed loop...
     * @param power
     * @param angle Direction to strafe (0 = forward, 180 = backward)
     * @param in
     * @throws InterruptedException if robot is stopped
     */
    public void gyroStrafeEncoder(double power, double angle, double in) throws InterruptedException{
        double ticks = in * TICKS_PER_IN;

        //convert direction (degrees) into radians
        double newDirection = angle * Math.PI/180 + Math.PI/4;
        //calculate powers needed using direction
        double leftPower = Math.cos(newDirection) * power;
        double rightPower = Math.sin(newDirection) * power;

        resetEncoders();
        resetAngle();
        setNewGain(0.02);
        while(Math.abs(motorFrontLeft.getCurrentPosition()) < ticks && opMode.opModeIsActive()){
            // telemetry.addData("Target ticks", ticks);
            // telemetry.addData("Current ticks", Math.abs(motorFrontLeft.getCurrentPosition()));
            // telemetry.update();

            double correction = getCorrection();
            correctedTankStrafe(leftPower, rightPower, correction);
        }
        completeStop();
        Thread.sleep(250);
        resetAngle();
        resetEncoders();
    }
    /**
     * Set new gain
     * @param newGain
     * */
    public void setNewGain(double newGain){
        gain = newGain;
    }

    /**
     * Get distanced travelled
     * @return Absolute value of Current position of front left motor, in ticks
     */
    public double getDistanceTraveled() {
        return (motorFrontLeft.getCurrentPosition() / ticksperrev) * DRIVE_WHEEL_CIRCUMFERENCE * 6;
    }

    public int distanceToTicks(double in){//CHANGE!!!!
        return (int)((in/DRIVE_WHEEL_CIRCUMFERENCE)*ticksperrev);
    }

    public void goToWarehouse_Red(boolean goOverBarrier) throws InterruptedException{
        if(!goOverBarrier) {
            //go to warehouse
            gyroTurn(-90, 0.5);
            gyroStrafeEncoder(0.5, 180, 28);//change distance
            gyroStrafeEncoder(0.5, 90, 56);
            // driveToWall(0.5);//chnage
            //option 2: there's a robot in the way, and we need to instead go over the bumps...
        }
        else{
            gyroTurn(90, 0.5);
            gyroStrafeEncoder(0.5, -90, 56);//???
            // driveToWall(0.5);
        }
    }

    public void goToWarehouse_Blue(boolean goOverBarrier) throws InterruptedException{
        if(!goOverBarrier) {
            //go to warehouse
            gyroTurn(-90, 0.5);
            gyroStrafeEncoder(0.5, 0, 28);//change distance
            gyroStrafeEncoder(0.5, -90, 56);
//            driveToWall(0.5);
            //option 2: there's a robot in the way, and we need to instead go over the bumps...
        }
        else{
            gyroTurn(-90, 0.5);
            gyroStrafeEncoder(0.5, -90, 56);//???
//            driveToWall(0.5);
        }
    }

    public void goToDepot_Red() throws InterruptedException{
        gyroTurn(90,0.5);
        gyroStrafeEncoder(0.5,90,54);//54
        // driveToWall(0.5);//other option
        gyroStrafeEncoder(0.5,180,12.7);
    }

    public void goToDepot_Blue() throws InterruptedException{
        gyroTurn(90,0.5);
        gyroStrafeEncoder(0.5,90,54);//54
//        robot.driveToWall(0.5);//other option
        gyroStrafeEncoder(0.5,0,12.7);
    }
    //robot components
    /**
     * Set intake power
     * @param power, + to intake, - to undo intake, 0 to stop
     */
    public void intake(double power) {
        intake.setPower(power); //changed power to 1.00 from "power"
    }

    /**
     * Set duck power
     * @param power, + to drop duck, 0 to stop
     * */
    public void duck(double power){
        duck.setPower(power); //changed power to 1.00 from "power"
    }

    public void dobucket() throws InterruptedException{
        bucket.setPosition(1);
        Thread.sleep(1000);
        //turn back
        bucket.setPosition(0.4);
        Thread.sleep(500);
    }
    /**
     * Move arm to position...?
     *
     */
    public void moveSlides(int level, double power) throws InterruptedException{
        int targetTicks;
        switch (level){
            case 1:
                //bottom
                targetTicks = 450;
                break;
            case 2:
                //middle
                targetTicks = 890;
                break;
            case 3:
                //top
                targetTicks = 1390;
                break;
            default:
                targetTicks = 30;
        }
        if(outtake.getCurrentPosition() < targetTicks) {
            outtake.setPower(power);
            while (outtake.getCurrentPosition() < targetTicks && opMode.opModeIsActive());
            outtake.setPower(0);
        }
        else{
            outtake.setPower(-power);
            while(outtake.getCurrentPosition() > targetTicks && opMode.opModeIsActive());
            outtake.setPower(0);
        }
    }

    public void rTPMoveSlides (int level, double power) throws InterruptedException {
        int targetTicks;
        switch (level) {
            case 1:
                //bottom
                targetTicks = 450;
                break;
            case 2:
                //middle
                targetTicks = 890;
                break;
            case 3:
                //top
                targetTicks = 1390;
                break;
            default:
                targetTicks = 30;
        }
        motorRunToPos(power, targetTicks);
        
    }
    public void autoDrop(int level, double power, double dist) throws InterruptedException{
        bucket.setPosition(0.7);
        moveSlides(level, power);
        gyroStrafeEncoder(0.5,90,dist);
        dobucket();
        gyroStrafeEncoder(0.5,-90,6);
        moveSlides(0,power);
    }

    public void motorRunToPos (double power, double dist) {
        outtake.setPower(power);
        outtake.setTargetPosition((int)dist);
                
        if(outtake.getCurrentPosition() < dist){
            while(outtake.getCurrentPosition() < dist);
        } 
        else{
            while(outtake.getCurrentPosition() > dist);
        }
        outtake.setPower(0);
    }

    public void runToPosDrop (double power, double dist, int level) throws InterruptedException {
        bucket.setPosition(0.7);
        int targetTicks;
        switch (level) {
            case 1:
                //bottom
                targetTicks = 450;
                break;
            case 2:
                //middle
                targetTicks = 890;
                break;
            case 3:
                //top
                targetTicks = 1390;
                break;
            default:
                targetTicks = 30;
        }
        motorRunToPos(power, targetTicks);
        gyroStrafeEncoder(0.5, 90, dist);
        dobucket();
        gyroStrafeEncoder(0.5, -90, 6);
        motorRunToPos(power, 100);
    }


}
