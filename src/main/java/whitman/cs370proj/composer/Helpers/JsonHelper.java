package whitman.cs370proj.composer.Helpers;

import com.google.gson.GsonBuilder;
import whitman.cs370proj.composer.Interfaces.INote;
import whitman.cs370proj.composer.Models.BasicNote;
import whitman.cs370proj.composer.Models.GroupNote;
import whitman.cs370proj.composer.Models.NoteBase;

public final class JsonHelper {
    public static String ToJson(Object obj) {
        var gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        return gson.toJson(obj);
    }

    public static <T> T ToObject(String jsonStr, Class<T> clazz) {
        RuntimeTypeAdapterFactory<INote> typeFactory = RuntimeTypeAdapterFactory
                .of(INote.class, "type")
                .registerSubtype(NoteBase.class, NoteBase.class.getName())
                .registerSubtype(BasicNote.class, BasicNote.class.getName())
                .registerSubtype(GroupNote.class, GroupNote.class.getName());

        var gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapterFactory(typeFactory)
                .create();

        return gson.fromJson(jsonStr, clazz);
    }
}
