package rpc.simplerpc2.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import rpc.simplerpc2.service.IHello;


public class ProxyFactory {

	public static <T> T create(Class<T> c, String ip, int port) {
		 InvocationHandler handler = new RpcProxy(ip, port, c);
		
		return (T) Proxy.newProxyInstance(c.getClassLoader(),
                new Class[] {c },
                handler);
	}
}
