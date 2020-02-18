package {{projectpackage}}.test.adapters;

import com.general.mbts4ma.erunner.*;
import com.robotium.solo.Solo;

public class {{testingclassname}}Adapter {

    private Solo solo = null;

    public {{testingclassname}}Adapter(Solo solo) {
        this.solo = solo;
    }

{{testingmethodtemplate}}

}
