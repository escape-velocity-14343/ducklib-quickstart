package org.firstinspires.ftc.teamcode.opmode.subsystem

import com.escapevelocity.ducklib.core.command.subsystem.Subsystem
import com.escapevelocity.ducklib.core.geometry.Pose2
import com.escapevelocity.ducklib.core.geometry.inches
import com.escapevelocity.ducklib.core.geometry.mm
import com.escapevelocity.ducklib.core.geometry.radians
import com.escapevelocity.ducklib.ftc.drivers.GoBildaPinpoint
import com.escapevelocity.ducklib.ftc.extensions.HardwareMapEx

class Pinpoint(map: HardwareMapEx) : Subsystem() {
    private val p by map.deferred<GoBildaPinpoint>("pinpoint") {
        initialize()
        recalibrateIMU()
        setEncoderResolution(GoBildaPinpoint.GoBildaOdometryPods.GOBILDA_4_BAR_POD)
        setEncoderDirections(
            GoBildaPinpoint.EncoderDirection.REVERSED, GoBildaPinpoint.EncoderDirection.FORWARD
        )
        xOffset = 65.mm
        yOffset = 45.mm
    }

    private var lastGoodPose = Pose2.ZERO
    private var lastPose: Pose2? = null
    val pose
        get() = lastPose ?: lastGoodPose
    val velocity
        get() = p.vel

    override fun periodic() {
        p.update()
        if (p.pose.x.inches.isNaN() || p.pose.y.inches.isNaN() || p.pose.heading.v.isNaN() || (p.pose.x == 0.0.inches && p.pose.y == 0.0.inches && p.pose.heading == 0.0.radians)) {
            lastPose = null
        } else {
            lastPose = p.pose
            lastGoodPose = lastPose!!
        }
    }
}