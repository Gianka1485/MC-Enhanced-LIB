package com.adrianka.mcenhancedlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

/**
 * A scrollable category list backed by enum values.
 *
 * <p>Each entry displays a translated title and notifies a callback
 * when its associated category is selected.</p>
 *
 * @param <E> the enum type representing the available categories
 */
public class MCELCategoryList<E extends Enum<E>>
        extends AbstractSelectionList<MCELCategoryList<E>.CategoryEntry> {

    private final Consumer<E> onCategorySelected;

    /**
     * Creates a category list with configurable dimensions and selection behavior.
     *
     * @param minecraft the Minecraft client instance
     * @param x the horizontal position of the list
     * @param y the vertical position of the list
     * @param width the list width
     * @param height the list height
     * @param defaultEntryHeight the default entry height
     * @param onCategorySelected the callback invoked when a category is selected
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
     * Adds a category with configurable text positioning.
     *
     * @param translatableKey the translation key for the category title
     * @param category the enum value associated with the entry
     * @param paddingX the horizontal content offset, or {@code null} to center horizontally
     * @param paddingY the vertical content offset, or {@code null} to center vertically
     * @param font the font used to render the title
     */
    public void addCategory(String translatableKey, E category, Integer paddingX, Integer paddingY, Font font) {
        this.addEntry(new CategoryEntry(Component.translatable(translatableKey), category, paddingX, paddingY, font));
    }

    /**
     * Reduces the row width to provide horizontal spacing within the list.
     *
     * @return the adjusted row width
     */
    @Override
    public int getRowWidth() { return this.width - 6; }

    /**
     * Disables the default narration behavior for this list.
     */
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    /**
     * Returns the enum value associated with the selected entry.
     *
     * @return the selected category, or {@code null} if none is selected
     */
    public E getSelectedCategory() {
        CategoryEntry selected = this.getSelected();
        return selected != null ? selected.category : null;
    }

    /**
     * Represents a selectable category with a translated title and configurable text alignment.
     */
    public class CategoryEntry extends AbstractSelectionList.Entry<CategoryEntry> {
        private final Component title;
        private final E category;
        private final Font font;

        private final Integer paddingX;
        private final Integer paddingY;

        /**
         * Creates a category entry with configurable content positioning.
         *
         * @param title the title displayed by the entry
         * @param category the associated enum value
         * @param paddingX the horizontal text offset, or {@code null} to center horizontally
         * @param paddingY the vertical text offset, or {@code null} to center vertically
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
         * Returns the enum value represented by this entry.
         *
         * @return the associated category
         */
        public E getCategory() { return this.category; }

        /**
         * Renders the category title using its configured alignment and a color determined by the hover state.
         */
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
                    this.paddingX != null
                            ? this.getContentX() + this.paddingX
                            : this.getContentX() + 1 + (this.getContentWidth() - this.font.width(this.title)) / 2,
                    this.paddingY != null
                            ? this.getContentY() + this.paddingY
                            : this.getContentY() + 1 + (this.getContentHeight() - this.font.lineHeight) / 2,
                    color
            );
        }

        /**
         * Selects this category and notifies the registered callback.
         *
         * @return {@code true} after processing the click
         */
        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            MCELCategoryList.this.setSelected(this);
            MCELCategoryList.this.onCategorySelected.accept(this.category);

            return true;
        }
    }
}