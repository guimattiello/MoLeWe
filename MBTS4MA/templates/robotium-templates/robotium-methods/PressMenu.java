new Thread(new Runnable() {
    public void run() {
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
    }
}).start();