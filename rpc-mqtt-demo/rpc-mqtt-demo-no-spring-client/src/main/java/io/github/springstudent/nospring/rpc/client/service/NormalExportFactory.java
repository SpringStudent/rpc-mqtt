package io.github.springstudent.nospring.rpc.client.service;

import io.github.springstudent.client.core.RpcExportFactory;

public class NormalExportFactory implements RpcExportFactory {
    @Override
    public Object getExport(Class<?> clzz) {
        return new MyExportService();
    }
}
