package com.ra.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
@Builder
public class OrderDetailId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Orders order;
    @ManyToOne
    @JoinColumn(name = "product_detail_id",referencedColumnName = "id")
    private ProductDetail productDetail;

    @Override
    public int hashCode() {
        return Objects.hash(order, productDetail);
    }
}