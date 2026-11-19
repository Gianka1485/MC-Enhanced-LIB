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
            Consumer<E> onCategorySelected
    ) {
        super(minecraft, width, height, y, defaultEntryHeight);

        this.setX(x);
        this.onCategorySelected = onCategorySelected;
        this.centerListVertically = false;
    }

    /**
     * Adds a category using a custom font and custom text padding.
     *
     * @param translatableKey the translation key used as the category title
     * @param category the enum value associated with this category
     * @param paddingX the horizontal content padding from the content area's left edge. If {@code null},
     *                     the content will be centered horizontally
     * @param paddingY the vertical content padding from the content area's top edge. If {@code null},
     *                     the content will be centered vertically
     * @param font the font used to render the category title
     */
    public void addCategory(String translatableKey, E category, Integer paddingX, Integer paddingY, Font font) {
        this.addEntry(new CategoryEntry(Component.translatable(translatableKey), category, paddingX, paddingY, font));
    }

    @Override
    public int getRowWidth() { return this.width - 6; }

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

        private final Integer paddingX;
        private final Integer paddingY;

        /**
         * Creates an entry with a custom font and custom text padding.
         *
         * @param title the text displayed by the entry
         * @param category the enum value associated with the entry
         * @param paddingX the horizontal padding from the content area's left edge. If {@code null},
         *                     the content will be centered horizontally
         * @param paddingY the vertical padding from the content area's top edge. If {@code null},
         *                     the content will be centered vertically
         * @param font the font used to render the title
         */
        private CategoryEntry(Component title, E category, Integer paddingX, Integer paddingY, Font font) {
            this.title = title;
            this.category = category;
            this.font = font;
            this.paddingX = paddingX;
            this.paddingY = paddingY;
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
                int mouseX, int mouseY,
                boolean hovered,
                float partialTick
        ) {
            int color = hovered ? 0xFFFFFFFF : 0xFFAAAAAA;

            graphics.text(
                    this.font,
                    this.title,
                    this.paddingX != null ?
                            this.getContentX() + this.paddingX :
                            this.getContentX() + 1 + (this.getContentWidth() - this.font.width(this.title)) / 2,

                    this.paddingY != null ?
                            this.getContentY() + this.paddingY :
                            this.getContentY() + 1 + (this.getContentHeight() - this.font.lineHeight) / 2,
                    color
            );
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            MCELCategoryList.this.setSelected(this);
            MCELCategoryList.this.onCategorySelected.accept(this.category);

            return true;
        }
    }
}