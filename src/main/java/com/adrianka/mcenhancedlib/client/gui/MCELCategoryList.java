package com.adrianka.mcenhancedlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class MCELCategoryList<E extends Enum<E>> extends AbstractSelectionList<MCELCategoryList<E>.CategoryEntry> {
    private final Consumer<E> onCategorySelected;

    /**
     * Creates a category list with the visual style and behavior of AbstractSelectionList.
     *
     * @param minecraft the Minecraft instance used by the list
     * @param x the horizontal position of the list
     * @param y the vertical position of the list
     * @param width the width of the list
     * @param height the height of the list
     * @param defaultEntryHeight the entry height
     * @param onCategorySelected the action performed when a category is selected
     */
    public MCELCategoryList(
            Minecraft minecraft,
            int x, int y, int width, int height,
            int defaultEntryHeight,
            Consumer<E> onCategorySelected) {
        super(minecraft, width, height, y, defaultEntryHeight);

        this.setX(x);
        this.onCategorySelected = onCategorySelected;
        this.centerListVertically = false;
    }

    /**
     * Adds a category with horizontally and vertically centered text.
     *
     * @param translatableKey the translation key used as the category title
     * @param category the enum value associated with this category
     */
    public void addCategory(String translatableKey, E category) {
        this.addEntry(new CategoryEntry(Component.translatable(translatableKey), category));
    }

    /**
     * Adds a category with a custom horizontal text padding and vertically centered text.
     *
     * @param translatableKey the translation key used as the category title
     * @param category the enum value associated with this category
     * @param xPaddingText the horizontal text padding from the content area's left edge
     */
    public void addCategory(String translatableKey, E category, int xPaddingText) {
        this.addEntry(new CategoryEntry(Component.translatable(translatableKey), category, xPaddingText));
    }

    /**
     * Adds a category with custom horizontal and vertical text padding.
     *
     * @param translatableKey the translation key used as the category title
     * @param category the enum value associated with this category
     * @param xPaddingText the horizontal text padding from the content area's left edge
     * @param yPaddingText the vertical text padding from the content area's top edge
     */
    public void addCategory(String translatableKey, E category, int xPaddingText, int yPaddingText) {
        this.addEntry(new CategoryEntry(Component.translatable(translatableKey), category, xPaddingText, yPaddingText));
    }

    /**
     * Adds a category using a custom font and custom text padding.
     *
     * @param translatableKey the translation key used as the category title
     * @param category the enum value associated with this category
     * @param xPaddingText the horizontal text padding from the content area's left edge
     * @param yPaddingText the vertical text padding from the content area's top edge
     * @param font the font used to render the category title
     */
    public void addCategory(String translatableKey, E category, int xPaddingText, int yPaddingText, Font font) {
        this.addEntry(new CategoryEntry(
                Component.translatable(translatableKey),
                category,
                xPaddingText,
                yPaddingText,
                font
        ));
    }

    @Override
    public int getRowWidth() { return this.width - 4; }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    /**
     * Returns the enum value associated with the currently selected category.
     *
     * @return the selected category, or null if no category is selected
     */
    public E getSelectedCategory() {
        CategoryEntry selected = this.getSelected();
        return selected != null ? selected.category : null;
    }

    public class CategoryEntry extends AbstractSelectionList.Entry<CategoryEntry> {

        private final Component title;
        private final E category;
        private final Font font;

        private final int xPaddingText;
        private final int yPaddingText;

        private final boolean centered;

        /**
         * Creates an entry with its title centered horizontally and vertically.
         *
         * @param title the text displayed by the entry
         * @param category the enum value associated with the entry
         */
        public CategoryEntry(Component title, E category) {
            this(title, category, 0, 0, MCELCategoryList.this.minecraft.font, true);
        }

        /**
         * Creates an entry with custom horizontal padding and vertically centered text.
         *
         * @param title the text displayed by the entry
         * @param category the enum value associated with the entry
         * @param xPaddingText the horizontal padding from the content area's left edge
         */
        public CategoryEntry(Component title, E category, int xPaddingText) {
            this(title, category, xPaddingText, 0, MCELCategoryList.this.minecraft.font, false);
        }

        /**
         * Creates an entry with custom horizontal and vertical padding.
         *
         * @param title the text displayed by the entry
         * @param category the enum value associated with the entry
         * @param xPaddingText the horizontal padding from the content area's left edge
         * @param yPaddingText the vertical padding from the content area's top edge
         */
        public CategoryEntry(Component title, E category, int xPaddingText, int yPaddingText) {
            this(title, category, xPaddingText, yPaddingText, MCELCategoryList.this.minecraft.font, false);
        }

        /**
         * Creates an entry with a custom font and custom text padding.
         *
         * @param title the text displayed by the entry
         * @param category the enum value associated with the entry
         * @param xPaddingText the horizontal padding from the content area's left edge
         * @param yPaddingText the vertical padding from the content area's top edge
         * @param font the font used to render the title
         */
        public CategoryEntry(Component title, E category, int xPaddingText, int yPaddingText, Font font) {
            this(title, category, xPaddingText, yPaddingText, font, false);
        }

        private CategoryEntry(
                Component title,
                E category,
                int xPaddingText,
                int yPaddingText,
                Font font,
                boolean centered
        ) {
            this.title = title;
            this.category = category;
            this.font = font;
            this.xPaddingText = xPaddingText;
            this.yPaddingText = yPaddingText;
            this.centered = centered;
        }

        /**
         * Returns the enum value associated with this entry.
         *
         * @return the category value
         */
        public E getCategory() { return this.category; }

        @Override
        public void extractContent(
                GuiGraphicsExtractor graphics,
                int mouseX,
                int mouseY,
                boolean hovered,
                float partialTick
        ) {
            int color = hovered ? 0xFFFFFFFF : 0xFFAAAAAA;

            int textX = this.getContentX() + this.xPaddingText;
            int textY = this.getContentY() + this.yPaddingText;

            if (this.centered) {
                textX = this.getContentX() + (this.getContentWidth() - this.font.width(this.title)) / 2;
                textY = this.getContentY() + (this.getContentHeight() - this.font.lineHeight) / 2;
            }

            graphics.text(this.font, this.title, textX, textY, color);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            MCELCategoryList.this.setSelected(this);
            MCELCategoryList.this.onCategorySelected.accept(this.category);

            return true;
        }
    }
}