TestPhoneCallEvent.phoneCallAction(PhoneCallActions.call, "{{phonenumber}}");

solo.sleep(2000);

TestPhoneCallEvent.phoneCallAction(PhoneCallActions.accept, "{{phonenumber}}");

solo.sleep(1000);

TestPhoneCallEvent.phoneCallAction(PhoneCallActions.cancel, "{{phonenumber}}");

solo.sleep(5000);

TestPhoneCallEvent.goBack();