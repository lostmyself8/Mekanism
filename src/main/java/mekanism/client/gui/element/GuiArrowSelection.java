package mekanism.client.gui.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.function.Supplier;
import mekanism.client.gui.GuiUtils;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiArrowSelection extends GuiTexturedElement {

    private static final ResourceLocation ARROW = MekanismUtils.getResource(ResourceType.GUI, "arrow_selection.png");

    private final Supplier<ITextComponent> textComponentSupplier;

    public GuiArrowSelection(IGuiWrapper gui, int x, int y, Supplier<ITextComponent> textComponentSupplier) {
        super(ARROW, gui, x, y, 33, 19);
        this.textComponentSupplier = textComponentSupplier;
    }

    @Override
    public boolean isMouseOver(double xAxis, double yAxis) {
        //TODO: override isHovered
        return this.field_230693_o_ && this.field_230694_p_ && xAxis >= field_230690_l_ + 16 && xAxis < field_230690_l_ + field_230688_j_ - 1 && yAxis >= field_230691_m_ + 1 && yAxis < field_230691_m_ + field_230689_k_ - 1;
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        ITextComponent component = textComponentSupplier.get();
        if (component != null) {
            int tooltipX = mouseX + 5;
            int tooltipY = mouseY - 5;
            GuiUtils.renderExtendedTexture(GuiInnerScreen.SCREEN, 2, 2, tooltipX - 3, tooltipY - 4, getStringWidth(component) + 6, 16);
            IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            MatrixStack matrix = new MatrixStack();
            //Make sure the text is above other renders like JEI
            matrix.translate(0.0D, 0.0D, 300);
            getFont().renderString(component.getFormattedText(), tooltipX, tooltipY, screenTextColor(), false, matrix.getLast().getMatrix(),
                  renderType, false, 0, MekanismRenderer.FULL_LIGHT);
            renderType.finish();
        }
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        minecraft.textureManager.bindTexture(getResource());
        blit(field_230690_l_, field_230691_m_, 0, 0, field_230688_j_, field_230689_k_, field_230688_j_, field_230689_k_);
    }
}