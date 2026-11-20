package com.adrianka.mcenhancedlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public class MCELWidgetList extends AbstractSelectionList<MCELWidgetList.WidgetEntry> {
    private final int marginX;

    /**
     * Creates a scrollable list for managing interactive widgets.
     *
     * @param minecraft the Minecraft client instance
     * @param x the horizontal position of the list
     * @param y the vertical position of the list
     * @param width the list width
     * @param height the list height
     * @param defaultEntryHeight the default entry height
     * @param marginX the horizontal margin applied to the rows
     */
    public MCELWidgetList(
            Minecraft minecraft,
            int x, int y, int width, int height,
            int defaultEntryHeight,
            int marginX
    ) {
        super(minecraft, width, height, y, defaultEntryHeight);

        this.setX(x);
        this.centerListVertically = false;

        this.marginX = marginX;
    }

    /**
     * Adds a widget with additional vertical spacing.
     *
     * @param widget the widget to add
     * @param spacing the space added after the widget
     */
    public void addWidget(AbstractWidget widget, int spacing) {
        this.addEntry(new WidgetEntry(widget), widget.getHeight() + spacing);
    }

    /**
     * Removes all widgets from the list.
     */
    public void clearWidgets() { this.clearEntries(); }

    /**
     * Disables the default narration behavior for this list.
     */
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    /**
     * Positions the scrollbar along the right edge of the list.
     *
     * @return the horizontal scrollbar position
     */
    @Override
    protected int scrollBarX() { return this.getRight() - this.scrollbarWidth(); }

    /**
     * Calculates the available widget width, accounting for the scrollbar when needed.
     *
     * @return the available width for widgets
     */
    public int getFixedWidth() {
        return this.scrollable() ? this.getRowWidth() - this.scrollbarWidth() : this.getRowWidth();
    }

    /**
     * Returns the row width after applying the horizontal margin.
     *
     * @return the adjusted row width
     */
    @Override
    public int getRowWidth() { return this.width - marginX; }

    /**
     * Prevents the list from rendering entry selection highlights.
     *
     * @return {@code false} to disable selection highlighting
     */
    @Override
    protected boolean entriesCanBeSelected() { return false; }

    public class WidgetEntry extends AbstractSelectionList.Entry<WidgetEntry> {
        private final AbstractWidget widget;

        /**
         * Creates an entry that wraps an interactive widget.
         *
         * @param widget the widget managed by this entry
         */
        public WidgetEntry(AbstractWidget widget) { this.widget = widget; }

        private void updateWidgetPosition() {
            this.widget.setX(this.getX());
            this.widget.setY(this.getY());
            this.widget.setWidth(MCELWidgetList.this.getFixedWidth());
        }

        /**
         * Updates the widget layout and delegates its rendering.
         */
        @Override
        public void extractContent(
                GuiGraphicsExtractor graphics,
                int mouseX, int mouseY,
                boolean hovered,
                float partialTick
        ) {
            this.updateWidgetPosition();
            this.widget.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }

        /**
         * Forwards mouse clicks to the wrapped widget.
         *
         * @return whether the widget handled the click
         */
        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            this.updateWidgetPosition();
            return this.widget.mouseClicked(event, doubleClick);
        }

        /**
         * Forwards mouse release events to the wrapped widget.
         *
         * @return whether the widget handled the release
         */
        @Override
        public boolean mouseReleased(MouseButtonEvent event) {
            this.updateWidgetPosition();
            return this.widget.mouseReleased(event);
        }

        /**
         * Forwards mouse dragging to the wrapped widget.
         *
         * @return whether the widget handled the drag
         */
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