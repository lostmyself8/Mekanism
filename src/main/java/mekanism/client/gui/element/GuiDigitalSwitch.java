package mekanism.client.gui.element;

import java.util.function.BooleanSupplier;
import mekanism.client.gui.IGuiWrapper;
import mekanism.common.MekanismLang;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiDigitalSwitch extends GuiTexturedElement {

    public static final ResourceLocation SWITCH = MekanismUtils.getResource(ResourceType.GUI, "switch/switch.png");
    public static final int BUTTON_SIZE_X = 15, BUTTON_SIZE_Y = 8;

    private final SwitchType type;
    private final ResourceLocation icon;
    private final BooleanSupplier stateSupplier;
    private final ITextComponent tooltip;
    private final Runnable onToggle;

    public GuiDigitalSwitch(IGuiWrapper gui, int x, int y, ResourceLocation icon, BooleanSupplier stateSupplier, ITextComponent tooltip,
          Runnable onToggle, SwitchType type) {
        super(SWITCH, gui, x, y, type.sizeX, type.sizeY);
        this.type = type;
        this.icon = icon;
        this.stateSupplier = stateSupplier;
        this.tooltip = tooltip;
        this.onToggle = onToggle;
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        displayTooltip(tooltip, mouseX, mouseY);
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        minecraft.textureManager.bindTexture(getResource());
        blit(field_230690_l_ + type.switchX, field_230691_m_ + type.switchY, 0, stateSupplier.getAsBoolean() ? 0 : BUTTON_SIZE_Y, BUTTON_SIZE_X, BUTTON_SIZE_Y, BUTTON_SIZE_X, BUTTON_SIZE_Y * 2);
        blit(field_230690_l_ + type.switchX, field_230691_m_ + type.switchY + BUTTON_SIZE_Y + 1, 0, stateSupplier.getAsBoolean() ? BUTTON_SIZE_Y : 0, BUTTON_SIZE_X, BUTTON_SIZE_Y, BUTTON_SIZE_X, BUTTON_SIZE_Y * 2);

        minecraft.textureManager.bindTexture(icon);
        blit(field_230690_l_ + 6, field_230691_m_ + 21, 0, 0, 5, 5, 5, 5);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
        super.renderForeground(mouseX, mouseY);

        drawScaledCenteredText(MekanismLang.ON.translate(), relativeX + 8, relativeY + 1, 0x101010, 0.5F);
        drawScaledCenteredText(MekanismLang.OFF.translate(), relativeX + 8, relativeY + 10, 0x101010, 0.5F);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(MekanismSounds.BEEP.get(), 1.0F));
        onToggle.run();
    }

    public enum SwitchType {
        LOWER_ICON(BUTTON_SIZE_X, 40, 0, 0);

        private final int sizeX, sizeY;
        private final int switchX, switchY;

        SwitchType(int sizeX, int sizeY, int switchX, int switchY) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.switchX = switchX;
            this.switchY = switchY;
        }
    }
}
