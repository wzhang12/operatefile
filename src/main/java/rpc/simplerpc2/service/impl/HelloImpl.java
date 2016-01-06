package rpc.simplerpc2.service.impl;

import rpc.simplerpc2.service.IHello;

public class HelloImpl implements IHello {
	@Override
	public String sayHello(String name) {
		return "hello:" + name;
	}
}
