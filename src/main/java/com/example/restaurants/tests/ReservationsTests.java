package com.example.restaurants.tests;

import com.example.restaurants.controllers.ReservationsService;
import com.example.restaurants.data.models.Tables;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.Test;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationsTests {

    @Autowired
    ReservationsService reservationsService;

    @Test
    public void find_NearestTables_CaseWhenTableExists_ReturnsTables(){
        final Integer expected = new Integer(6);

        Tables tables = reservationsService.findNearest(5);

        final Integer actual = tables.getType();

        Assert.assertEquals(actual, expected);
    }
}
