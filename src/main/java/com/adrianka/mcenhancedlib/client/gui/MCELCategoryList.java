package com.adrianka.mcenhancedlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class MCELCategoryList<E extends Enum<E>> extends AbstractSelectionList<MCELCategoryList<E>.CategoryEntry> {
    private final Consumer<E> onCategorySelected;

    public MCELCategoryList(Minecraft minecraft, int x, int y, int width, int height, Consumer<E> onCategorySelected) {
        super(minecraft, x, y, width, height);

        this.onCategorySelected = onCategorySelected;

        this.centerListVertically = false;
    }

    public void addCategory(String translatableKey, E category) { this.addEntry(new CategoryEntry(Component.translatable(translatableKey), category)); }

    @Override
    public int getRowWidth() { return this.width - 4; }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public E getSelectedCategory() {
        CategoryEntry selected = this.getSelected();
        return selected != null ? selected.category : null;
    }

    public class CategoryEntry extends AbstractSelectionList.Entry<CategoryEntry> {

        private final Component title;
        private final E category;

        public CategoryEntry(Component title, E category) {
            this.title = title;
            this.category = category;
        }

        public E getCategory() { return this.category; }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            int color = hovered ? 0xFFFFFFFF : 0xFFAAAAAA;

            graphics.text(MCELCategoryList.this.minecraft.font, this.title, this.getContentX() + 4, this.getContentY() + 3, color);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            MCELCategoryList.this.setSelected(this);
            MCELCategoryList.this.onCategorySelected.accept(this.category);

            return true;
        }
    }
}