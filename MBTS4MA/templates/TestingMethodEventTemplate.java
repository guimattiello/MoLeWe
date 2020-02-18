	public void test{{testingmethodname}}{{param}} {
		String ces = "{{ces}}";
		
		new EventRunner().executeCompleteEventSequence(new {{testingclassname}}Adapter(solo), ces);
	}