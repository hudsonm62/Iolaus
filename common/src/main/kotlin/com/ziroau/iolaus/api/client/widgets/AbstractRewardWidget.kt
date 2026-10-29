package com.ziroau.iolaus.api.client.widgets

import com.teamresourceful.resourcefullib.client.screens.CursorScreen
import com.teamresourceful.resourcefullib.client.utils.CursorUtils
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils
import earth.terrarium.heracles.api.client.theme.QuestScreenTheme
import earth.terrarium.heracles.api.rewards.QuestReward
import earth.terrarium.heracles.client.handlers.ClientQuests
import earth.terrarium.heracles.client.ui.quest.AbstractQuestScreen
import earth.terrarium.heracles.client.widgets.buttons.ThemedButton
import earth.terrarium.heracles.common.constants.ConstantComponents
import earth.terrarium.heracles.common.handlers.progress.QuestProgress
import earth.terrarium.heracles.common.network.NetworkHandler
import earth.terrarium.heracles.common.network.packets.rewards.ClaimRewardsPacket
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

abstract class AbstractRewardWidget : AbstractDisplayWidget() {
    abstract val reward: QuestReward<*>
    abstract val questId: String
    abstract val progress: QuestProgress?
    abstract val interactive: Boolean

    companion object {
        private const val BUTTON_SIZE = 20
        private const val BUTTON_RIGHT_OFFSET = 30
        private const val BUTTON_TOP_OFFSET = 11

        /**
         * The texture used for the claim button in the reward widget.
         * By default, uses "heracles:textures/item/lootbag.png" which is what the builtin Heracles rewards use.
         */
        protected val CLAIM_BUTTON_TEXTURE: Identifier =
            Identifier.of("heracles", "textures/item/lootbag.png")

        fun getQuestContext(): Pair<String, QuestProgress?> {
            val screen = MinecraftClient.getInstance().currentScreen

            if (screen is AbstractQuestScreen) {
                val id = screen.content().id()
                return id to ClientQuests.getProgress(id)
            }

            return "" to null
        }
    }
    override val titleColor: Int
        get() = QuestScreenTheme.getRewardTitle()

    override val descriptionColor: Int
        get() = QuestScreenTheme.getRewardDescription()

    override fun renderExtra(graphics: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int) {
        if (interactive) renderClaimButton(graphics, x, y, width, mouseX, mouseY)
    }

    protected open fun canClaim(): Boolean = interactive && progress?.canClaim(reward.id()) == true

    protected open fun claimReward() {
        val questProgress = progress ?: return
        if (!canClaim()) return

        questProgress.claimReward(reward.id())

        NetworkHandler.CHANNEL.sendToServer(
            ClaimRewardsPacket(questId, reward.id())
        )
    }

    private fun renderClaimButton(graphics: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int) {
        val buttonX = x + width - BUTTON_RIGHT_OFFSET
        val buttonY = y + BUTTON_TOP_OFFSET
        val hoveredButton =
            mouseX > buttonX &&
                    mouseX < buttonX + BUTTON_SIZE &&
                    mouseY > buttonY &&
                    mouseY < buttonY + BUTTON_SIZE

        val claimable = canClaim()

        val sprite = when {
            !claimable -> ThemedButton.SPRITE_DISABLED
            hoveredButton -> ThemedButton.SPRITE_COMPLETABLE_HOVERED
            else -> ThemedButton.SPRITE_COMPLETABLE
        }

        graphics.drawGuiTexture(
            sprite, buttonX, buttonY,
            BUTTON_SIZE, BUTTON_SIZE
        )

        graphics.drawTexture(
            CLAIM_BUTTON_TEXTURE, buttonX + 2, buttonY + 2,
            0f, 0f, 16, 16, 16, 16
        )

        if (hoveredButton) {
            CursorUtils.setCursor(
                true,
                if (claimable) CursorScreen.Cursor.POINTER
                else CursorScreen.Cursor.DISABLED
            )
            if (claimable) {
                ScreenUtils.setTooltip(
                    ConstantComponents.Rewards.SELECT_CLAIM
                )
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int, width: Int): Boolean {
        if (mouseButton != 0 || !canClaim()) return false

        val buttonX = width - BUTTON_RIGHT_OFFSET
        val buttonY = BUTTON_TOP_OFFSET
        val clicked =
            mouseX > buttonX &&
                    mouseX < buttonX + BUTTON_SIZE &&
                    mouseY > buttonY &&
                    mouseY < buttonY + BUTTON_SIZE

        if (!clicked) return false

        MinecraftClient.getInstance().soundManager.play(
            PositionedSoundInstance.master(
                SoundEvents.UI_BUTTON_CLICK, 1.0f
            )
        )

        claimReward()
        return true
    }
}