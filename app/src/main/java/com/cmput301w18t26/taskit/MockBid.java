package com.cmput301w18t26.taskit;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kevingordon on 2018-03-04.
 */

public class MockBid extends Bid {
    public MockBid() {
        setUUID(UUID.randomUUID().toString());
        setDate(new Date());
        setOwner("AliceBob");
        setAmount(3.50);
        setStatus("BID");
    }


    public MockBid(String owner) {
        setUUID(UUID.randomUUID().toString());
        setDate(new Date());
        setOwner(owner);
        setAmount(3.50);
        setStatus("BID");
    }
}
