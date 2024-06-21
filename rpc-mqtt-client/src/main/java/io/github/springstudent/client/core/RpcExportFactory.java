package io.github.springstudent.client.core;


/**
 * 导出Rpc服务的工厂
 *
 * @author ZhouNing
 **/
public interface RpcExportFactory {

    /**
     * 实现该工厂，用于返回指定clzz类型的对象实例
     * @param clzz 类型
     * @author ZhouNing
     **/
    Object getExport(Class<?> clzz);
}
