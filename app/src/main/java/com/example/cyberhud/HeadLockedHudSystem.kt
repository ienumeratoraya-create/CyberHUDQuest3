package com.example.cyberhud

import com.meta.spatial.core.Entity
import com.meta.spatial.core.Pose
import com.meta.spatial.core.SystemBase
import com.meta.spatial.core.Vector3
import com.meta.spatial.toolkit.PlayerBodyAttachmentSystem
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.Visible

class HeadLockedHudSystem(private val panelId: Int) : SystemBase() {
    private var shown = false

    override fun execute() {
        val head =
            systemManager
                .tryFindSystem<PlayerBodyAttachmentSystem>()
                ?.tryGetLocalPlayerAvatarBody()
                ?.head
                ?: return

        val headPose = head.tryGetComponent<Transform>()?.transform ?: return
        if (headPose == Pose()) return

        val forward = headPose.q * Vector3(0f, 0f, 1f)
        val hudPose = Pose()
        hudPose.t = headPose.t + forward * 0.92f
        hudPose.q = headPose.q

        val panel = Entity(panelId)
        panel.setComponent(Transform(hudPose))

        if (!shown) {
            panel.setComponent(Visible(true))
            shown = true
        }
    }
}
