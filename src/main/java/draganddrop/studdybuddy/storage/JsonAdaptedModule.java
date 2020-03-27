package draganddrop.studdybuddy.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import draganddrop.studdybuddy.model.module.Module;


/**
 * Only stores ModuleName & ModuleCode. Omits internalList.
 * internalList will only extract from taskList when called for search by module.
 * when program terminates, internalList in Module will be wiped to minimise confusion.
 */

public class JsonAdaptedModule {
    private final String moduleName;
    private final String moduleCode;

    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleName") String moduleName,
                             @JsonProperty("moduleCode") String moduleCode) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
    }

    public JsonAdaptedModule(Module module) {
        this.moduleName = module.getModuleName();
        this.moduleCode = module.getModuleCode().toString();
    }

    public Module toModelType() {
        return new Module(moduleName, moduleCode);
    }
}
