package com.farm2table.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class FraudDetectionServiceTest {

    private FraudDetectionService fraudDetectionService;

    @BeforeEach
    void setUp() {
        fraudDetectionService = new FraudDetectionService();
    }

    @Test
    void testIsTransactionFraudulent() {
        List<Double> transactionAmounts = Arrays.asList(10.0, 15.0, 12.0, 14.0, 11.0);
        double newTransaction = 20.0;

        boolean isFraudulent = fraudDetectionService.isTransactionFraudulent(transactionAmounts, newTransaction);

        assertTrue(isFraudulent, "The transaction should be marked as fraudulent");
    }

    @Test
    void testIsTransactionNotFraudulent() {
        List<Double> transactionAmounts = Arrays.asList(10.0, 15.0, 12.0, 14.0, 11.0);
        double newTransaction = 12.0;

        boolean isFraudulent = fraudDetectionService.isTransactionFraudulent(transactionAmounts, newTransaction);

        assertFalse(isFraudulent, "The transaction should not be fraudulent");
    }

    @Test
    void testIsTransactionFraudulentWithInsufficientData() {
        List<Double> transactionAmounts = Arrays.asList();
        double newTransaction = 12.0;

        boolean isFraudulent = fraudDetectionService.isTransactionFraudulent(transactionAmounts, newTransaction);

        assertFalse(isFraudulent, "With insufficient data, the transaction should not be marked as fraudulent");
    }
}