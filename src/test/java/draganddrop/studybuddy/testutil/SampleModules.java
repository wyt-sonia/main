package draganddrop.studybuddy.testutil;

import draganddrop.studybuddy.model.module.Module;

public class SampleModules {
    public static Module[] getSampleModule() {
        Module cs2100 = new Module("Computer Organisation", "CS2100");
        Module cs2100clone1 = new Module("dasadsa" , "CS2100");
        Module cs2100clone2 = new Module("Computer Organisation", "CS1111");
        return new Module[]{cs2100, cs2100clone1, cs2100clone2};
    }
}
