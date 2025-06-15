package org.firstinspires.ftc.teamcode.opmode.subsystem

import com.escapevelocity.ducklib.core.command.subsystem.Subsystem
import com.escapevelocity.ducklib.core.geometry.Pose2
import com.escapevelocity.ducklib.core.geometry.Radians
import com.escapevelocity.ducklib.core.geometry.Vector2
import com.escapevelocity.ducklib.ftc.extensions.HardwareMapEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

class Drivetrain(map: HardwareMapEx) : Subsystem() {
    val flMotor by map.deferred<DcMotor>("frontLeft") {
        this.direction = DcMotorSimple.Direction.REVERSE
    }
    val frMotor by map.deferred<DcMotor>("frontRight")
    val blMotor by map.deferred<DcMotor>("backLeft") {
        this.direction = DcMotorSimple.Direction.REVERSE
    }
    val brMotor by map.deferred<DcMotor>("backRight")

    fun drive(power: Pose2) {
        val (x, y, h) = power.xyh
        flMotor.power = x.inches - y.inches - h.radians
        frMotor.power = x.inches + y.inches + h.radians
        blMotor.power = x.inches + y.inches - h.radians
        brMotor.power = x.inches - y.inches + h.radians
    }

    fun drive(translationPower: Vector2, headingPower: Radians) =
        drive(Pose2(translationPower, headingPower))
}