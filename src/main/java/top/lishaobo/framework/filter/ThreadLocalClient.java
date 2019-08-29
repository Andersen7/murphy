package top.lishaobo.framework.filter;

public class ThreadLocalClient {

    private static ThreadLocal<ClientInfoBean> threadLocal = new ThreadLocal<ClientInfoBean>(){

        @Override
        protected synchronized ClientInfoBean initialValue() {
            return new ClientInfoBean();
        }
    };


    public static ClientInfoBean get() {
        return threadLocal.get();
    }


    public static void put(ClientInfoBean object) {
        threadLocal.set(object);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
