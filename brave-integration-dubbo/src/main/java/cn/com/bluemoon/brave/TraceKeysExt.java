package cn.com.bluemoon.brave;

/**
 * Created by leonwong on 2016/12/5.
 */
enum TraceKeysExt {

    DUBBO_CONSUMER_URL("dubbo.consumer.url"),
    CONSUMER_RPC_EXCEPTION("consumer.rpc.exception"),
    DUBBO_PROVIDER_URL("dubbo.provider.url"),
    PROVIDER_RPC_EXCEPTION("provider.rpc.exception"),
    DUBBO_SERVER_ADDR("server.address"),
    DUBBO_CLIENT_ADDR("client.address");

    private String key;

    TraceKeysExt(String s) {
        this.setKey(s);
    }

    public String getKey() {
        return key;
    }

    public TraceKeysExt setKey(String key) {
        this.key = key;
        return this;
    }
}
