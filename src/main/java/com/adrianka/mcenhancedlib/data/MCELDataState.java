package com.adrianka.mcenhancedlib.data;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Use this record to create instances of {@link SavedDataType} without needing to write a new implementation
 * to register it in your "Main" class for data storage; it improves usability somewhat by simplifying
 * the implementation.
 * This might be unnecessary, but I also created it to practice my skills.
 *
 * @param codecGroup takes a simplified function {@code instance -> instance.group().apply()} where
 * {@code instance.group()} receives the group of encoded values; for example:
 * {@code Codec.DATA_TYPE.fieldOf("value_name").forGetter(T::getValue)}
 */
public record MCELDataState<T extends SavedData>(
        String modID,
        String path,
        Supplier<T> constructor,
        Function<RecordCodecBuilder.Instance<T>, App<RecordCodecBuilder.Mu<T>, T>> codecGroup
) {
    public SavedDataType<T> savedData() {
        return new SavedDataType<>(
                Identifier.fromNamespaceAndPath(modID, path),
                constructor,
                RecordCodecBuilder.create(codecGroup)
        );
    }
}
