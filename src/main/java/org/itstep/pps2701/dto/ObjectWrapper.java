package org.itstep.pps2701.dto;

import java.io.Serializable;

public interface ObjectWrapper<T> extends Serializable{
    T fromWrapper();
    void toWrapper(T item);
}