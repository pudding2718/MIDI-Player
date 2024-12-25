package whitman.cs370proj.composer.Helpers;

import whitman.cs370proj.composer.Interfaces.INote;

import java.util.List;
import java.util.Stack;

public final class OperationHistoryHelper {
    private final Stack<String> past;
    private final Stack<String> future;
    private String present;

    public OperationHistoryHelper() {
        this.past = new Stack<>();
        this.future = new Stack<>();
    }

    public void reset() {
        past.clear();
        future.clear();
    }

    public void setCurrent(List<INote> notes) {
        past.push(present);
        present = JsonHelper.ToJson(notes);
        future.clear();
    }

    public List<INote> undo() {
        if (past.isEmpty()) return null;

        var last = past.pop();
        future.push(present);
        present = last;

        if (present == null)
            return null;

        return List.of(JsonHelper.ToObject(present, INote[].class));
    }

    public List<INote> redo() {
        if (future.isEmpty()) return null;

        var last = future.pop();
        past.push(present);
        present = last;

        if (present == null)
            return null;

        return List.of(JsonHelper.ToObject(present, INote[].class));
    }
}
