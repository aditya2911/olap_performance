package com.aditya.olap_performance.domain;

public enum TransactionStatus {
    ACCEPTED,
    SETTLED,
    AUTHORISED,
    PARTIALLY_REFUSED,
    REJECTED,
    COMPLETELY_REFUSED,
    CAPTURE_ERROR,
    PROCESSING
}
