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
            
            
            
APIs
----

1. a. URI: "/parking-service/onboard/new"

   b. Description: API to be invoked when a new Parking Space is to be Onboarded
   
   c. Request Body:    
              
              
              {
                  "name": "Indiranagar",
                  "zip_code": "560008",
                  "floors": [
                      {
                          "slots": [
                              {
                                  "bay_size": "SMALL",
                                  "count": 10
                              },
                              {
                                  "bay_size": "MEDIUM",
                                  "count": 10
                              }
                          ]
                      },
                      {
                          "slots": [
                              {
                                  "bay_size": "LARGE",
                                  "count": 5
                              },
                              {
                                  "bay_size": "XL",
                                  "count": 5
                              }
                          ]
                      }
                  ]
              }
 
 d. Response Body : Successfully onboarded new Parking Lot. : 200
 
e. errorResponse : 
                    
                    
                    {
                        "errorMessage" : "blahblah"
                        "status" : 400
                        "timestamp": 164786782
                    }
                    
                    
2. a. URI: "/parking-service/booking/park"

   b. Description: API to be invoked to park a vehicle
   
   c. Request Body: 
                
                
                {
                    "parking_lot_id" : 1,
                    "vehicle_size" : "SMALL",
                    "license_plate_number" : "qwerty",
                    "booking_duration_in_minutes" : 300
                }
                
   
   d. Response Body : 
   
                {
                    "ticketId" : 12345,
                    "parkingLotId" : 12,
                    "floorNumber" : 2,
                    "slotNumber" : 400,
                    "durationInMinutes" : 300,
                    "licensePlateNumber" : "KA01 2311D",
                    "parkingSlotSize" : 3,
                    "ticketPrice" : 400
                }
                
                
 
3. a. URI: "/parking-service/booking/unpark?ticket_id=12345"

   b. Description: API to free Parking Slot

   c. Request Param: ticket_id=12435

   d. Response Body : 
   
                    {
                        "ticket_id" : 123455,
                        "parking_lot_id" : 1234,
                        "floor_number" : 2,
                        "slot_number" : 400,
                        "duration_in_minutes"  300,
                        "license_plate_number" : "qwertyu",
                        "parking_slot_size" : "SMALL",
                        "ticket_price" : 400
                    }
   
              
