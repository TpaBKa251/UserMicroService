CREATE TABLE booking
(
    id           UUID NOT NULL,
    start_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status       VARCHAR(255),
    type         VARCHAR(255),
    user_id      UUID,
    time_slot_id UUID,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE time_slots
(
    id         UUID    NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_booked  BOOLEAN NOT NULL,
    user_id    UUID,
    CONSTRAINT pk_time_slots PRIMARY KEY (id)
);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_TIMESLOT FOREIGN KEY (time_slot_id) REFERENCES time_slots (id);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE time_slots
    ADD CONSTRAINT FK_TIME_SLOTS_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);