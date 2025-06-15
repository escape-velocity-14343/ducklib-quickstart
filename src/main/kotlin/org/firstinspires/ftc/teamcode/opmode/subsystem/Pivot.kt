package org.firstinspires.ftc.teamcode.opmode.subsystem

import com.escapevelocity.ducklib.core.command.subsystem.Subsystem
import com.escapevelocity.ducklib.core.geometry.Inches
import com.escapevelocity.ducklib.core.geometry.degrees
import com.escapevelocity.ducklib.core.geometry.radians
import com.escapevelocity.ducklib.core.util.deriv
import com.escapevelocity.ducklib.core.util.startTimer
import com.escapevelocity.ducklib.ftc.drivers.sensorange.SensOrangeAbsoluteEncoder
import com.escapevelocity.ducklib.ftc.extensions.HardwareMapEx
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

class Pivot(map: HardwareMapEx, private val extension: () -> Inches) : Subsystem() {
    private val motor0 by map.deferred<DcMotorEx>("tilt0") {
        direction = DcMotorSimple.Direction.REVERSE
    }
    private val motor1 by map.deferred<DcMotorEx>("tilt1")

    private val encoder by map.deferred<SensOrangeAbsoluteEncoder>("sensOrange")
    private var timer = startTimer()

    var target = 0.0.radians
    var currentPos = 0.0.radians
        private set
    var velocity = 0.0
        private set

    fun setPower(power: Double) {
        motor0.power = power
        motor1.power = power
    }

    override fun periodic() {
        val lastPos = currentPos
        currentPos = encoder.angle
        velocity = timer.deriv(currentPos.radians, lastPos.radians)
        timer = startTimer()
    }
}

object PivotConstants {
    var maxVelocity: Double = 4.0
    var maxAcceleration: Double = 4.0

    var kPRetracted: Double = -0.05
    var kPExtended: Double = 0.06
    var kS: Double = 0.0
    var kD: Double = -0.2
    var kGRetracted: Double = 0.0
    var debugGain: Double = 0.0
    var kGExtended: Double = 0.0

    var maxPivotVelocity: Double = 1.0

    var bottomLimit = 0.5.degrees
    var topLimit = 86.0.degrees
    var stallTopLimit = 88.5.degrees
    var tolerance = 3.0.degrees
    var direction = -1.0
    var encoderInvert = true
    var encoderOffset = 170.2.radians
    var outtakeExtendPosition = 45.0.degrees
    var autoOuttakeExtendPosition = 30.0.degrees
    var retractPosition = bottomLimit

    var neutralPos = 25.0.degrees
    var intakeReadyPos = 15.0.degrees
    var intakePos = 10.0.degrees

    var specimenIntakeAngle = topLimit
    var specimenTopBarAngle = 63.0.degrees

    var manualControlDeadband: Double = 0.1

    var bottomPGain: Double = 1.25
}
