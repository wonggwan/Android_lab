package io.github.wonggwan.lab4;

/**
 * Created by wonggwan on 2017/10/29.
 */

public class NoticeMessage {
    public Integer productpos;
    public Integer number_in_cart;
    public Boolean isstar;

    public NoticeMessage(Products product, Integer productIndex) {
        this.isstar=product.get_isstar();
        this.productpos=productIndex;
        this.number_in_cart= product.how_many_in_the_cart();
    }
}
