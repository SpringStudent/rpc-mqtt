package io.github.springstudent.common.bean;

/**
 * @author ZhouNing
 * @date 2025/7/29 9:03
 **/
public interface Orderable {

    /**
     * maximum value is the lowest priority
     * minimum value is the highest priority
     * @author ZhouNing
     * @date 2025/7/29 9:03
     **/
    int getOrder();
}
