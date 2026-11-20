package com.adrianka.mcenhancedlib.server.data;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Simplifies the creation of {@link SavedDataType} instances using a constructor and a codec definition.
 *
 * @param <T> the type of saved data
 * @param modID the namespace used to identify the saved data
 * @param path the path of the saved data identifier
 * @param constructor the supplier used to create new data instances
 * @param codecGroup the function defining the fields to serialize and deserialize.
 *                   Use {@code instance -> instance.group(...).apply(instance, T::new)},
 *                   where {@code group(...)} contains field definitions such as
 *                   {@code Codec.BOOL.fieldOf("enabled").forGetter(T::isEnabled)},
 *                   and {@code apply(...)} specifies how to reconstruct the data instance
 */
public record MCELDataState<T extends SavedData>(
        String modID,
        String path,
        Supplier<T> constructor,
        Function<RecordCodecBuilder.Instance<T>, App<RecordCodecBuilder.Mu<T>, T>> codecGroup
) {

    /**
     * Creates a saved data type using the configured identifier, constructor, and codec.
     *
     * @return the configured saved data type
     */
    public SavedDataType<T> savedData() {
        return new SavedDataType<>(
                Identifier.fromNamespaceAndPath(modID, path),
                constructor,
                RecordCodecBuilder.create(codecGroup)
        );
    }
}