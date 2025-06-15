package org.firstinspires.ftc.teamcode.opmode.subsystem

import com.escapevelocity.ducklib.control.P
import com.escapevelocity.ducklib.control.SquIDController
import com.escapevelocity.ducklib.control.pipe
import com.escapevelocity.ducklib.core.command.subsystem.Subsystem
import com.escapevelocity.ducklib.core.geometry.inches
import com.escapevelocity.ducklib.ftc.extensions.HardwareMapEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

class Extension(map: HardwareMapEx) : Subsystem() {
    private val motor0 by map.deferred<DcMotorEx>("slide0") {
        direction = DcMotorSimple.Direction.REVERSE
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }
    private val motor1 by map.deferred<DcMotorEx>("slide1") {
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }

    var extension = 0.inches

    private val controller =
        { motor0.currentPosition.toDouble() } pipe SquIDController(P(ExtensionConstants.kP)) { extension.inches } pipe {
            //it + SlideConstants.kP +
        }

    override fun periodic() {
        super.periodic()
    }
}


object ExtensionConstants {
    var kP: Double = 0.004
    var kI: Double = 0.0
    var kD: Double = 0.0
    var kS: Double = 0.13
    var ticksPerInch: Double =
        54.9 / 1.5555555555555556
    var maxExtension = 31.0.inches

    var autonBucketPos: Double = 30.0
    var bucketPos: Double = 31.0
    var lowBucketPos: Double = 11.5

    var minExtension: Double = 0.0
    var direction: Double = 1.0
    var tolerance: Double = 1.0
    var alertCurrent: Double = 4.0

    /** Feedforward value that is multiplied by `Math.cos(slideAngle)`  */
    var FEEDFORWARD_bottom: Double = 0.07

    var FEEDFORWARD_top: Double = 0.12 // TUNED VALUE

    var submersibleIntakeMinExtension: Double = 10.0
    var submersibleIntakeMidExtension: Double = 20.0
    var submersibleIntakeMaxExtension: Double = 27.0
    var submersibleIntakeGroundMaxExtension: Double = 22.0

    var specimenRaisePosition: Double = 0.0
    var specimenHighRaisePosition: Double = 13.0
    var specimenHookPosition: Double = 16.75

    var millisPerInch: Double = 5 * ticksPerInch

    var highExtendInches: Double = 1.5
    var lowExtendInches: Double = -1.0
    var extendedThreshold: Double = 3.0
    // It takes 0.4 seconds from scoring pos reversed to scoring Pos
    var pivotDownExtension: Double = 15.0

    var safeForDunk: Double = 26.0

    var manualControlDeadband: Double = 0.1
    var highExtend: Boolean = false
    var lowExtend: Boolean = false
}