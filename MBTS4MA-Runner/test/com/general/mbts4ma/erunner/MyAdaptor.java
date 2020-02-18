package com.general.mbts4ma.erunner;

public class MyAdaptor {

	@Event(label = "a")
	public boolean a() {
		System.out.println("a");

		return true;
	}

	@Event(label = "b")
	public boolean b() {
		System.out.println("b");

		return true;
	}

	@Event(label = "c")
	public boolean c() {
		System.out.println("c");

		return true;
	}

	@Event(label = "d", rule = "R1")
	public boolean d1() {
		System.out.println("d#R1");

		return true;
	}

	@Event(label = "d", rule = "R2")
	public boolean d2() {
		System.out.println("d#R2");

		return true;
	}

	@Event(label = "e")
	public boolean e() {
		System.out.println("e");

		return false;
	}

}