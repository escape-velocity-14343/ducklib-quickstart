@file:Suppress("PackageDirectoryMismatch")

package org.firstinspires.ftc.teamcode

import com.escapevelocity.ducklib.core.command.subsystem.Subsystem
import com.escapevelocity.ducklib.core.geometry.Pose2
import com.escapevelocity.ducklib.core.geometry.Radians
import com.escapevelocity.ducklib.core.geometry.Vector2
import com.escapevelocity.ducklib.ftc.extensions.HardwareMapEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE

class DrivetrainSubsystem(map: HardwareMapEx) : Subsystem() {
    val flMotor by map.deferred<DcMotor>("frontLeft") {
        this.direction = REVERSE
    }
    val frMotor by map.deferred<DcMotor>("frontRight")
    val blMotor by map.deferred<DcMotor>("backLeft") {
        this.direction = REVERSE
    }
    val brMotor by map.deferred<DcMotor>("backRight")

    fun drive(power: Pose2) {
        val (x, y, h) = power.xyh
        flMotor.power = x.v - y.v - h.v
        frMotor.power = x.v + y.v + h.v
        blMotor.power = x.v + y.v - h.v
        brMotor.power = x.v - y.v + h.v
    }

    fun drive(translationPower: Vector2, headingPower: Radians) =
        drive(Pose2(translationPower, headingPower))
}
