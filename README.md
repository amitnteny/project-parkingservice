# project-parkingservice
Parking Lot Management System

Schema Design
Tables:

parking_lot [parking_lot_id bigint PK, 
             name         varchar2 NONNULL,
             zip_code     varchar2 NONNULL, 
             floor_count    bigint NONNULL, 
             slot_count     bigint NONNULL
            ]
            
floor       [floor_id       bigint PK, 
             floor_number   bigint NONNULL,
             slot_count     bigint NONNULL, 
             floor_count    bigint NONNULL, 
             parking_lot_id bigint NONNULL
            ]
            
 slot       [slot_id        bigint PK, 
             slot_number    bigint NONNULL,
             size           bigint NONNULL, 
             is_available  tinyint NONNULL, 
             floor_id       bigint NONNULL
            ]
            
 booking    [booking_id           bigint PK, 
             booking_time         bigint NONNULL,
             duration_minutes     bigint , 
             slot_id              bigint NONNULL, 
             license_plate_number varchar2 NONNULL,
             vehicle_size         tinyint NONNULL,
             status               varchar2 NONNULL
            ]
            
parking_ticket      [ ticket_id     bigint PK, 
                      booking_id    bigint NONNULL,
                      customer_id   bigint NONNULL, 
                      arrive_time   bigint NONNULL, 
                      depart_time   bigint NONNULL,
                      ticket_price  decimal NONNULL
                    ]         
            
            
            
            
