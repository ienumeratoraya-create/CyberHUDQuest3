package com.example.cyberhud

import com.meta.spatial.core.Entity
import com.meta.spatial.core.SpatialFeature
import com.meta.spatial.runtime.LayerConfig
import com.meta.spatial.toolkit.AppSystemActivity
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.Visible
import com.meta.spatial.toolkit.createPanelEntity
import com.meta.spatial.vr.VRFeature

class CyberHudActivity : AppSystemActivity() {
    override fun registerFeatures(): List<SpatialFeature> = listOf(VRFeature(this))

    override fun onSceneReady() {
        super.onSceneReady()
        scene.enablePassthrough(true)

        Entity.createPanelEntity(
            HUD_PANEL_ID,
            R.layout.hud_overlay,
            Transform(),
            Visible(false),
        )
        systemManager.registerSystem(HeadLockedHudSystem(HUD_PANEL_ID))
    }

    override fun registerPanels(): List<PanelRegistration> =
        listOf(
            PanelRegistration(R.layout.hud_overlay) { _ ->
                config {
                    themeResourceId = R.style.PanelAppThemeTransparent
                    includeGlass = false
                    layerConfig = LayerConfig()
                    width = 1.58f
                    height = 0.88f
                }
            }
        )

    companion object {
        const val HUD_PANEL_ID = 9001
    }
}
