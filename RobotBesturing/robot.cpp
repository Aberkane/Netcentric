#include "mbed.h"
#include "Servo.h"


Servo motor1(p23);
Servo motor2(p24);
Servo rotor(p25);

void control_robot(int command) {
                       
    rotor.calibrate(0.0005, 45.0); 
    motor1.calibrate(0.0005, 45.0); 
    motor2.calibrate(0.0005, 45.0); 
        switch(command) {
            /* Roteren en naar voren bewegen */
            // NW
            case 4: 
                motor1 = 0.0;
                motor2 = 1.0;
                rotor = 0.0;
                rotor.calibrate(0.00005, 45.0); 
                rotor = 0.0;
                break;
            // N
            case 3:  
                motor1 = 0.0;
                motor2 = 1.0;
                break;
            // NO
            case 2: 
                motor1 = 0.0;
                motor2 = 1.0;
                rotor.calibrate(-0.0160, 45.0); 
                rotor = 1.0;
                break;
            // ZW
            case 6: 
                motor1 = 1.0;
                motor2 = 0.0;
                rotor.calibrate(0.00005, 45.0); 
                rotor = 0.0;
                break; 
            // Z
            case 7: 
                motor1 = 1.0;
                motor2 = 0.0;
                break;
            // ZO
            case 8: 
                motor1 = 1.0;
                motor2 = 0.0;
                rotor.calibrate(-0.0160, 45.0); 
                rotor = 1.0;
                break; 
            case 0:
                rotor.calibrate(-0.0180, 45.0); 
                motor1.calibrate(-0.0180, 45.0); 
                motor2.calibrate(-0.0180, 45.0); 
                motor1 = 1.0;
                motor2 = 1.0;
                rotor = 1.0;
            
            /* Alleen roteren */
            // W
            case 5: 
                rotor = -1.0;
                motor1 = -1.0;
                motor2 = -1.0;
                break; 
            // O
            case 1: 
                rotor = 1.0;
                motor1 = 1.0;
                motor2 = 1.0; 
                break; 
        }
    
}