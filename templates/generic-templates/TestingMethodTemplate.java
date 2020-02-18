@Test
public void test{{testingmethodname}}() {
    String ces = "{{ces}}";

    assertTrue( new EventRunner().executeCompleteEventSequence({{testingclassname}}Adapter.class, ces));
}