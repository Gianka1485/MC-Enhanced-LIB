package com.adrianka.mcenhancedlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

/**
*/
public class MCELWidgetList extends AbstractSelectionList<MCELWidgetList.WidgetEntry> {
    public MCELWidgetList(Minecraft minecraft, int x, int y, int width, int height, int defaultEntryHeight) {
        super(minecraft, width, height, y, defaultEntryHeight);

        this.setX(x);
        this.centerListVertically = false;
    }

    public void addWidget(AbstractWidget widget, int spacing) { this.addEntry(new WidgetEntry(widget), widget.getHeight() + spacing); }

    public void clearWidgets() { this.clearEntries(); }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public static class WidgetEntry extends AbstractSelectionList.Entry<WidgetEntry> {
        private final AbstractWidget widget;

        public WidgetEntry(AbstractWidget widget) { this.widget = widget; }

        private void updateWidgetPosition() {
            this.widget.setX(this.getContentX());
            this.widget.setY(this.getContentY());
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            this.updateWidgetPosition();
            this.widget.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            this.updateWidgetPosition();
            return this.widget.mouseClicked(event, doubleClick);
        }

        @Override
        public boolean mouseReleased(MouseButtonEvent event) {
            this.updateWidgetPosition();
            return this.widget.mouseReleased(event);
        }

        @Override
        public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
            this.updateWidgetPosition();
            return this.widget.mouseDragged(event, dragX, dragY);
        }

        @Override
        public boolean keyPressed(KeyEvent event) { return this.widget.keyPressed(event); }

        @Override
        public boolean charTyped(CharacterEvent event) { return this.widget.charTyped(event); }

        @Override
        public void setFocused(boolean focused) { this.widget.setFocused(focused); }

        @Override
        public boolean isFocused() { return this.widget.isFocused(); }
    }
}