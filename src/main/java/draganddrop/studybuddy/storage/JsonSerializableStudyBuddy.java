package draganddrop.studybuddy.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import draganddrop.studybuddy.commons.exceptions.IllegalValueException;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.statistics.GeneralStats;
import draganddrop.studybuddy.model.task.Task;

/**
 * An Immutable StudyBuddy that is serializable to JSON format.
 */
@JsonRootName(value = "studyBuddy")
class JsonSerializableStudyBuddy {

    //final private static String MESSAGE_DUPLICATE_TASK = "Task list contains duplicate task(s).";
    //final private static String MESSAGE_DUPLICATE_ARCHIVED_TASK = "Archived contains duplicate task(s).";
    final private static String MESSAGE_DUPLICATE_MODULES = "Module List contains duplicate module(s).";
    //final private static String MESSAGE_DUPLICATE_DUE_SOON_TASK = "Due soon list contains duplicate task(s).";

    private final List<JsonAdaptedTask> archivedTasks = new ArrayList<>();
    private final List<JsonAdaptedTask> tasks = new ArrayList<>();
    private final List<JsonAdaptedTask> dueSoonTasks = new ArrayList<>();
    private final List<JsonAdaptedModule> modules = new ArrayList<>();
    private final List<Integer> completeCountList = new ArrayList<>();
    private final List<Integer> overdueCountList = new ArrayList<>();
    private final GeneralStats generalStats = new GeneralStats();

    /**
     * Constructs a {@code JsonSerializableStudyBuddy} with the given tasks.
     */
    @JsonCreator
    public JsonSerializableStudyBuddy(@JsonProperty("tasks") List<JsonAdaptedTask> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Converts a given {@code ReadOnlyStudyBuddy} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableStudyBuddy}.
     */
    public JsonSerializableStudyBuddy(ReadOnlyStudyBuddy source) {
        tasks.addAll(source.getTaskList().stream().map(JsonAdaptedTask::new).collect(Collectors.toList()));
        archivedTasks.addAll(source.getArchivedList().stream()
            .map(JsonAdaptedTask::new).collect(Collectors.toList()));
        dueSoonTasks.addAll(source.getTaskList().stream()
                .map(JsonAdaptedTask::new).collect(Collectors.toList()));
        modules.addAll(source.getModuleList().stream().map(JsonAdaptedModule::new).collect(Collectors.toList()));
        completeCountList.addAll(source.getCompleteCountList());
        overdueCountList.addAll(source.getOverdueCountList());
        generalStats.copy(source.getGeneralStats());
    }

    /**
     * Converts this study buddy into the model's {@code StudyBuddy} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public StudyBuddy toModelType() throws IllegalValueException {
        StudyBuddy studyBuddy = new StudyBuddy();
        for (JsonAdaptedTask jsonAdaptedTask : tasks) {
            Task task = jsonAdaptedTask.toModelType();
            if (task.isStatusExpired()) {
                task.freshStatus();
            }
            studyBuddy.addTask(task);
        }
        for (JsonAdaptedTask jsonAdaptedTask : archivedTasks) {
            Task task = jsonAdaptedTask.toModelType();
            studyBuddy.addArchiveTask(task);
        }
        for (JsonAdaptedModule jsonAdaptedModule : modules) {
            Module module = jsonAdaptedModule.toModelType();
            if (studyBuddy.hasModule(module)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_MODULES);
            }
            studyBuddy.addModule(module);
        }
        studyBuddy.setCompleteList(this.completeCountList);
        studyBuddy.setOverdueList(this.overdueCountList);
        studyBuddy.getGeneralStats().copy(this.generalStats);

        Task.updateCurrentTaskList(new ArrayList<>(studyBuddy.getTaskList()));
        return studyBuddy;
    }

}
