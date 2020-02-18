String currentActivityName = solo.getCurrentActivity().getClass().getSimpleName();

solo.goBack();

solo.sleep(5000);

solo.goBackToActivity(currentActivityName);

solo.waitForActivity(currentActivityName);