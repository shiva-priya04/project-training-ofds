package com.ofds.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;

class DatabaseIdentityLengthTest {

    @Test
    void generatedIdentifiersUseLongEnoughColumnLengths() throws NoSuchFieldException {
        Column restaurantIdColumn = Restaurant.class.getDeclaredField("resId").getAnnotation(Column.class);
        Column menuItemIdColumn = MenuItem.class.getDeclaredField("itemId").getAnnotation(Column.class);

        assertEquals(100, restaurantIdColumn.length());
        assertEquals(100, menuItemIdColumn.length());
    }
}
