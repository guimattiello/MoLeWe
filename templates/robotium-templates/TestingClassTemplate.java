package {{projectpackage}}.test;

import android.test.ActivityInstrumentationTestCase2;
import {{projectpackage}}.R;
import {{projectpackage}}.{{activity}};
import {{projectpackage}}.test.util.*;
import {{projectpackage}}.test.adapters.*;
import com.general.mbts4ma.erunner.*;
import com.robotium.solo.Solo;
{{otherimports}}

public class {{testingclassname}}Test extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    private boolean actualTest = false;

    @SuppressWarnings("unchecked")
    public {{testingclassname}}Test() {
        super({{activity}}.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    
{{testingmethodtemplate}}
    
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
